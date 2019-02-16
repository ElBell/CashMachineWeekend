package rocks.zipcode.atm;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Window;
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.nio.BufferUnderflowException;
import java.util.*;

/**
 * @author ZipCodeWilmington
 */
public class AccountPane extends Pane {

    private TextField moneyField = new TextField();
    private CashMachine cashMachine;
    private Deque<String> balanceHistory = new ArrayDeque<String>();
    private Label idLabel;
    private Label nameLabel;
    private Label emailLabel;
    private Label balanceLabel;
    private Label messageLabel;
    private Text balanceHistoryArea = new Text();
    private MenuButton menuButton = new MenuButton();
    private Button btnWithdraw;
    private Button btnDeposit;
    private TextField idField;
    private GridPane gridPane;
    private CashMachineApp manager;

    public AccountPane() {
        cashMachine = new CashMachine(new Bank());
    }

    public AccountPane(CashMachine cashMachine, CashMachineApp cashMachineApp) {
        this.cashMachine = cashMachine;
        this.manager = cashMachineApp;
        gridPane = createRegistrationFormPane();
        controls(gridPane);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void enableAll() {
        nameLabel.setText("Name : " + cashMachine.getAccountName());
        emailLabel.setText("Email : " + cashMachine.getAccountEmail());
        balanceLabel.setText("Balance : " + String.format("%.2f", cashMachine.getAccountBalance()));
        balanceHistoryArea.setText("");
        balanceHistory.clear();

        if(cashMachine.isPremium()) {
            idLabel.setText("*Premium* ID : " + cashMachine.getAccountID());
            idLabel.setStyle("-fx-text-fill: goldenrod;");
            nameLabel.setStyle("-fx-text-fill: goldenrod;");
            emailLabel.setStyle("-fx-text-fill: goldenrod;");
        } else {
            idLabel.setText("ID : " + cashMachine.getAccountID());
            idLabel.setStyle("-fx-text-fill: black;");
            nameLabel.setStyle("-fx-text-fill: black;");
            emailLabel.setStyle("-fx-text-fill: black;");
        }


        btnDeposit.setDisable(false);
        btnWithdraw.setDisable(false);
        moneyField.setVisible(true);
        messageLabel.setVisible(true);

    }

    public void makeMenuItems() {
        menuButton.getItems().clear();
        for(Integer account: cashMachine.bank.getAccountIds()) {
            MenuItem newItem = new MenuItem(Integer.toString(account));
            newItem.setOnAction(event -> {
                cashMachine.login(account);
                enableAll();
            });
            menuButton.getItems().add(newItem);
        }
    }

    private String manageHistory(String lastAction) {
        balanceHistory.offerFirst(lastAction);
        if(balanceHistory.size() > 10) {
            balanceHistory.removeLast();
        }
        String result = String.join("\n", balanceHistory);
        return result;
    }

    private void controls(GridPane gridPane) {
        Label headerLabel = new Label("Cash Machine");

        idLabel = new Label("ID : " + cashMachine.getAccountID());
        nameLabel = new Label("Name : " + cashMachine.getAccountName());
        emailLabel = new Label("Email : " + cashMachine.getAccountEmail());
        balanceLabel = new Label(String.format("Balance : %.2f", cashMachine.getAccountBalance()));
        messageLabel = new Label("Please enter amount:");
        Label historyLabel = new Label("Recent Withdraws and Deposits");

        //Creating the menu
        menuButton = new MenuButton("Select Account", null);


        Button btnAddAccount = new Button("Make New Account");
        btnAddAccount.setOnAction(e -> {
            manager.changeToRegistrationPane();
        });

        Button selectIdButton = new Button("Get Account by ID:");
        selectIdButton.setOnAction(e -> {
            if(idField.getText().equals("")) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "No Entry", "Please enter valid ID");
            } else {
                int attemptedAccount = Integer.valueOf(idField.getText());
                if (cashMachine.hasAccount(attemptedAccount)) {
                    cashMachine.login(Integer.valueOf(idField.getText()));
                    idField.clear();
                    enableAll();
                } else {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Account Error!", attemptedAccount + " is not a registered account.");
                    idField.clear();
                }
            }

        });

        btnDeposit = new Button("Deposit");
        btnDeposit.setDisable(true);
        btnDeposit.setOnAction(e -> {
            if(moneyField.getText().equals("")) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "No Entry", "Please enter valid amount");
            } else {
                float amount = Float.parseFloat(moneyField.getText());
                StringBuilder lastAction = new StringBuilder(String.format("    $%-20.2f  +  $%-7.2f", cashMachine.getAccountBalance(), amount));
                balanceHistoryArea.setText(manageHistory(lastAction.toString()));
                cashMachine.deposit(amount);

                balanceLabel.setText(String.format("Balance : %.2f", cashMachine.getAccountBalance()));
            }
        });


        btnWithdraw = new Button("Withdraw");
        btnWithdraw.setDisable(true);
        btnWithdraw.setOnAction(e -> {
            float balance = cashMachine.getAccountBalance();
            if(moneyField.getText().equals("")) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "No Entry", "Please enter valid amount");
            } else {
                float amount = Float.parseFloat(moneyField.getText());
                if(amount <= balance || (cashMachine.isPremium() && amount <= balance + 100)) {
                    StringBuilder lastAction = new StringBuilder(String.format("    $%-20.2f  -  $%7.2f", balance, amount));
                    balanceHistoryArea.setText(manageHistory(lastAction.toString()));
                    cashMachine.withdraw(amount);
                    if(cashMachine.getAccountBalance() < 0) {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Overdraft", String.format("Account is now overdrawn by $%.2f", cashMachine.getAccountBalance()));
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Withdraw Error!", String.format("Insufficient funds to withdraw $%.2f" , amount));
                }
            }

            balanceLabel.setText(String.format("Balance : $%.2f" , cashMachine.getAccountBalance()));
        });


        moneyField = new TextField();
        moneyField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    moneyField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        idField = new TextField();
        idField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    idField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 21));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(5, 5,5,5));

        gridPane.add(btnAddAccount, 1, 1);
        gridPane.add(menuButton, 0, 1);
        gridPane.add(selectIdButton, 0, 2);
        gridPane.add(idField, 1, 2);

        idLabel.setFont(Font.font("Arial", FontWeight.BLACK, 14));
        gridPane.add(idLabel, 0,3, 2, 1);
        GridPane.setHalignment(idLabel, HPos.CENTER);
        gridPane.add(nameLabel, 0,4, 2, 1);
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        gridPane.add(emailLabel, 0,5, 2, 1);
        GridPane.setHalignment(emailLabel, HPos.CENTER);


        gridPane.add(btnDeposit, 0, 6);
        gridPane.add(btnWithdraw, 1, 6);

        gridPane.add(messageLabel, 0, 7);
        messageLabel.setVisible(false);
        moneyField.setPrefSize(100, 40);
        moneyField.setVisible(false);
        gridPane.add(moneyField, 1, 7);

        balanceLabel.setFont(Font.font("Courier", FontWeight.BOLD, 16));
        gridPane.add(balanceLabel, 0,8,2,1);
        GridPane.setHalignment(balanceLabel, HPos.CENTER);

        gridPane.add(historyLabel, 0, 9, 2, 1);
        gridPane.add(balanceHistoryArea, 0, 10, 2, 7);
        GridPane.setHalignment(balanceHistoryArea, HPos.CENTER);


    }

    private GridPane createRegistrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.TOP_CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(0, 0, 0, 10));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(150, 150, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        //columnTwoConstrains.setHgrow(Priority.ALWAYS);

        // columnTwoConstraints will be applied to all the nodes placed in column three.



        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("CashMachine");
        GridPane gridPane = createRegistrationFormPane();
        controls(gridPane);
        Scene scene = new Scene(gridPane, 400, 500);
        primaryStage.setScene(scene);
        //primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }
    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}

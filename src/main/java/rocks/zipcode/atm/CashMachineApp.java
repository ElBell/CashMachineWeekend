package rocks.zipcode.atm;

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
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author ZipCodeWilmington
 */
public class CashMachineApp extends Application {

    private TextField moneyField = new TextField();
    private CashMachine cashMachine = new CashMachine(new Bank());
    private Queue<String> balanceHistory = new LinkedList<String>();
    //private StringBuilder balanceHistory = new StringBuilder();
    private Label idLabel;
    private Label nameLabel;
    private Label emailLabel;
    private Label balanceLabel;
    private Text balanceHistoryArea = new Text();
    private Text messageArea = new Text();
    private TextField newAccountField = new TextField();
    private MenuButton menuButton = new MenuButton();
    private Button btnWithdraw;
    private Button btnDeposit;

    public void makeMenuItem(Integer account) {
        MenuItem newItem = new MenuItem(Integer.toString(account));
        newItem.setOnAction( event -> {
            cashMachine.login(account);

            idLabel.setText("ID : " + cashMachine.getAccountID());
            nameLabel.setText("Name : " + cashMachine.getAccountName());
            emailLabel.setText("Email : " + cashMachine.getAccountEmail());
            balanceLabel.setText("Balance : " + String.format("%.2f", cashMachine.getAccountBalance()));
            balanceHistoryArea.setText(balanceHistory.toString());

            btnDeposit.setDisable(false);
            btnWithdraw.setDisable(false);
        });
        menuButton.getItems().add(newItem);
    }

//    private Parent createContent() {
//        VBox vbox = new VBox(10);
//        vbox.setPrefSize(600, 580);
//
//        TextArea moneyArea = new TextArea();
///*
//        //Creating the menu
//        menuButton = new MenuButton("Select Account", null);
//
//        for(Integer account: cashMachine.bank.getAccountIds()) {
//            makeMenuItem(account);
//        }
////
////        menuItem3.setOnAction(event -> {
////            System.out.println("Option 3 selected via Lambda");
////        });
//
//        HBox hbox = new HBox(menuButton);
//
//
//        Button btnAddAccount = new Button("Make New Account");
//        btnAddAccount.setOnAction(e -> {
//            idArea.setText("ID : " + cashMachine.getAccountID());
//            cashMachine.bank.addAccount("Trial", "email", 100, false);
//            makeMenuItem(cashMachine.bank.getNumberOfAccounts());
//        });
//
//        Button btnDeposit = new Button("Deposit");
//        btnDeposit.setOnAction(e -> {
//            float amount = Float.parseFloat(field.getText());
//            StringBuilder lastAction = new StringBuilder(String.format("    %-20.2f  +  %-7.2f %n", cashMachine.getAccountBalance(), amount));
//            balanceHistory = lastAction.append(balanceHistory);
//            balanceHistoryArea.setText("Previous Withdraws and Deposits \n" + balanceHistory.toString());
//            cashMachine.deposit(amount);
//
//            balanceArea.setText("Balance : " + cashMachine.getAccountBalance());
//        });
//
//        Button btnWithdraw = new Button("Withdraw");
//        btnWithdraw.setOnAction(e -> {
//            float amount = Float.parseFloat(field.getText());
//            StringBuilder lastAction = new StringBuilder(String.format("    %-20.2f  -  %7.2f %n", cashMachine.getAccountBalance(), amount));
//            balanceHistory = lastAction.append(balanceHistory);
//            balanceHistoryArea.setText("Previous Withdraws and Deposits \n" + balanceHistory.toString());
//            cashMachine.withdraw(amount);
//
//            balanceArea.setText("Balance : " + cashMachine.getAccountBalance());
//        });
//*/
////        Button btnExit = new Button("Exit");
////        btnExit.setOnAction(e -> {
////            cashMachine.exit();
////
////            areaInfo.setText(cashMachine.toString());
////        });
//
//        FlowPane flowpane1 = new FlowPane();
//
//        flowpane1.getChildren().add(btnAddAccount);
//        flowpane1.getChildren().add(menuButton);
//
//        FlowPane flowpane2 = new FlowPane();
//        flowpane2.getChildren().add(btnDeposit);
//        flowpane2.getChildren().add(btnWithdraw);
//        //flowpane.getChildren().add(btnExit);
//        vbox.getChildren().addAll(flowpane1, newAccountField, hbox, idArea, nameArea, emailArea, messageArea, flowpane2, field, balanceArea, balanceHistoryArea);
//        return vbox;
//    }

    private String manageHistory(String lastAction) {
        balanceHistory.add(lastAction);
        if(balanceHistory.size() > 12) {
            balanceHistory.remove();
        }
        String result = String.join("\n", balanceHistory);
        return result;
    }
    private void controls(GridPane gridPane) {
        Label headerLabel = new Label("Cash Machine");

        idLabel = new Label("ID : " + cashMachine.getAccountID());
        nameLabel = new Label("Name : " + cashMachine.getAccountName());
        emailLabel = new Label("Email : " + cashMachine.getAccountEmail());
        balanceLabel = new Label("Balance : " + cashMachine.getAccountBalance());
        Label messageLabel = new Label("Please enter amount:");
        Label historyLabel = new Label("Previous Withdraws and Deposits");

        //Creating the menu
        menuButton = new MenuButton("Select Account", null);

        for(Integer account: cashMachine.bank.getAccountIds()) {
            makeMenuItem(account);
        }

        Button btnAddAccount = new Button("Make New Account");
        btnAddAccount.setOnAction(e -> {
            idLabel.setText("ID : " + cashMachine.getAccountID());
            cashMachine.bank.addAccount("Trial", "email", 100, false);
            makeMenuItem(cashMachine.bank.getNumberOfAccounts());
        });

        btnDeposit = new Button("Deposit");
        btnDeposit.setDisable(true);
        btnDeposit.setOnAction(e -> {
            float amount = Float.parseFloat(moneyField.getText());
            StringBuilder lastAction = new StringBuilder(String.format("    %-20.2f  +  %-7.2f", cashMachine.getAccountBalance(), amount));
            balanceHistoryArea.setText(manageHistory(lastAction.toString()));
            cashMachine.deposit(amount);

            balanceLabel.setText("Balance : " + cashMachine.getAccountBalance());
        });


        btnWithdraw = new Button("Withdraw");
        btnWithdraw.setDisable(true);
        btnWithdraw.setOnAction(e -> {
            float amount = Float.parseFloat(moneyField.getText());
            StringBuilder lastAction = new StringBuilder(String.format("    %-20.2f  -  %7.2f", cashMachine.getAccountBalance(), amount));
            balanceHistoryArea.setText(manageHistory(lastAction.toString()));
            cashMachine.withdraw(amount);

            balanceLabel.setText("Balance : " + cashMachine.getAccountBalance());
        });


        moneyField = new TextField();

        headerLabel.setFont(Font.font("Courier", FontWeight.BOLD, 21));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(5, 5,5,5));

        gridPane.add(btnAddAccount, 1, 1);
        gridPane.add(menuButton, 0, 1);

        idLabel.setFont(Font.font("Arial", FontWeight.BLACK, 14));
        gridPane.add(idLabel, 0,2, 2, 1);
        GridPane.setHalignment(idLabel, HPos.CENTER);
        gridPane.add(nameLabel, 0,3, 2, 1);
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        gridPane.add(emailLabel, 0,4, 2, 1);
        GridPane.setHalignment(emailLabel, HPos.CENTER);

        gridPane.add(btnDeposit, 0, 5);
        gridPane.add(btnWithdraw, 1, 5);

        gridPane.add(messageLabel, 0, 6);
        moneyField.setPrefSize(100, 40);
        gridPane.add(moneyField, 1, 6);

        balanceLabel.setFont(Font.font("Courier", FontWeight.BOLD, 16));
        gridPane.add(balanceLabel, 0,7,2,1);
        GridPane.setHalignment(balanceLabel, HPos.CENTER);

        gridPane.add(historyLabel, 0, 8, 2, 1);
        gridPane.add(balanceHistoryArea, 0, 9, 2, 7);
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("CashMachine");
        GridPane gridPane = createRegistrationFormPane();
        controls(gridPane);
        Scene scene = new Scene(gridPane, 400, 500);
        primaryStage.setScene(scene);
        //primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }


}

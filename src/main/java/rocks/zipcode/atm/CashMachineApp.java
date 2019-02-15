package rocks.zipcode.atm;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * @author ZipCodeWilmington
 */
public class CashMachineApp extends Application {

    private TextField field = new TextField();
    private CashMachine cashMachine = new CashMachine(new Bank());
    private Scene accountScene;
    private Scene createAccountScene;
    private StringBuilder balanceHistory = new StringBuilder();
    private Text idArea = new Text();
    private Text nameArea = new Text();
    private Text emailArea = new Text();
    private Text balanceArea = new Text();
    private Text balanceHistoryArea = new Text();
    private Text messageArea = new Text();
    private TextField newAccountField = new TextField();
    private MenuButton menuButton = new MenuButton();

    public void makeMenuItem(Integer account) {
        MenuItem newItem = new MenuItem(Integer.toString(account));
        newItem.setOnAction( event -> {
            cashMachine.login(account);

            idArea.setText("ID : " + cashMachine.getAcountID());
            nameArea.setText("Name : " + cashMachine.getAcountName());
            emailArea.setText("Email : " + cashMachine.getAcountEmail());
            messageArea.setText("Please enter amount to Withdraw or Deposit");
            balanceArea.setText("Balance : " + String.format("%.2f", cashMachine.getAcountBalance()));
            balanceHistoryArea.setText(balanceHistory.toString());
        });
        menuButton.getItems().add(newItem);
    }

    private Parent createContent() {
        VBox vbox = new VBox(10);
        vbox.setPrefSize(600, 580);

        TextArea moneyArea = new TextArea();

        //Creating the menu
        menuButton = new MenuButton("Select Account", null);

        for(Integer account: cashMachine.bank.getAccountIds()) {
            makeMenuItem(account);
        }
//
//        menuItem3.setOnAction(event -> {
//            System.out.println("Option 3 selected via Lambda");
//        });

        HBox hbox = new HBox(menuButton);


        Button btnAddAccount = new Button("Make New Account");
        btnAddAccount.setOnAction(e -> {
            idArea.setText("ID : " + cashMachine.getAcountID());
            cashMachine.bank.addAccount("Trial", "email", 100, false);
            makeMenuItem(cashMachine.bank.getNumberOfAccounts());
        });

        Button btnDeposit = new Button("Deposit");
        btnDeposit.setOnAction(e -> {
            float amount = Float.parseFloat(field.getText());
            StringBuilder lastAction = new StringBuilder(String.format("    %-20.2f  +  %-7.2f %n", cashMachine.getAcountBalance(), amount));
            balanceHistory = lastAction.append(balanceHistory);
            balanceHistoryArea.setText("Previous Withdraws and Deposits \n" + balanceHistory.toString());
            cashMachine.deposit(amount);

            balanceArea.setText("Balance : " + cashMachine.getAcountBalance());
        });

        Button btnWithdraw = new Button("Withdraw");
        btnWithdraw.setOnAction(e -> {
            float amount = Float.parseFloat(field.getText());
            StringBuilder lastAction = new StringBuilder(String.format("    %-20.2f  -  %7.2f %n", cashMachine.getAcountBalance(), amount));
            balanceHistory = lastAction.append(balanceHistory);
            balanceHistoryArea.setText("Previous Withdraws and Deposits \n" + balanceHistory.toString());
            cashMachine.withdraw(amount);

            balanceArea.setText("Balance : " + cashMachine.getAcountBalance());
        });

//        Button btnExit = new Button("Exit");
//        btnExit.setOnAction(e -> {
//            cashMachine.exit();
//
//            areaInfo.setText(cashMachine.toString());
//        });

        FlowPane flowpane1 = new FlowPane();

        flowpane1.getChildren().add(btnAddAccount);
        flowpane1.getChildren().add(menuButton);

        FlowPane flowpane2 = new FlowPane();
        flowpane2.getChildren().add(btnDeposit);
        flowpane2.getChildren().add(btnWithdraw);
        //flowpane.getChildren().add(btnExit);
        vbox.getChildren().addAll(flowpane1, newAccountField, hbox, idArea, nameArea, emailArea, messageArea, flowpane2, field, balanceArea, balanceHistoryArea);
        return vbox;
    }

    @Override
    public void start(Stage accountStage) throws Exception {
        accountStage.setScene(new Scene(createContent()));
        accountStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

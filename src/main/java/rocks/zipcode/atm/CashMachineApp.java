package rocks.zipcode.atm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rocks.zipcode.atm.bank.Bank;

public class CashMachineApp extends Application {
    private CashMachine cashMachine = new CashMachine(new Bank());
    private AccountPane accountPane = new AccountPane(cashMachine, this);
    private RegistrationPane registrationPane = new RegistrationPane(cashMachine, this);
    private Scene accountScene = new Scene(accountPane.getGridPane(), 400, 500);
    private Scene registrationScene = new Scene(registrationPane.getGridPane(), 400, 500);
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void changeToAccountPane() {
        accountPane.makeMenuItems();
        primaryStage.setScene(accountScene);
        primaryStage.show();
    }

    public void changeToRegistrationPane() {
        primaryStage.setScene(registrationScene);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("CashMachine");
        changeToAccountPane();
    }
}

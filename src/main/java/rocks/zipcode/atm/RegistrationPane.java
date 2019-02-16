package rocks.zipcode.atm;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import rocks.zipcode.atm.bank.Bank;

// https://www.callicoder.com/javafx-registration-form-gui-tutorial/

public class RegistrationPane extends Pane {
    private CashMachine cashMachine;
    private CashMachineApp manager;
    private GridPane gridPane;

    public RegistrationPane() {
        cashMachine = new CashMachine(new Bank());
    }

    public RegistrationPane(CashMachine cashMachine, CashMachineApp cashMachineApp) {
        this.cashMachine = cashMachine;
        this.manager = cashMachineApp;
        gridPane = createRegistrationFormPane();
        controls(gridPane);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Registration Form");

        // Create the registration form grid pane
        GridPane gridPane = createRegistrationFormPane();
        // Add UI controls to the registration form grid pane
        controls(gridPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(gridPane, 400, 500);
        // Set the scene in primary stage
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    private GridPane createRegistrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

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
        columnTwoConstrains.setMaxWidth(200);
        columnTwoConstrains.setMaxWidth(200);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void controls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Make a New Account");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));


        // Add Name Label
        Label nameLabel = new Label("Full Name : ");
        gridPane.add(nameLabel, 0,1);

        // Add Name Text Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1,1);


        // Add Email Label
        Label emailLabel = new Label("Email ID : ");
        gridPane.add(emailLabel, 0, 2);

        // Add Email Text Field
        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1, 2);

        Label premiumLabel = new Label("Premium Account : ");
        gridPane.add(premiumLabel, 0, 3);
        CheckBox premiumBox = new CheckBox();
        gridPane.add(premiumBox, 1, 3);

        Label balanceLabel = new Label("Starting Balance : ");
        gridPane.add(balanceLabel, 0, 4);

        Label IdLabel = new Label(String.format("The Account ID will be : %d", cashMachine.bank.getNumberOfAccounts() + 1));
        gridPane.add(IdLabel, 0, 5, 2, 1);
        IdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        GridPane.setHalignment(IdLabel, HPos.CENTER);

        // Add Password Field
        TextField balanceField = new TextField();
        balanceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    balanceField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        balanceField.setPrefHeight(40);
        gridPane.add(balanceField, 1, 4);

        // Add Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            if(nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your name");
                return;
            }
            if(emailField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your email");
                return;
            }
            if(balanceField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter a balance");
                return;
            }else {
                cashMachine.bank.addAccount(nameField.getText(), emailField.getText(), Integer.valueOf(balanceField.getText()), premiumBox.isSelected());
                nameField.clear();
                emailField.clear();
                balanceField.clear();

                manager.changeToAccountPane();
            }
                });

        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 6, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
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

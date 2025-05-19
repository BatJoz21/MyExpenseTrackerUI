package joz.javapractice.myexpensetrackerui.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import joz.javapractice.myexpensetrackerui.dto.AddingExpenseDTO;
import joz.javapractice.myexpensetrackerui.security.AuthenticationException;
import joz.javapractice.myexpensetrackerui.service.ExpenseDataParser;
import joz.javapractice.myexpensetrackerui.utils.HttpClientUtil;
import joz.javapractice.myexpensetrackerui.utils.JwtStorageUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class ExpenseController {
    @FXML
    public MFXComboBox<String> expenseTypeDropdown;
    @FXML
    public DatePicker datePicker;
    @FXML
    public MFXTextField amountField;
    @FXML
    public MFXComboBox<String> categoryDropDown;
    @FXML
    public MFXComboBox<String> accountDropdown;
    @FXML
    public MFXTextField noteField;
    @FXML
    public MFXButton submitButton;
    @FXML
    public MFXButton cancelButton;

    private MainController mainController;

    @FXML
    public void initialize(){
        expenseTypeDropdown.getItems().addAll("Expense", "Income");
        categoryDropDown.getItems().addAll(
                Arrays.asList("Food", "Transport", "Travel", "Household", "Health",
                        "Social life", "Gift", "Apparel", "Education", "Beauty", "Salary","Other")
        );
        accountDropdown.getItems().addAll(Arrays.asList("Bank", "Cash", "Card"));

        datePicker.setValue(LocalDate.now());

        datePicker.getEditor().setDisable(true);
        datePicker.getEditor().setOpacity(1);
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    @FXML
    public void handleSubmit(ActionEvent actionEvent) {
        if (!validateForm()){
            return;
        }

        int expenseType = expenseTypeDropdown.getValue().equals("Expense") ? 0 : 1;
        LocalDate date = datePicker.getValue();
        double amount = Double.parseDouble(amountField.getText());
        String category = categoryDropDown.getValue();
        String account = accountDropdown.getValue();
        String note = noteField.getText();

        String jsonBody = ExpenseDataParser
                .serializeExpense(new AddingExpenseDTO(expenseType, date, amount, category, account, note));
        String token = JwtStorageUtil.getToken();
        String path = "/expenses";

        System.out.println("Adding new expense: Type=" + expenseType);

        try {
            HttpClientUtil.sendPostRequestWithToken(path, token, jsonBody);

            if (mainController != null){
                mainController.refreshExpense();
            }
        } catch (AuthenticationException e){
            handleAuthenticationFailure();
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        } finally {
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }

        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private boolean validateForm(){
        if (expenseTypeDropdown.getValue() == null){
            showErrorMessage("Please select an expense type!");
            return false;
        }

        LocalDate date;
        try {
            date = datePicker.getValue();
            if (date == null ||
                date.isAfter(LocalDate.now()) ||
                date.isBefore(LocalDate.now().minusYears(1))){
                showErrorMessage("Please select a valid date!");
                return false;
            }
        } catch (Exception e){
            showErrorMessage("Invalid date selected!");
            return false;
        }

        double amount = 0;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter the correct number format!");
        }
        if (amountField.getText() == null ||
            amountField.getText().isEmpty() || amount < 0){
            showErrorMessage("Please enter a valid amount");
            return false;
        }

        if(categoryDropDown.getValue() == null){
            showErrorMessage("Please select a category");
            return false;
        }

        if (accountDropdown.getValue() == null){
            showErrorMessage("Please select an account!");
            return false;
        }

        if (noteField.getText() == null || noteField.getText().isEmpty()){
            showErrorMessage("Please enter an information on the note field!");
            return false;
        }

        return true;
    }

    private void showErrorMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleAuthenticationFailure() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Session Expired");
        alert.setHeaderText(null);
        alert.setContentText("Your session has expired. Please log in again.");

        alert.showAndWait();
    }
}

package joz.javapractice.myexpensetrackerui.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import joz.javapractice.myexpensetrackerui.security.AuthenticationException;
import joz.javapractice.myexpensetrackerui.utils.HttpClientUtil;
import joz.javapractice.myexpensetrackerui.utils.JwtStorageUtil;
import lombok.Setter;

import java.io.IOException;

public class DeleteExpenseController {
    @FXML
    public MFXButton cancelButton;
    @FXML
    public MFXButton confirmButton;

    @Setter
    private Integer expenseId = null;
    @Setter
    private MainController mainController;

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleDelete(ActionEvent actionEvent) {
        if (expenseId == null){
            showErrorMessage("The ID is empty!");
            return;
        }
        String token = JwtStorageUtil.getToken();
        String path = "/expenses/" + expenseId;

        try {
            HttpClientUtil.sendDeleteRequestWithToken(path, token);

            if (mainController != null){
                mainController.refreshExpense();
            }
        } catch (AuthenticationException e){
            handleAuthenticationFailure();
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        } finally {
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        }

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
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

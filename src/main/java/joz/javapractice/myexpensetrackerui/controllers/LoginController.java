package joz.javapractice.myexpensetrackerui.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import joz.javapractice.myexpensetrackerui.models.AuthRequest;
import joz.javapractice.myexpensetrackerui.service.AuthService;

import java.io.IOException;

public class LoginController {
    @FXML
    public MFXTextField usernameField;
    @FXML
    public MFXTextField passwordField;
    @FXML
    public MFXButton submitButton;

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        AuthRequest request = new AuthRequest();
        request.setUsername(usernameField.getText());
        request.setPassword(passwordField.getText());

        Stage stage = ((Stage) usernameField.getScene().getWindow());
        AuthService.login(request, stage);
    }

    @FXML
    public void handleCreateAccount(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/views/SignupScreen.fxml"));
            Stage stage = ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow());
            Scene signupScene = new Scene(loader.load());
            signupScene.getStylesheets().add(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/css/style.css").toExternalForm());

            stage.setScene(signupScene);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

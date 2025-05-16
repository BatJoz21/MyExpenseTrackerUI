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

public class SignupController {
    @FXML
    public MFXTextField fullNameField;
    @FXML
    public MFXTextField usernameField;
    @FXML
    public MFXTextField passwordField;
    @FXML
    public MFXButton signupButton;

    @FXML
    public void handleSignup(ActionEvent actionEvent) {
        AuthRequest request = new AuthRequest();
        request.setFullName(fullNameField.getText());
        request.setUsername(usernameField.getText());
        request.setPassword(passwordField.getText());

        Stage stage = ((Stage) fullNameField.getScene().getWindow());
        AuthService.signup(request, stage);
    }

    @FXML
    public void handleLoginAccount(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/views/LoginScreen.fxml"));
            Stage stage = ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow());
            Scene loginScene = new Scene(loader.load());
            loginScene.getStylesheets().add(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/css/style.css").toExternalForm());

            stage.setScene(loginScene);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

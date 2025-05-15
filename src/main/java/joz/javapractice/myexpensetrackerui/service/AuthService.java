package joz.javapractice.myexpensetrackerui.service;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import joz.javapractice.myexpensetrackerui.models.AuthRequest;
import joz.javapractice.myexpensetrackerui.models.AuthResponse;
import joz.javapractice.myexpensetrackerui.utils.HttpClientUtil;
import joz.javapractice.myexpensetrackerui.utils.JwtStorageUtil;

import java.io.IOException;
import java.net.http.HttpResponse;

public class AuthService {
    private static final String BASE_URL = "http://localhost:8080";
    private static final Gson gson = new Gson();

    public static void signup(AuthRequest request, Stage stage){
        new Thread(
                () -> {
                    try {
                        String url = BASE_URL + "/signup";
                        String jsonBody = gson.toJson(request);
                        HttpResponse<String> response = HttpClientUtil.sendPostRequest(url, jsonBody);
                        AuthResponse authResponse = gson.fromJson(response.body(), AuthResponse.class);
                        if ("success".equals(authResponse.getMessage())){
                            JwtStorageUtil.saveToken(authResponse.getToken());
                            Platform.runLater(
                                    () -> navigateToLoginScreen(stage)
                            );
                        }
                        else{
                            System.out.println(authResponse.getMessage());
                        }
                    } catch (IOException | InterruptedException e){
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    public static void login(AuthRequest request, Stage stage){
        new Thread(
                () -> {
                    try {
                        String url = BASE_URL + "/login";
                        String jsonBody = gson.toJson(request);
                        HttpResponse<String> response = HttpClientUtil.sendPostRequest(url, jsonBody);
                        AuthResponse authResponse = gson.fromJson(response.body(), AuthResponse.class);
                        if ("success".equals(authResponse.getMessage())){
                            JwtStorageUtil.saveToken(authResponse.getToken());
                            Platform.runLater(
                                    () -> navigateToLoadingScreen(stage)
                            );
                        }
                        else{
                            System.out.println(authResponse.getMessage());
                        }
                    } catch (IOException | InterruptedException e){
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    private static void navigateToLoginScreen(Stage stage){
        try {
            FXMLLoader loader = new FXMLLoader(AuthService.class
                    .getResource("/joz/javapractice/myexpensetrackerui/views/LoginScreen.fxml"));
            Scene loadingScene = new Scene(loader.load());
            loadingScene.getStylesheets().add(AuthService.class
                    .getResource("/joz/javapractice/myexpensetrackerui/css/style.css").toExternalForm());

            stage.setScene(loadingScene);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static void navigateToLoadingScreen(Stage stage){
        try {
            FXMLLoader loader = new FXMLLoader(AuthService.class
                    .getResource("/joz/javapractice/myexpensetrackerui/views/LoadingScreen.fxml"));
            Scene loadingScene = new Scene(loader.load());
            loadingScene.getStylesheets().add(AuthService.class
                    .getResource("/joz/javapractice/myexpensetrackerui/css/style.css").toExternalForm());

            stage.setScene(loadingScene);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

package joz.javapractice.myexpensetrackerui;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import joz.javapractice.myexpensetrackerui.utils.JwtStorageUtil;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        UserAgentBuilder.builder()
                        .themes(JavaFXThemes.MODENA)
                        .themes(MaterialFXStylesheets.forAssemble(true))
                        .setDeploy(true)
                        .setResolveAssets(true)
                        .build().setGlobal();

        stage.setTitle("My Expense Tracker App");
        stage.setWidth(900);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED);

        String token = JwtStorageUtil.getToken();
        if (token != null){
            loadMainScreen(stage);
        }else{
            loadLoginScreen(stage);
        }
    }

    public static void main(String[] args){
        launch(args);
    }

    private void loadLoadingScreen(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/joz/javapractice/myexpensetrackerui/views/LoadingScreen.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass()
                .getResource("/joz/javapractice/myexpensetrackerui/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private void loadLoginScreen(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/joz/javapractice/myexpensetrackerui/views/LoginScreen.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass()
                .getResource("/joz/javapractice/myexpensetrackerui/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private void loadMainScreen(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/joz/javapractice/myexpensetrackerui/views/MainScreen.fxml"));
        Scene mainScene = new Scene(loader.load());
        mainScene.getStylesheets().add(getClass()
                .getResource("/joz/javapractice/myexpensetrackerui/css/main-screen.css").toExternalForm());

        stage.setScene(mainScene);
        stage.show();
    }
}

module joz.javapractice.myexpensetrackerui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires static lombok;
    requires MaterialFX;
    requires java.prefs;
    requires javafx.base;

    opens joz.javapractice.myexpensetrackerui to javafx.fxml;
    opens joz.javapractice.myexpensetrackerui.controllers to javafx.fxml;
    opens joz.javapractice.myexpensetrackerui.models to com.google.gson, javafx.base;

    exports joz.javapractice.myexpensetrackerui;
}
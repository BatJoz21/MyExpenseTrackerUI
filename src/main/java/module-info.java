module joz.javapractice.myexpensetrackerui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires static lombok;


    opens joz.javapractice.myexpensetrackerui to javafx.fxml;
    exports joz.javapractice.myexpensetrackerui;
}
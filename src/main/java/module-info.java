module project.ruffinary {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires org.jsoup;
    requires java.sql;
    requires com.google.protobuf;
    requires java.desktop;


    opens project.ruffinary to javafx.fxml;
    exports project.ruffinary.controller;
    opens project.ruffinary.controller to javafx.fxml;
    opens project.ruffinary.modele.entite to javafx.base;
    exports project.ruffinary;

}
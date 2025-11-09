package project.ruffinary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import project.ruffinary.controller.MainController;
import project.ruffinary.view.Alert;

import java.io.IOException;
import java.util.Optional;

public class ApplicationFx extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/ruffinary/fxml/main.fxml"));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setStage(stage);
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/project/ruffinary/img/icon.ico")));
        stage.setTitle("Ruffinary");
        stage.setScene(scene);
        stage.show();
       stage.setOnCloseRequest(e -> {
            Optional<ButtonType> result = Alert.quit();
            if(result.isPresent() && result.get() != ButtonType.OK){
                e.consume();
            }

       });
    }

    public static void main(String[] args) {
        launch();
    }
}
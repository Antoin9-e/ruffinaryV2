package project.ruffinary.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.Desktop;
import java.net.URI;

public class AproposController {

    @FXML
    private ImageView photoView;

    @FXML
    private Hyperlink githubLink;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResource("/project/ruffinary/img/pp.jpg").toExternalForm());
        photoView.setImage(image);

        githubLink.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Antoin9-e"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
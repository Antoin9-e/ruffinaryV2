package project.ruffinary.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.ruffinary.modele.EntityManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    public MenuItem exportBtn;
    public MenuItem exportScriptBtn;
    public MenuItem exportCsvBtn;
    public MenuItem aboutBtn;
    private EntityManager entityManager;
    private Stage stage;
    private MainController mainController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entityManager = EntityManager.getInstance();
        exportScriptBtn.setOnAction(event -> {
            exportSql();
        });
        exportCsvBtn.setOnAction(event -> {
            exportTableur();
        });

        aboutBtn.setOnAction(event -> {
            showApropos();
        });
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void exportSql() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter un Script");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Fichiers SQL", "*.sql");
        fileChooser.getExtensionFilters().add(filter);
        File choice = fileChooser.showSaveDialog(stage);
        if(choice != null) entityManager.exportScript(choice.getAbsolutePath());
    }

    private void exportTableur() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter un CSV");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv");
        fileChooser.getExtensionFilters().add(filter);
        File choice = fileChooser.showSaveDialog(stage);
        if(choice != null) entityManager.exportCsv(choice.getAbsolutePath());
    }

    public void showApropos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/ruffinary/fxml/apropos.fxml"));
            Parent root = loader.load();
            MainController mainController = new MainController();
            Stage st = mainController.getModalStage("À propos",root);
            st.setMaxHeight(300);
            st.setMaxWidth(250);
            st.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

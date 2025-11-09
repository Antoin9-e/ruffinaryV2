package project.ruffinary.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import project.ruffinary.modele.EntityManager;
import project.ruffinary.modele.entite.Entity;
import project.ruffinary.modele.entite.Format;
import project.ruffinary.modele.entite.IEntity;
import project.ruffinary.modele.entite.Shelf;
import project.ruffinary.modele.exception.InvalidBarcodeException;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    public StackPane rootPane;
    public Button manualButton;
    public Button barcodeButton;
    public VBox manualForm;
    public TextField titleField;
    public TextField yearField;
    public TextField directorField;
    public Button submitManualButton;
    public VBox barcodeForm;
    public TextField barcodeField;
    public Button submitBarcodeButton;
    public TextField genreField;
    public TextField codeField;
    public ChoiceBox formatChoiceBox;
    public ChoiceBox shelfChoiceBox;
    public TextField editorField;
    public CheckBox laserdiscCheckBox;
    public ComboBox yearComboBox;
    public Button codeBtn;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        HBox a = new HBox();
        a.setSpacing(10);
        a.getChildren().add(new Label("Etagère:"));
        this.shelfChoiceBox = MainController.getShelfChoicebox(mainController.getEntityManager().getEntities());
        a.getChildren().add(shelfChoiceBox);
        manualForm.getChildren().add(7, a);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear; y >= 1900; y--) {
            yearComboBox.getItems().add(y);
        }
        manualForm.setVisible(false);
        manualForm.setManaged(false);
        barcodeForm.setVisible(false);
        barcodeForm.setManaged(false);
        manualButton.setOnAction(e -> {
            manualForm.setVisible(true);
            manualForm.setManaged(true);
            barcodeForm.setVisible(false);
            barcodeForm.setManaged(false);
        });
        barcodeButton.setOnAction(e -> {
            barcodeForm.setVisible(true);
            barcodeForm.setManaged(true);
            manualForm.setVisible(false);
            manualForm.setManaged(false);
        });
        submitBarcodeButton.setOnAction(e -> {
            lookForEntity();
        });
        submitManualButton.setOnAction(e -> {
            createManualEntity();
        });
        codeBtn.setOnAction(e -> {
            setGenerateCode();
        });
    }


    public void setGenerateCode() {
        String code = mainController.getEntityManager().getMaxCodeFromDb();
        System.out.println(code);
        codeField.setText(code);
    }


    public void lookForEntity() {
        String bc = barcodeField.getText();
        EntityManager em = mainController.getEntityManager();
        if (laserdiscCheckBox.isSelected()) em.setApi(Format.LD);
        else em.setApi(Format.DVD);
        try
        {
            String resp = em.getApi().search(bc);
            IEntity neo = em.getApi().getParser().parse(resp);
            Entity conf = project.ruffinary.view.Alert.confirmAdd(neo, em.getEntities());
            if (conf != null) {
                mainController.getEntityManager().getEntities().add(conf);
                mainController.getEntityManager().addEntity(conf);
                for (IEntity e : mainController.getEntityManager().getEntities()) {
                    if (e instanceof Shelf sh) {
                        if (sh.getNom().equals(conf.getShelfId())) {
                            sh.addChild(conf);
                        }
                    }
                }
            }
        }
        catch (InvalidBarcodeException e)
        {
            Alert alert = project.ruffinary.view.Alert.errorBC(e.getMessage());
            alert.show();
        }
    }


    public void createManualEntity() {
        String title = titleField.getText();
        String director = directorField.getText();
        String year;
        String genre = genreField.getText();
        String code = codeField.getText();
        int format = formatChoiceBox.getSelectionModel().getSelectedIndex();
        String editor = editorField.getText();
        if (code == null || code.length() < 12 || code.length() > 13) {
            Alert a = project.ruffinary.view.Alert.errorBC("Le code est vide ou non valide");
            a.show();
            return;
        }
        if (yearComboBox.getValue() == null) {
            year = null;
        } else {
            year = yearComboBox.getValue().toString();
        }
        if (title.isEmpty()) {
            Alert a = project.ruffinary.view.Alert.warningTitle();
            a.show();
            return;
        }
        if (format == -1) {
            Alert a = project.ruffinary.view.Alert.warningFormat();
            a.show();
            return;
        }
        String shelf = null;
        if (shelfChoiceBox.getSelectionModel().getSelectedItem() != null) {
            shelf = shelfChoiceBox.getValue().toString();
            if (shelf.equalsIgnoreCase("--Aucune--")) shelf = null;
        }
        Entity conf = new Entity(format, shelf, code, editor, genre, director, year, title, null);

        if (conf != null) {
            mainController.getEntityManager().getEntities().add(conf);
            mainController.getEntityManager().addEntity(conf);
            for (IEntity e : mainController.getEntityManager().getEntities()) {
                if (e instanceof Shelf sh) {
                    if (sh.getNom().equals(conf.getShelfId())) {
                        sh.addChild(conf);
                    }
                }
            }
        }
        manualForm.setVisible(false);
        manualForm.setManaged(false);
        titleField.clear();
        directorField.clear();
        genreField.clear();
        codeField.clear();
        editorField.clear();
    }
}

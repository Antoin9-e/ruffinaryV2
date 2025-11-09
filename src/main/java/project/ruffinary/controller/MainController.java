package project.ruffinary.controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;
import project.ruffinary.modele.EntityManager;
import project.ruffinary.modele.api.IApi;
import project.ruffinary.modele.db.Database;
import project.ruffinary.modele.entite.Entity;
import project.ruffinary.modele.entite.Format;
import project.ruffinary.modele.entite.IEntity;
import project.ruffinary.modele.entite.Shelf;
import project.ruffinary.view.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    public TextField searchField;
    public TableColumn<IEntity, String> colGenre;
    public Label dbStatusLabel;
    @FXML
    private Button entityManagerBtn;

    @FXML
    private Button shelfManagerBtn;
    @FXML
    private Button addBtn;

    @FXML
    private TableView entityTable;

    @FXML
    private TableColumn<IEntity, String> colCode;
    @FXML
    private TableColumn<IEntity, String> colTitle;
    @FXML
    private TableColumn<IEntity, String> colDirector;
    @FXML
    private TableColumn<IEntity, String> colFormat;
    @FXML
    private TableColumn<IEntity, String> colShelf;
    private EntityManager entityManager;

    private Stage primaryStage;

    private MenuController menuController;


    public static ChoiceBox<String> getFormatChoicebox() {
        ChoiceBox<String> format = new ChoiceBox<>();
        for (Format fmt : Format.values()) {
            format.getItems().add(fmt.name());
        }
        return format;
    }

    public static ChoiceBox<String> getShelfChoicebox(ObservableList<IEntity> entities) {
        ChoiceBox<String> etagere = new ChoiceBox<>();
        etagere.getItems().add("--Aucune--");
        for (IEntity e : entities) {
            if (e instanceof Shelf sh) {
                String label = sh.getNom();
                etagere.getItems().add(label);
            }
        }
        return etagere;
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setMenuController() {
        if (primaryStage != null) {
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            try {
                Parent menuRoot = menuLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MenuController menuController = menuLoader.getController();
            menuController.setStage(primaryStage);
            menuController.setMainController(this);


        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        entityManager = EntityManager.getInstance();
        setMenuController();
        if (entityManager.setDatabase()) {
            dbStatusLabel.setText("✅ Connexion réussie");
            dbStatusLabel.setStyle("-fx-text-fill: green;");
            entityTable.setFixedCellSize(-1); // désactive la hauteur fixe
            entityTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            showData();
        } else {
            dbStatusLabel.setText("❌ Échec de la connexion");
            dbStatusLabel.setStyle("-fx-text-fill: red;");
        }
        entityManagerBtn.setOnAction(e -> {
            Entity selected = (Entity) entityTable.getSelectionModel().getSelectedItem();
            showCrudModal(selected);

        });

        shelfManagerBtn.setOnAction(e -> {
            showShelfModal();

        });

        addBtn.setOnAction(e -> {
            showAddModal();
        });

        PauseTransition pause = new PauseTransition(Duration.millis(300));
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            pause.setOnFinished(e -> search(newVal));
            pause.playFromStart();
        });

        entityManagerBtn.setDisable(true);
        entityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            entityManagerBtn.setDisable(newVal == null);
        });

    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @FXML
    private void showData() {
        entityManager.loadEntitiesFromDB();
        FilteredList<IEntity> onlyEntities = new FilteredList<IEntity>(
                entityManager.getEntities().filtered(e -> e instanceof Entity)
        );
        entityTable.setItems(entityManager.getEntities().filtered(e -> e instanceof Entity));
        colShelf.setCellValueFactory(cellData -> ((Entity) cellData.getValue()).shelfIdProperty());
        colCode.setCellValueFactory(celldata -> ((Entity) celldata.getValue()).eanProperty());
        colTitle.setCellValueFactory(celldata -> ((Entity) celldata.getValue()).titleProperty());
        colDirector.setCellValueFactory(celldata -> ((Entity) celldata.getValue()).directorProperty());
        colFormat.setCellValueFactory(celldata -> ((Entity) celldata.getValue()).formatProperty().asString());
        colGenre.setCellValueFactory(cellData -> ((Entity) cellData.getValue()).genreProperty());

    }

    @FXML
    private void showCrudModal(IEntity entity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/ruffinary/fxml/crud.fxml"));
            Parent root = loader.load();
            CrudController crudController = loader.getController();
            crudController.setEntity(entity);
            crudController.setMainController(this);
            Stage modalStage = getModalStage("Gestion de l'entité", root);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showShelfModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/ruffinary/fxml/shelf.fxml"));
            Parent root = loader.load();
            ShelfController shelfController = loader.getController();
            shelfController.setMainController(this);
            shelfController.setEntityList(getEntityManager().getEntities());
            shelfController.show();
            Stage modalStage = getModalStage("Gestion des étagères", root);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAddModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/ruffinary/fxml/add.fxml"));
            Parent root = loader.load();
            AddController addController = loader.getController();
            addController.setMainController(this);
            Stage modalStage = getModalStage("add a movie", root);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getModalStage(String message, Parent root) {
        Stage modalStage = new Stage();
        modalStage.setTitle(message);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.UTILITY);
        modalStage.setResizable(false);
        Scene scene = new Scene(root, 800, 500);
        modalStage.setScene(scene);
        modalStage.centerOnScreen();
        return modalStage;
    }

    public void search(String filter) {
        entityTable.setItems(
                getEntityManager().getEntities().filtered(e -> {
                    if (e instanceof Entity entity) {
                        String filterLower = filter.toLowerCase();

                        return safeContains(entity.getTitle(), filterLower)
                                || safeContains(entity.getEan(), filterLower)
                                || safeContains(entity.getDirector(), filterLower)
                                || safeContains(entity.getEditor(), filterLower)
                                || Optional.ofNullable(entity.formatProperty())
                                .map(p -> p.getValue())
                                .map(v -> v.name())
                                .map(String::toLowerCase)
                                .map(s -> s.contains(filterLower))
                                .orElse(false);
                    }
                    return false;
                })
        );
    }

    private boolean safeContains(String value, String filter) {
        return value != null && value.toLowerCase().contains(filter);
    }



}


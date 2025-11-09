package project.ruffinary.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import project.ruffinary.modele.entite.Entity;
import project.ruffinary.modele.entite.IEntity;
import project.ruffinary.modele.entite.Shelf;
import project.ruffinary.view.Alert;

import java.net.URL;
import java.util.*;

public class ShelfController {

    private final Deque<List<Node>> flowPaneHistory = new ArrayDeque<>();
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private FlowPane shelvesFlow;
    @FXML
    private VBox detailContent;
    @FXML
    private Label statusLabel;
    @FXML
    private Button backBtn;
    private FlowPane prevFlow;


    private FilteredList<IEntity> entityList;
    private MainController mainController;

    public void setEntityList(ObservableList<IEntity> entityList) {
        this.entityList = new FilteredList<>(entityList.filtered(entity -> entity instanceof Shelf));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void updateFlowPane(List<Node> flowPane) {
        saveLastState();
        shelvesFlow.getChildren().setAll(flowPane);

    }


    public void show() {
        List<Node> flowPane = new ArrayList<>();
        shelvesFlow.getChildren().clear();

        setEntityList(mainController.getEntityManager().getEntities());
        entityList.forEach(entity -> {
            if (entity instanceof Shelf db) {
                if (db.getParent_id() == null) {
                    flowPane.add(createTile(db));
                }
            }

        });
        updateFlowPane(flowPane);



        backBtn.setOnMouseClicked(event -> {
            if (!flowPaneHistory.isEmpty() && flowPaneHistory.size() > 1) {
                System.out.println(flowPaneHistory.size());
                List<Node> previous = flowPaneHistory.pop();
                shelvesFlow.getChildren().setAll(previous);
            }


        });

    }

    private StackPane createTile(IEntity entity) {
        double width = 160;
        double height = 100;
        String cardFill = "#f8fafb";
        String cardStroke = "#d0d7dd";
        String hoverFill = "#e9f3ff";
        String titleColor = "#212121";
        String subtitleColor = "#555555";

        Rectangle card = new Rectangle(width, height);
        card.setArcWidth(12);
        card.setArcHeight(12);
        card.setFill(Color.web(cardFill));
        card.setStroke(Color.web(cardStroke));

        String id = entity instanceof Shelf ? ((Shelf) entity).getNom() : entity instanceof Entity ? (((Entity) entity).getTitle()) : "inconnu";
        int countE = entity instanceof Shelf ? ((Shelf) entity).getEntityChilds().size() : 0;
        int countS = entity instanceof Shelf ? ((Shelf) entity).getShelfChilds().size() : 0;

        Label title = new Label(id);
        title.setFont(Font.font(16));
        title.setTextFill(Color.web(titleColor));


        Label subtitle = new Label(countE + " films");
        subtitle.setFont(Font.font(12));
        subtitle.setTextFill(Color.web(subtitleColor));
        Label subtitle2 = new Label(countS + " Etagères");
        subtitle2.setFont(Font.font(12));
        subtitle2.setTextFill(Color.web(subtitleColor));

        VBox vbox;

        if (entity instanceof Shelf) vbox = new VBox(6, title, subtitle, subtitle2);
        else vbox = new VBox(6, title);
        vbox.setAlignment(Pos.CENTER);

        StackPane tile = new StackPane(card, vbox);
        tile.setPrefSize(width, height);
        tile.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        tile.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        tile.setOnMouseEntered(e -> {
            card.setFill(Color.web(hoverFill));
            card.setStroke(Color.web("#7eaee0"));
        });
        tile.setOnMouseExited(e -> {
            card.setFill(Color.web(cardFill));
            card.setStroke(Color.web(cardStroke));
        });

        tile.setOnMouseClicked(e -> {
            if (entity instanceof Shelf sh) {
                saveLastState();
                List<Node> newTiles = new ArrayList<>();
                sh.getChilds().forEach(shelf -> newTiles.add(createTile(shelf)));
                shelvesFlow.getChildren().setAll(newTiles);
            }
        });
        return tile;
    }

    private void saveLastState() {
        List<Node> snapshot = new ArrayList<>(shelvesFlow.getChildren());
        flowPaneHistory.push(snapshot);
    }

    public void addNewShelf() {
        Shelf nShelf = Alert.createShelf(entityList);
        if (nShelf != null) {
            mainController.getEntityManager().addShelf(nShelf);
            show();
        }

    }

    public void deleteShelf() {
        Shelf id = Alert.removeShelf(entityList);
        if (id != null) {
            List<IEntity> entities = mainController.getEntityManager().getEntities();
            List<IEntity> snapshot = new ArrayList<>(entities);

            for (IEntity entity : snapshot) {

                if (entity instanceof Entity en) {
                    if (Objects.equals(en.getShelfId(), id.getNom())) {
                        en.setShelfId(null);
                    }
                }


                if (entity instanceof Shelf sh) {
                    if (Objects.equals(sh.getParent_id(), id.getNom())) {
                        sh.setParent_id(null);
                    }

                    if (sh.getShelfChilds() != null && sh.getShelfChilds().contains(id)) {
                        if (sh.getChilds() != null) {
                            sh.getChilds().remove(id);
                        }
                    }

                    if (Objects.equals(sh.getNom(), id.getNom())) {
                        mainController.getEntityManager().deleteShelf(sh);
                    }
                }
            }


        }


    }

/*

public FlowPane copy( FlowPane origine){
        FlowPane copy = new FlowPane();
        for(Node node : origine.getChildren()){
            if(node instanceof StackPane source){
                StackPane copie = new StackPane();
                for(Node child : source.getChildren()){
                    if(child instanceof VBox vb){
                        VBox vbcopie = new VBox();
                        for(Node children : vb.getChildren()){
                            if(children instanceof Label){
                                Label label = new Label();
                                label.setText(((Label) children).getText());
                            }
                        }
                    }
                }
            }
        }
}

 */

}

package project.ruffinary.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import project.ruffinary.modele.entite.Entity;
import project.ruffinary.modele.entite.IEntity;
import project.ruffinary.modele.entite.Shelf;
import project.ruffinary.view.Alert;



public class CrudController {
    @FXML
    private ImageView posterImage;
    @FXML
    private Label titleLabel,directorLabel,editorLabel,yearLabel,formatLabel,shelfLabel,genreLabel;
    private IEntity entity;
    private MainController mainController;

    public void setEntity(IEntity entity) {
        this.entity = entity;
        loadEntity();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    // Méthode pour charger les infos d’un film
    public void loadEntity() {
        if (entity instanceof Entity e) {
            titleLabel.setText(e.getTitle());
            directorLabel.setText("Réalisateur : " + e.getDirector());
            editorLabel.setText("Éditeur : " + e.getEditor());
            yearLabel.setText("Année : " + e.getReleaseYear());
            formatLabel.setText("Format : " + e.formatProperty().getValue().name());
            shelfLabel.setText("Étagère : " + e.getShelfId());
            genreLabel.setText("Genre : " + e.getGenre());
            String imageUrl = e.getImageUrl();
            System.out.println(imageUrl);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Image img = new Image(imageUrl, false); // synchro
                if (img.isError()) {
                    posterImage.setImage(new Image(getClass().getResource("/project/ruffinary/img/No_Image_Available.jpg").toExternalForm()));

                } else posterImage.setImage(img);
            } else {
                Image img = new Image(getClass().getResource("/project/ruffinary/img/No_Image_Available.jpg").toExternalForm());
                posterImage.setImage(img);
            }
        }
    }

    public void updateEntity() {
        String oldShelfId = ((Entity) entity).getShelfId();
        Entity modified = Alert.showEntityForUpdate((Entity) this.entity, mainController.getEntityManager().getEntities());
        if (modified != null) {
            mainController.getEntityManager().update(((Entity) entity), modified);
            updateAttribute(((Entity) entity), modified);
            mainController.getEntityManager().getEntities().forEach(entity -> {
                if (entity instanceof Shelf sh) {
                    if (sh.getNom().equals(oldShelfId)) {
                        sh.removeChild(this.entity);
                    }
                    if (sh.getNom().equals(((Entity) this.entity).shelfIdProperty().getValue())) {
                        sh.addChild(this.entity);
                    }
                }
            });
            loadEntity();
        }
    }


    private void updateAttribute(Entity target, Entity source) {
        target.shelfIdProperty().set(source.getShelfId());
        target.releaseYearProperty().set(source.getReleaseYear());
        target.titleProperty().set(source.getTitle());
        target.directorProperty().set(source.getDirector());
        target.editorProperty().set(source.getEditor());
        target.formatIdProperty().set(source.getFormatId());
        target.genreProperty().set(source.getGenre());
    }

    public void deleteEntity() {
        if (Alert.confirmDelete()) {
            mainController.getEntityManager().getEntities().forEach(entity -> {
                if (entity instanceof Shelf sh) {
                    if (sh.getNom().equals(((Entity) this.entity).shelfIdProperty().getValue())) {
                        sh.removeChild(this.entity);
                    }
                }
            });
            mainController.getEntityManager().deleteEntity(entity);
            mainController.getEntityManager().getEntities().remove(entity);
        }
    }

}
        

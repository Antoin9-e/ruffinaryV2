package project.ruffinary.view;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import project.ruffinary.controller.MainController;
import project.ruffinary.modele.entite.Entity;
import project.ruffinary.modele.entite.Format;
import project.ruffinary.modele.entite.IEntity;
import project.ruffinary.modele.entite.Shelf;

import java.time.LocalDate;
import java.util.Optional;

public abstract class Alert {

    public static Pair<String, String> showLoginDialog() {
        // Création du dialogue
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Connexion SQL");
        dialog.setHeaderText("Veuillez entrer vos identifiants SQL");

        // Boutons OK / Annuler
        ButtonType loginButtonType = new ButtonType("Connexion", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Utilisateur SQL");

        PasswordField password = new PasswordField();
        password.setPromptText("Mot de passe");

        grid.add(new Label("Utilisateur:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);
        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        // Affichage et récupération
        Optional<Pair<String, String>> result = dialog.showAndWait();
        return result.orElse(null);
    }


    public static Shelf createShelf(FilteredList<IEntity> entityList){
        Dialog<Shelf> dialog = new Dialog<>();
        dialog.setTitle("Création d'une étagère");
        ButtonType confirm = new ButtonType("Creation", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField id = new TextField();
        id.setPromptText("Id");

        ChoiceBox<String> parent = new ChoiceBox<>();
        parent.getItems().add("-- Aucune --");
        for(IEntity e : entityList){
            if(e instanceof Shelf sh){
                String label = sh.getNom();
                parent.getItems().add(label);
            }
        }




        grid.add(new Label("Identifiant:"), 0, 0);
        grid.add(id, 1, 0);
        grid.add(new Label("Etagère parente:"), 0, 1);
        grid.add(parent, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirm) {
                if(parent.getValue().equals("-- Aucune --")){
                    return new Shelf(id.getText(),null);
                }else return new Shelf(id.getText(),parent.getValue().toString());
            }
            return null;
        });

        Optional<Shelf> result = dialog.showAndWait();
        return result.orElse(null);


    }


    public static Shelf removeShelf(FilteredList<IEntity> entityList) {
        Dialog<Shelf> dialog = new Dialog<>();
        dialog.setTitle("Suppression d'une étagère");
        ButtonType confirm = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);



        ChoiceBox<String> etagere = new ChoiceBox<>();
        etagere.getItems();
        for(IEntity e : entityList){
            if(e instanceof Shelf sh){
                String label = sh.getNom();
                etagere.getItems().add(label);
            }
        }




        grid.add(new Label("Identifiant:"), 0, 0);
        grid.add(etagere, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirm) {
                String value = etagere.getValue();
                if (value == null || value.isEmpty()) {
                    return null;
                }

                return entityList.stream()
                        .filter(e -> e instanceof Shelf)
                        .map(e -> (Shelf) e)
                        .filter(sh -> value.equals(sh.getNom()))
                        .findFirst()
                        .orElse(null);


            }
            return null;
        });

        Optional<Shelf> result = dialog.showAndWait();
        return result.orElse(null);


    }

    public static javafx.scene.control.Alert errorBC(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
    }

    public static javafx.scene.control.Alert warningFormat() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("Le format est vide ou non valide");
        return alert;
    }
    public static javafx.scene.control.Alert warningTitle() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("Le titre est vide !");
        return alert;
    }


    public static Entity confirmAdd(IEntity entity, ObservableList<IEntity> entityList) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation d'ajout");
        alert.setHeaderText("Veuillez confirmer les informations du film");

        // Création des champs
        TextField titreField = new TextField();
        TextField editeurField = new TextField();
        TextField directorField = new TextField();
        TextField anneeField = new TextField();
        TextField genreField = new TextField();
        ChoiceBox<String> formatField = MainController.getFormatChoicebox();
        ChoiceBox<String> shelfField = MainController.getShelfChoicebox(entityList);

        // Pré-remplissage si l'entité contient des données (optionnel)
        if (entity != null && entity instanceof Entity en) {
            titreField.setText(en.getTitle()); // Assure-toi que getTitre() existe
            editeurField.setText(en.getEditor()); // idem
            directorField.setText(en.getDirector()); // idem
            formatField.setValue(String.valueOf(Format.values()[en.getFormatId()]));
            anneeField.setText(en.getReleaseYear());
            genreField.setText(en.getGenre());
            shelfField.setValue("--Aucune--");

        }

        // Mise en page avec GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titreField, 1, 0);

        grid.add(new Label("Éditeur:"), 0, 1);
        grid.add(editeurField, 1, 1);

        grid.add(new Label("Réalisateur:"), 0, 2);
        grid.add(directorField, 1, 2);

        grid.add(new Label("Format:"), 0, 3);
        grid.add(formatField, 1, 3);

        grid.add(new Label("Annee:"), 0, 4);
        grid.add(anneeField, 1, 4);

        grid.add(new Label("Genre:"), 0, 5);
        grid.add(genreField, 1, 5);

        grid.add(new Label("Etagère:"), 0, 6);
        grid.add(shelfField, 1, 6);


        alert.getDialogPane().setContent(grid);

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            String titre = titreField.getText();
            String editeur = editeurField.getText();
            String director = directorField.getText();
            String format = formatField.getValue();
            String annee = anneeField.getText();
            String genre = genreField.getText();
            String etagere = shelfField.getValue();


            if(shelfField.getValue().equals("--Aucune--")){
                etagere = null;
            }


            return new Entity(Format.valueOf(format).ordinal(),etagere,((Entity)entity).getEan(),editeur,genre,director,annee,titre,((Entity)entity).getImageUrl());
        }

        return null;
    }


    public static Entity showEntityForUpdate(Entity entity, ObservableList<IEntity> entityList) {
        Dialog<Entity> dialog = new Dialog<>();
        dialog.setTitle("Modification d'une entité");
        ButtonType confirm = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        TextField titreField = new TextField();
        TextField editeurField = new TextField();
        TextField directorField = new TextField();
        TextField anneeField = new TextField();
        TextField genreField = new TextField();
        ChoiceBox<String> formatField = MainController.getFormatChoicebox();
        ChoiceBox<String> shelfField = MainController.getShelfChoicebox(entityList);

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titreField, 1, 0);
        grid.add(new Label("Editeur:"), 0, 1);
        grid.add(editeurField, 1, 1);
        grid.add(new Label("Director:"), 0, 2);
        grid.add(directorField, 1, 2);
        grid.add(new Label("Format:"), 0, 3);
        grid.add(formatField, 1, 3);
        grid.add(new Label("Annee:"), 0, 4);
        grid.add(anneeField, 1, 4);
        grid.add(new Label("Genre:"), 0, 5);
        grid.add(genreField, 1, 5);
        grid.add(new Label("Etagère:"), 0, 6);
        grid.add(shelfField, 1, 6);
        formatField.setValue(String.valueOf(Format.values()[entity.getFormatId()]));
        shelfField.setValue(entity.getShelfId());
        genreField.setText(entity.getGenre());
        titreField.setText(entity.getTitle());
        editeurField.setText(entity.getEditor());
        directorField.setText(entity.getDirector());
        anneeField.setText(entity.getReleaseYear());
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogbutton -> {
            if(dialogbutton == confirm){

                Entity b = new Entity(Format.valueOf(formatField.getValue()).ordinal(),shelfField.getValue(),entity.getEan(),editeurField.getText(),genreField.getText(),directorField.getText(),anneeField.getText(),titreField.getText(),entity.getImageUrl());
                return b;
            }else{
                return null;
            }
        });

        Optional<Entity> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static boolean confirmDelete(){
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Confirmation");
        dialog.setContentText("Confirmer la suppression de ce film?");
        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);
        dialog.setResultConverter(dialogbutton ->{
            if(dialogbutton == confirm)return true;
            else return false;
        });
        Optional<Boolean> result = dialog.showAndWait();

        return result.orElse(false);

    }

    public static void errorDatabase(String message){
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public static Optional<ButtonType> quit(){
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitter l'application");
        alert.setHeaderText("Êtes-vous sûr de vouloir quitter ?");

        return alert.showAndWait();
    }

}

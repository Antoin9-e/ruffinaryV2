package project.ruffinary.modele.entite;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import project.ruffinary.modele.exception.InvalidBarcodeException;

import java.security.InvalidParameterException;
import java.time.LocalDate;

public class Entity implements IEntity {
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty releaseYear = new SimpleStringProperty();
    private final StringProperty director = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();
    private final StringProperty dateAjout = new SimpleStringProperty();
    private final StringProperty editor = new SimpleStringProperty();
    private final StringProperty shelfId = new SimpleStringProperty();
    private final StringProperty ean = new SimpleStringProperty();
    private final IntegerProperty formatId = new SimpleIntegerProperty();
    private final StringProperty imageUrl = new SimpleStringProperty();

    public Entity(int format_id, String shelf_id, String ean, String editor,
                  String genre, String director, String releaseYear, String title, String image_url) {
        if(ean == null || ean.length() <12){
            throw new InvalidBarcodeException("Le code barre ne peut être nul et/ou doit contenir au moins 12 caractères! (UPC-A ou EAN-13) ");
        }


        this.formatId.set(format_id);
        this.shelfId.set(shelf_id);
        this.ean.set(ean);
        this.editor.set(editor);
        this.dateAjout.set(LocalDate.now().toString());
        this.genre.set(genre);
        this.director.set(director);
        this.releaseYear.set(releaseYear);
        this.title.set(title);
        this.imageUrl.set(image_url);
    }

    // Getters
    public String getTitle() { return title.get(); }
    public StringProperty titleProperty() { return title; }

    public String getShelfId() { return shelfId.get(); }
    public StringProperty shelfIdProperty() { return shelfId; }

    public ObjectBinding<Format> formatProperty() {
        return Bindings.createObjectBinding(
                () -> Format.values()[formatId.get()],
                formatId
        );
    }
    public int getFormatId() { return formatId.get(); }
    public IntegerProperty formatIdProperty() { return formatId; }

    public String getEan() { return ean.get(); }
    public StringProperty eanProperty() { return ean; }

    public String getEditor() { return editor.get(); }
    public StringProperty editorProperty() { return editor; }

    public String getDateAjout() { return dateAjout.get(); }
    public StringProperty dateAjoutProperty() { return dateAjout; }

    public String getGenre() { return genre.get(); }
    public StringProperty genreProperty() { return genre; }

    public String getDirector() { return director.get(); }
    public StringProperty directorProperty() { return director; }

    public String getReleaseYear() { return releaseYear.get(); }
    public StringProperty releaseYearProperty() { return releaseYear; }

    public String getImageUrl() { return imageUrl.get(); }
    public StringProperty imageUrlProperty() { return imageUrl; }

    // Setters
    public void setShelfId(String shelf_id) { this.shelfId.set(shelf_id); }

    @Override
    public void afficher() {
        System.out.println("Le film " + getTitle() + " de format: " + getFormatId() +
                " dateajout " + getDateAjout() + " releaseYear " + getReleaseYear() +
                " code: " + getEan());
    }
}
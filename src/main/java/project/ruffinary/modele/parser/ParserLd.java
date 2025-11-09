package project.ruffinary.modele.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import project.ruffinary.modele.entite.Entity;
import project.ruffinary.modele.entite.IEntity;

import java.time.LocalDate;

public class ParserLd implements Parser {

    private Document doc;
    private String code;
    @Override
    public IEntity parse(String json) {
        this.doc = Jsoup.parse(json);
        return new Entity(2,null,code,getEditor(doc),null,null,getReleasedYear(doc),getTitle(doc),getUrl(doc));
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle(Document doc)   {
        // Récupérer le titre du LaserDisc
        String title = doc.select("h2.lddb").text();
        System.out.println("Titre : " + title);
        int startIndex = title.indexOf("(");
        title = title.substring(0, startIndex).trim();
        return title;
    }

    public String getLaserDiscCountry(Document doc)   {
        // Extraire le pays
        String country = doc.select("td.field:contains(Country) + td.data").text();
        System.out.println("Pays : " + country);
        return country;
    }

    public String getLaserDiscPrice(Document doc)   {
        // Extraire le prix
        String price = doc.select("td.field:contains(Price) + td.data").text();
        System.out.println("Prix : " + price);
        return price;
    }



    public String getEditor(Document doc)  {
        // Extraire l'éditeur
        String publisher = doc.select("td.field:contains(Publisher) + td.data a").text();
        System.out.println("Éditeur : " + publisher);
        return publisher;
    }

    public String getReleasedYear(Document doc) {

        String title = doc.select("h2.lddb").text();
        int startIndex = title.indexOf("(");
        int endIndex = title.indexOf(")");
        String released = title.substring(startIndex + 1, endIndex);
        System.out.println("Date de sortie : " + released);
        return released;
    }

    public String getUrl(Document doc) {
        String imageUrl = "";
        Element flipbox = doc.getElementById("flipbox");


        Element img = flipbox.selectFirst("a img");
        if (img != null) {
             imageUrl = img.attr("src");
            System.out.println("Image dans flipbox : " + imageUrl);
        } else {
            imageUrl = null;
            System.out.println("Aucune image trouvée dans flipbox.");
        }

        return imageUrl;

    }

   }

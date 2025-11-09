package project.ruffinary.modele.parser;


import project.ruffinary.modele.entite.Entity;
import project.ruffinary.modele.entite.Format;
import project.ruffinary.modele.entite.IEntity;

import java.time.LocalDate;

public class ParserDvd implements Parser {

    private String code;
    @Override
    public IEntity parse(String json) {

        return new Entity(getFormat(json),null,code,getEditor(json),null,getDirector(json),getAnnee(json),getTitle(json),getUrl(json));
    }

    public Integer getFormat(String json) {
        String format;

        if (json.contains("<media>")) {
            String[] parts = json.split("<media>");
            format = parts[1].split("</media>")[0];
        } else {
            format = "DVD";
        }

        System.out.println("Format part: " + format);

        if (format.equalsIgnoreCase("Unknown")) {
            return null;
        }

        Format[] formats = Format.values();
        if(format.equalsIgnoreCase("HD-DVD")){
            return Format.HDDVD.ordinal();
        }
        if(format.equalsIgnoreCase("BRD")){
            return Format.BR.ordinal();
        }
        for (int i = 0; i < formats.length; i++) {
            if (formats[i].name().equalsIgnoreCase(format)) {
                return i;
            }

        }

        return null; // Si aucun format ne correspond
    }

    public String getDirector(String json){
        String director = "";
        if (json.contains("star type=")) {
            String [] parts = json.split("<star type=\"Réalisateur\".*?>");

            director = parts[1].split("</star>")[0];

        }else {
            director = "Unknown";
        }

        System.out.println("Director part: " + director);

        return director;
    }

    public String getAnnee(String json){
        String annee = "";
        int a = 0;
        if (json.contains("<annee>")) {
            String [] parts = json.split("<annee>");

            annee = parts[1].split("</annee>")[0];

        }

        System.out.println("Annee part: " + annee);

        return annee;

    }

    public String getTitle(String json){
        String title = "";
        if (json.contains("<titres> <fr>")) {
            String [] parts = json.split("<titres> <fr>");

            title = parts[1].split("</fr>")[0];
            if (title.contains("&#039;")){
                title = title.replace("&#039;", "'");
            }
        }else{
            title = "Unknown";
        }

        System.out.println("Title part: " + title);


        return title;
    }

    public String getEditor(String json){
        String editor = "";
        if (json.contains("editeur")) {
            String [] parts = json.split("<editeur>");
            editor = parts[1].split("</editeur>")[0];
            if (editor.contains("&#039;")){
                editor = editor.replace("&#039;", "'");
            }


        }else{
            editor = "Unknown";
        }

        System.out.println("Editor part: " + editor);


        return editor;
    }

    public String getUrl(String json){
        String url = "";
        if (json.contains("<cover>")) {
            String [] parts = json.split("<cover>");
            url = parts[1].split("</cover>")[0];

        }else{
            url = null;
        }
        System.out.println("Url part: " + url);
        return url;
    }



    @Override
    public void setCode(String code) {
        this.code = code;

    }
}

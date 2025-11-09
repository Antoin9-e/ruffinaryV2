package project.ruffinary.modele.api;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import project.ruffinary.modele.parser.Parser;

public class ApiLd implements IApi{

    private Parser parser;

    public ApiLd(Parser parser) {
        this.parser = parser;
    }
    @Override
    public String search(String input) {

            try {

                parser.setCode(input);
                // URL de la page de redirection
                String url = "https://www.lddb.com/search.php?search=" + input;
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(10_000)
                        .followRedirects(true) // Permet de suivre les redirections
                        .get();

                // Afficher tout le contenu HTML de la page redirigée
                String pageHtml = doc.html();  // Récupère tout le HTML de la page après redirection
                Elements tables = doc.select("tr.contents_0");

                String newUrl = "";
                for (Element table : tables) {
                    newUrl = table.select("td a").attr("href");
                }

                // Afficher le contenu de la page redirigée
                Document newDoc = Jsoup.connect(newUrl)
                        .userAgent("Mozilla/5.0")
                        .timeout(10_000)
                        .get();
                // Récupérer une table et extraire des données spécifiques (ex : format vidéo)
                return newDoc.toString();
            }catch (Exception e){
                e.printStackTrace();

            }

            return null;

    }

    @Override
    public Parser getParser() {
        return parser;
    }
}

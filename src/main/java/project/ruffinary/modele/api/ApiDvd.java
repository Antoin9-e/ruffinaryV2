package project.ruffinary.modele.api;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.ruffinary.modele.parser.Parser;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ApiDvd implements IApi {

    private Parser parser;

    public ApiDvd(Parser parser){
        this.parser = parser;
    }
    @Override
    public String search(String input) {
        parser.setCode(input);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        // Encodage du paramètre pour éviter les erreurs d'URL
        String encodedInput = URLEncoder.encode(input, StandardCharsets.UTF_8);
        String url = "http://www.dvdfr.com/api/search.php?gencode=" + encodedInput;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String body = response.body().string();
                System.out.println("Réponse de l'API :\n" + body);
                return body;
            } else {
                System.err.println("Erreur HTTP : " + response.code());
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Timeout de la requête : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur réseau : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Parser getParser() {
        return parser;
    }
}

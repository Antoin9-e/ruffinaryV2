package project.ruffinary.modele.api;


import project.ruffinary.modele.entite.Format;
import project.ruffinary.modele.parser.Parser;
import project.ruffinary.modele.parser.ParserDvd;
import project.ruffinary.modele.parser.ParserLd;

public interface IApi {
    String search(String input);
    Parser getParser();
    static IApi getApi(Format format) {
        switch (format) {
            case LD -> {
                return  new ApiLd(new ParserLd());
            }
            default -> {
                return  new ApiDvd(new ParserDvd());
            }
        }
    };
}

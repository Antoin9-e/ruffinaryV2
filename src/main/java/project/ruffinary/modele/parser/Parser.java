package project.ruffinary.modele.parser;


import project.ruffinary.modele.entite.IEntity;

public interface Parser {
    IEntity parse(String json);
    void setCode(String code);
}

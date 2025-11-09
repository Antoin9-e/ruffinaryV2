package project.ruffinary.modele.entite;

import java.util.ArrayList;
import java.util.List;

public class Shelf implements IEntity {
    private List<IEntity> childs;
    private String nom;
    private String parent_id;

    public Shelf(String nom, String parent_id) {
        this.parent_id = parent_id;
        this.nom = nom;
        childs = new ArrayList<IEntity>();
    }

    public String getNom() {
        return nom;
    }

    public String getParent_id() {
        return parent_id;
    }
    public void addChild(IEntity child){
        childs.add(child);
    }

    public List<IEntity> getChilds(){
        return childs;
    }

    public List<IEntity> getShelfChilds(){
        List<IEntity> sc = new ArrayList<>();
        for(IEntity child : childs){
            if(child instanceof Shelf){
                sc.add(child);
            }
        }
        return sc;
    }

    public List<IEntity> getEntityChilds(){
        List<IEntity> ec = new ArrayList<>();
        for(IEntity child : childs){
            if(child instanceof Entity){
                ec.add(child);
            }
        }
        return ec;
    }

    public void removeChild(IEntity child){
        childs.remove(child);
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public void afficher() {
        System.out.println("étagere appartenant a "+parent_id);
        System.out.println("----Haut----");
        System.out.println("Etagere: "+this.nom);
        childs.forEach(IEntity::afficher);
        System.out.println("----Bas----");
    }
}

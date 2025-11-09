package project.ruffinary.modele;

import com.google.protobuf.Api;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import project.ruffinary.modele.api.IApi;
import project.ruffinary.modele.db.Database;
import project.ruffinary.modele.entite.Entity;
import project.ruffinary.modele.entite.Format;
import project.ruffinary.modele.entite.IEntity;
import project.ruffinary.modele.entite.Shelf;
import project.ruffinary.view.Alert;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class EntityManager {
    private final Database database = new Database();
    private ObservableList<IEntity> entities = FXCollections.observableArrayList();
    private IApi api;
    private static EntityManager instance;


    private EntityManager() {

    }



    public static EntityManager getInstance() {
        if (instance == null)
        {
            instance = new EntityManager();
        }
        return instance;
    }

    public ObservableList<IEntity> getEntities() {
        return entities;
    }

    public boolean setDatabase(){
       Pair<String,String> logs =  Alert.showLoginDialog();
       if(logs != null) {
           database.setUsername(logs.getKey());
           database.setPassword(logs.getValue());
           if (!database.connect()) setDatabase();
           return true;
       }else return false;
    }

    public void loadEntitiesFromDB() {
        if (database.getConnection() == null) {
            database.connect();
        }
        String sql = database.getQueries().getLoadShelfQ();

        try (Statement stmt = database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Shelf shelf = new Shelf(
                        rs.getString("shelf_id"),
                        rs.getString("parent_id"));
                entities.add(shelf);
                for (IEntity entity : entities) {
                    if (entity instanceof Shelf parent && parent.getNom().equals(shelf.getParent_id())) {
                        parent.addChild(shelf);
                    }
                }
            }

        } catch (SQLException e) {
            Alert.errorDatabase(e.getMessage());
        }
        sql = database.getQueries().getLoadIEntityQ();
        try
        {
            Statement stmt = database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Entity entity = new Entity(rs.getInt("format_id"), rs.getString("shelf_id"), rs.getString("code"), rs.getString("editor"), rs.getString("genre"), rs.getString("director"), rs.getString("release_year"), rs.getString("title"), rs.getString("image_url"));
                System.out.println(rs.getString("image_url"));
                entities.add(entity);
                for (IEntity e : entities) {
                    if (e instanceof Shelf parent && parent.getNom().equals(entity.getShelfId())) {
                        parent.addChild(entity);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            Alert.errorDatabase(e.getMessage());
        }
        database.disconnect();
    }

    public void addShelf(IEntity shelf) {
        if (database.getConnection() == null) {
            database.connect();
        }
        String sql = database.getQueries().getAddScQ();
        if (shelf instanceof Shelf sh) {
            try
            {
                PreparedStatement stmt = database.getConnection().prepareStatement(sql);
                stmt.setString(1, sh.getNom());
                if (sh.getParent_id() == null) {
                    stmt.setNull(2, Types.VARCHAR);
                } else {
                    stmt.setString(2, sh.getParent_id());
                    for (IEntity e : entities) {
                        if (e instanceof Shelf sc) {
                            if (sc.getNom().equals(sh.getParent_id())) sc.addChild(shelf);
                        }
                    }
                }
                int ligne = stmt.executeUpdate();
                System.out.println(ligne + " lignes affectées");
                entities.add(shelf);
            }
            catch (SQLException e)
            {
                Alert.errorDatabase(e.getMessage());
            }
        }
        database.disconnect();
    }

    public void deleteShelf(IEntity shelf) {
        if (database.getConnection() == null) {
            database.connect();
        }
        String sql = database.getQueries().getDeleteScQ();
        try
        {
            PreparedStatement pstmt = database.getConnection().prepareStatement(sql);
            pstmt.setString(1, ((Shelf) shelf).getNom());
            int ligne = pstmt.executeUpdate();
            System.out.println(ligne + " lignes affectées");
            entities.remove(shelf);
        }
        catch (SQLException e)
        {
            Alert.errorDatabase(e.getMessage());
        }
        database.disconnect();
    }

    public void setApi(Format fmt) {
        this.api = fmt.name().equals("LD") ? IApi.getApi(Format.LD) : IApi.getApi(Format.DVD);
    }

    public IApi getApi() {
        return api;
    }


    public void addEntity(Entity conf) {
        if (database.getConnection() == null) {
            database.connect();
        }
        String sql = database.getQueries().getAddEnQ();
        //INSERT INTO ENTITY(code,title,director,editor,release_year,format_id,genre,shelf_id,url_image) VALUES (?,?,?,?,?,?,?,?,?)
        try
        {
            PreparedStatement pstmt = database.getConnection().prepareStatement(sql);
            pstmt.setString(1, conf.getEan());
            pstmt.setString(2, conf.getTitle());
            pstmt.setString(3, conf.getDirector());
            pstmt.setString(4, conf.getEditor());
            pstmt.setString(5, conf.getReleaseYear());
            pstmt.setInt(6, conf.getFormatId());
            pstmt.setString(7, conf.getGenre());
            pstmt.setString(8, conf.getShelfId());
            pstmt.setString(9, conf.getImageUrl());
            int ligne = pstmt.executeUpdate();
            System.out.println(ligne + "inseré");
        }
        catch (SQLException e)
        {
            Alert.errorDatabase(e.getMessage());
        }
        database.disconnect();
    }

    public String getMaxCodeFromDb(){
        if(database.getConnection() == null) {
            database.connect();
        }
        String sql = database.getQueries().getMaxCodeQ();
        try
        {
            Statement stmt = database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String resp = "";
            while(rs.next()){
                resp = rs.getString("max");
            }
            System.out.println(resp);
            return resp;
        }
        catch(SQLException e)
        {
            Alert.errorDatabase(e.getMessage());
        }
        database.disconnect();
        return null;
    }

    public void update(Entity initial, Entity modifie) {
        if (database.getConnection() == null) {
            database.connect();
        }
        String sql = database.getQueries().getUpdateQ();
        //UPDATE ENTITY SET title = ? ,director = ?, editor = ? , release_year = ?, genre = ?, shelf_id = ?, format_id=? where code = ? "
        try
        {
            PreparedStatement pstmt = database.getConnection().prepareStatement(sql);
            pstmt.setString(1, modifie.getTitle());
            pstmt.setString(2, modifie.getDirector());
            pstmt.setString(3, modifie.getEditor());
            pstmt.setString(4, modifie.getReleaseYear());
            pstmt.setString(5, modifie.getGenre());
            pstmt.setString(6, modifie.getShelfId());
            pstmt.setInt(7, modifie.getFormatId());
            pstmt.setString(8, initial.getEan());
            int ligne = pstmt.executeUpdate();
            System.out.println(ligne + " affectés");

        }
        catch (SQLException e)
        {
            Alert.errorDatabase(e.getMessage());
        }
        database.disconnect();
    }

    public void deleteEntity(IEntity entity) {
        if (database.getConnection() == null) {
            database.connect();
        }
        String sql = database.getQueries().getDeleteQ();
        //DELETE FROM ENTITY WHERE code=?
        try
        {
         PreparedStatement pstmt = database.getConnection().prepareStatement(sql);
         pstmt.setString(1,((Entity) entity).getEan());
         int ligne = pstmt.executeUpdate();
            System.out.println(ligne  + " supprimés");
        }
        catch (SQLException e)
        {
            Alert.errorDatabase(e.getMessage());
        }
        database.disconnect();
    }


    public void exportScript(String filepath) {
        if(database.getConnection() == null) {
            database.connect();
        }
        String sql =  database.getQueries().getShelfWithMetaR();
        try
        {
            Statement stmt = database.getConnection().createStatement();
            ResultSet rs =  stmt.executeQuery(sql);
            BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
            while(rs.next()){
                System.out.println(rs.getString(1));
                bw.write(rs.getString(1));
                bw.newLine();
            }

            sql = database.getQueries().getMetaRequete();
            rs =  stmt.executeQuery(sql);
            while(rs.next()){
                bw.write(rs.getString(1));
                bw.newLine();
            }

            rs.close();

            bw.close();

        }
        catch (SQLException  | IOException e)
        {
            Alert.errorDatabase(e.getMessage());
        }
        database.disconnect();
    }

    public void exportCsv(String filepath){
        if(database.getConnection() == null) {
            database.connect();
        }
        String sql =  database.getQueries().getLoadIEntityQ();
        try
        {
            Statement stmt = database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
            ResultSetMetaData rmd = rs.getMetaData();
            int nbCol = rmd.getColumnCount();
            for(int i=1;i<=nbCol;i++){
                System.out.println(rmd.getColumnName(i));
                bw.write(rmd.getColumnName(i));
                if (i < nbCol) bw.write(",");
            }
            bw.newLine();
            while (rs.next()) {
                for (int i = 1; i <= nbCol; i++) {
                    String value = rs.getString(i);
                    System.out.println(value);
                    if (value != null) {
                        value = value.replace("\"", "\"\"");
                    }
                    bw.write("\"" + value + "\"");
                    if (i < nbCol) bw.write(",");
                }
                bw.newLine();
            }
            rs.close();
            bw.close();
        }

        catch (SQLException  | IOException e)
        {
            Alert.errorDatabase(e.getMessage());
        }
        database.disconnect();
    }
}


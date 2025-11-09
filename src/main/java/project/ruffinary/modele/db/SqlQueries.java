package project.ruffinary.modele.db;

public class SqlQueries {


    public String getUpdateQ(){
        return "UPDATE ENTITY SET title = ? ,director = ?, editor = ? , release_year = ?, genre = ?, shelf_id = ?, format_id=? where code = ? ";
    }



    public String getAddEnQ() {
        return "INSERT INTO ENTITY(code,title,director,editor,release_year,format_id,genre,shelf_id,image_url) VALUES (?,?,?,?,?,?,?,?,?)";
    }

    public String getDeleteScQ(){
        return "DELETE FROM shelf WHERE shelf_id=?";
    }

    public String getMaxCodeQ() {
        return "SELECT CAST(MAX(CAST(code AS UNSIGNED)) + 1 AS CHAR) AS max FROM entity";
    }

    public String getAddScQ(){
        return "INSERT INTO shelf(shelf_id,parent_id) VALUES (?,?)";
    }

    public String getLoadShelfQ(){
        return "SELECT  *  FROM shelf";
    }

    public String getModifyQ() {
        return "UPDATE ENTITY SET title = ?, director = ?,editor = ?, release_year = ?, genre = ?,  shelf_id = ?, url = ? WHERE code = ?";
    }

    public String getDeleteQ() {
        return "DELETE FROM ENTITY WHERE code=?";
    }

    public String getLoadIEntityQ() {
        return "SELECT * FROM ENTITY ";
    }

    public String getMetaRequete() {
        return "SELECT \n" +
                "  CONCAT(\n" +
                "    'INSERT INTO entity (code, title, director, editor, release_year, format_id, date_ajout, genre, shelf_id, image_url) VALUES (',\n" +
                "    QUOTE(code), ', ',\n" +
                "    QUOTE(title), ', ',\n" +
                "    QUOTE(director), ', ',\n" +
                "    QUOTE(editor), ', ',\n" +
                "    IFNULL(release_year, 'NULL'), ', ',\n" +
                "    IFNULL(format_id, 'NULL'), ', ',\n" +
                "    QUOTE(date_ajout), ', ',\n" +
                "    QUOTE(genre), ', ',\n" +
                "    QUOTE(shelf_id), ', ',\n" +
                "    QUOTE(image_url),\n" +
                "    ');'\n" +
                "  ) AS insert_statement\n" +
                "FROM entity";
    }

    public String getShelfWithMetaR(){
        return "select CONCAT('INSERT INTO shelf(shelf_id,parent_id) VALUES (',\n" +
                "quote(shelf_id),' ,',\n" +
                "quote(parent_id),');') \n" +
                "as insert_statement\n" +
                "from shelf";
    }
}

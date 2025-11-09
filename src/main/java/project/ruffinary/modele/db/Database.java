package project.ruffinary.modele.db;

import java.sql.*;

public class Database {
    private String  host = "localhost";
    private String  port = "3306";
    private String  username ; //="Antoine" ;
    private String  password; // ="J9ueve-14540";
    private String  database = "ruffinary_db";

    private Connection con;
    private SqlQueries queries;

    public Database() {
        queries = new SqlQueries();
    }








    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() {
        return con;
    }

    public SqlQueries getQueries() {
        return queries;
    }



    public boolean connect() {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        try{
            this.con = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database.");
            return true;

        }catch(SQLException e){
            System.out.println("Error connecting to database."+e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        if(this.con != null){
            try {
                con.close();
                System.out.println("Disconnected from database.");
            }catch(SQLException e){
                System.out.println("Error disconnecting from database.");
            }

            con = null;

        }
    }







}

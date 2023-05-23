package com.example.progettomap.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {

    private static String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private final String DBMS = "jdbc:mysql";
    private String server = "localhost";
    private String database = "MapDB";
    private final int PORT = 3306;
    private String user_id = "MapUser";
    private String password = "map";

    private Connection conn;

    public DbAccess() {

    }

    public DbAccess(String server, String database, String userId, String password) {
        this.server = server;
        this.database = database;
        this.user_id = userId;
        this.password = password;
    }

    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME).newInstance();
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException("[!] Driver non trovato: " + e.getMessage());
        } catch (InstantiationException e) {
            throw new DatabaseConnectionException("[!] Errore durante l'istanziazione: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new DatabaseConnectionException("[!] Impossibile accedere al driver: " + e.getMessage());
        }
        String connectionString = DBMS + "://" + server + ":" + PORT + "/" + database
                + "?user=" + user_id + "&password=" + password + "&serverTimezone=UTC";
        System.out.println("Connection's String: " + connectionString);
        try {
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "[!] SQLException: " + e.getMessage() + "\n"
                            + "[!] SQLState: " + e.getSQLState() + "\n"
                            + "[!] VendorError: " + e.getErrorCode()
            );
        }
    }

    Connection getConnection() {
        return conn;
    }

    public void closeConnection() throws DatabaseConnectionException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("[!] Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }

  /*public void exampleQuery() {
    try {
      Statement s = conn.createStatement();

      // codice SQL: può generare l’eccezione SQLException

      ResultSet r = s.executeQuery(
          "SELECT FIRST, LAST, EMAIL " +
              "FROM peoplecsv as people " +
              "WHERE " +
              "(LAST='map') " +
              " AND (EMAIL Is Not Null) " +
              "ORDER BY FIRST");

      while (r.next()) {
        // Capitalization doesn't matter:
        System.out.println(
            r.getString("Last") + ", "
                + r.getString("fIRST")
                + ": " + r.getString("EMAIL"));
        // In alternativa: accesso posizionale
				System.out.println(
				r.getString(2) + ", "
				+ r.getString(1)
				+ ": " + r.getString(3) );
							NB: nell’accesso posizionale, gli indici delle colonne
				del ResultSet partono da 1
      }
      r.close();
      s.close(); // Also closes ResultSet

    } catch (SQLException ex) {
      // handle any errors
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }
  }*/

}

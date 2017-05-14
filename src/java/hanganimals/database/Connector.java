package hanganimals.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {
    
    private final String HOST     = "185.121.172.101";
    private final int    PORT     = 3306;
    private final String DATABASE = "zhgmzrgi_hanganimals";
    private final String USERNAME = "zhgmzrgi_REST";
    private final String PASSWORD = "xcv123REST";
    
    private static Connection connection;
    
    private static Connector connector;
    public static Connector getInstance() {
        if (connector == null) {
            connector = new Connector();
        }
        return connector;
    }
    
    private Connector() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    static public Connection getConnection(){
        return connection;
    }
    
    public ResultSet query(String query) throws SQLException {
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(query);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public void update(String query) throws SQLException {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
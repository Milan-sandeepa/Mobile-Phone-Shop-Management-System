package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    //rule 1
    private static DBConnection dbConnection;
    private Connection connection;

    //rule 2
    private DBConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/finetell","root","1234");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    //rule 3
    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        return (dbConnection==null)? dbConnection=new DBConnection():dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
}

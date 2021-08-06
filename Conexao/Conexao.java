package Conexao;

import java.sql.*;

public final class Conexao{
    public static Connection getConnection() throws Exception{
        
        Class.forName("com.mysql.jdbc.Driver");
        
        Connection c = DriverManager.getConnection("jdbc:mysql://192.168.0.30:3306/correio","root","");
        
        //Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/correio", "root", "");
        
        return c;
    }
}
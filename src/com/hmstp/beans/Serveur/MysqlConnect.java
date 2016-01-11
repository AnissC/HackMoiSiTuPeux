package com.hmstp.beans.Serveur;

import com.mysql.jdbc.Connection;
import java.sql.*;
import java.sql.DriverManager;


public final class MysqlConnect {
    public Connection conn;
    private Statement statement;
    public static MysqlConnect db;

    private MysqlConnect() {
        String url= "jdbc:mysql://localhost:3306/hmstp";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "root";
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection)DriverManager.getConnection(url,userName,password);
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }
    public static MysqlConnect getDbCon() {
        if ( db == null ) {
            db = new MysqlConnect();
        }
        return db;
    }
    public ResultSet query(String query) throws SQLException{
        statement = db.conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }
    public int insert(String insertQuery) throws SQLException {
        statement = db.conn.createStatement();
        int result = statement.executeUpdate(insertQuery);
        return result;
    }

    public static PreparedStatement initialisationRequetePreparee(java.sql.Connection connexion, String sql, boolean returnGeneratedKeys, Object... objets ) throws SQLException
    {
        PreparedStatement preparedStatement = connexion.prepareStatement(sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

        for (int i = 0; i < objets.length; i++) {
            preparedStatement.setObject(i + 1, objets[i]);
        }

        return preparedStatement;
    }

}
package com.mycompany.wannyanfin.dao;

import com.mycompany.wannyanfin.model.Dogcat;
import java.sql.*;
import java.util.*;

public class DogcatDao {
    
    static String url = "jdbc:mysql://wannyandb.cmpmacg0ssmv.us-east-1.rds.amazonaws.com:3306/wannyandb";
    static String user = "root";
    static String pass = "yCluphsmpsnP3kjiYSeL";

    public static Connection getConnection() throws SQLException {
        
        return DriverManager.getConnection(url, user, pass);
    }

    public static List<Dogcat> getAllDogcats() throws SQLException {
        List<Dogcat> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM wannyan_data");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Dogcat m = new Dogcat();
            m.setWannyan_id(rs.getInt("wannyan_id"));
            m.setUserId(rs.getInt("user_id")); 
            m.setDogcat(rs.getString("dogcat"));
            m.setName(rs.getString("name"));
            m.setText(rs.getString("text"));
            list.add(m);
        }
        con.close();
        return list;
    }
    
        public static List<Dogcat> getLoginDogcat(int userId) throws SQLException {
        List<Dogcat> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM wannyan_data Where user_id = ? ");
        ps.setInt(1,userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Dogcat m = new Dogcat();
            m.setWannyan_id(rs.getInt("wannyan_id"));
            m.setUserId(rs.getInt("user_id")); 
            m.setDogcat(rs.getString("dogcat"));
            m.setName(rs.getString("name"));
            m.setText(rs.getString("text"));
            list.add(m);
        }
        con.close();
        return list;
    }

    public static void addDogcat(Dogcat m) throws SQLException {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO wannyan_data(user_id,dogcat,name,text) VALUES(?,?,?,?)");
        ps.setInt(1, m.getUserId());
        ps.setString(2, m.getDogcat());
        ps.setString(3, m.getName());
        ps.setString(4, m.getText());
        ps.executeUpdate();
        con.close();
    }
    
    public static void deleteDogcat(int wannyanId) throws SQLException {
        // 【★ ここで wannyanId の値を出力する ★】
        System.out.println("--- 削除処理開始 ---");
        System.out.println("削除対象の wannyan_id: " + wannyanId);
        // try-with-resources を使用し、ConnectionとPreparedStatementを自動的に閉じます
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM wannyan_data WHERE wannyan_id = ?")) {

            // 1番目のプレースホルダー（?）に Dogcatオブジェクトから取得したIDをセット
            ps.setInt(1, wannyanId);

            // SQLを実行し、レコードを削除
            int executeUpdate = ps.executeUpdate();
            // 削除された行数を確認するデバッグログなどをここに追加できます
            //System.out.println("Deleted " + rowsAffected + " rows.");

        }
        // try-with-resources により、con.close() と ps.close() は自動的に実行されます
    }
}

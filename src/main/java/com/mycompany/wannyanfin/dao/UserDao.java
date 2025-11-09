package com.mycompany.wannyanfin.dao;

import com.mycompany.wannyanfin.model.UserAuthResult;
import java.sql.*;

public class UserDao {
    static String url = "jdbc:mysql://wannyandb.cmpmacg0ssmv.us-east-1.rds.amazonaws.com:3306/wannyandb";
    static String user = "root";
    static String pass = "yCluphsmpsnP3kjiYSeL";

    public static UserAuthResult validate(String username, String password_hash) {
        UserAuthResult result = null; // 判定が否だった場合はnullを返す
        
        final String USERID_COLUMN = "user_id"; //sqlデータベースのuser_idの列名指定
        final String USERNAME_COLUMN = "username"; //sqlデータベースのusernameの列名指定        
        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(
                     "SELECT " + USERID_COLUMN + "," + USERNAME_COLUMN + " FROM users WHERE username=? AND password_hash=?")) {
            ps.setString(1, username);
            ps.setString(2, password_hash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                //usernameとpassword_hashが合致した列のuser_idの値を取得
                String userId = rs.getString(USERID_COLUMN);
                String dbUserName = rs.getString(USERNAME_COLUMN);
                
                result = new UserAuthResult(userId,dbUserName);
            }
        } catch (SQLException e) {
            // ★★★ データベース接続/認証失敗時の詳細ログ出力 ★★★
            System.err.println("FATAL: データベース接続または認証中に SQLException が発生しました。");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            
            // 接続情報の問題の場合、特にメッセージを確認する
            System.err.println("Error Message: " + e.getMessage());
            
            e.printStackTrace(); // スタックトレースを出力
            // 接続エラーが起きた場合も、result は null のまま戻る
            
        } catch (Exception e) {
            // SQLException 以外の予期せぬ例外をキャッチ
            System.err.println("FATAL: 予期せぬ例外が発生しました。");
            e.printStackTrace();
        }
        return result;
    }
}


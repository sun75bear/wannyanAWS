package com.mycompany.wannyanfin.sdrun;

import java.io.*;
import java.sql.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import com.google.gson.Gson;

@WebServlet(name = "PetDataServlet", urlPatterns = {"/PetDataServlet"})
public class PetDataServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // --- AWS上でmySQLjdbcドライバを確実にロードするためのおまじない ---
        try {
            // MySQL Connector/J 8.0+ のドライバクラスを明示的にロード
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            // ドライバが見つからない場合、ログに記録
            getServletContext().log("CRITICAL: MySQL Driver (com.mysql.cj.jdbc.Driver) not found in Classpath.", e);
        }
        // ------------------
        List<Map<String, Object>> pets = new ArrayList<>();
        // ★★★ RDS接続情報に修正 ★★★
        // 1. エンドポイントを RDS のものに置き換え
        String url = "jdbc:mysql://wannyandb.cmpmacg0ssmv.us-east-1.rds.amazonaws.com:3306/wannyandb";
        // 2. マスターユーザー名に置き換え
        String user = "root";
        // 3. RDSの新しいマスターパスワードに置き換え
        String pass = "yCluphsmpsnP3kjiYSeL";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, dogcat, text FROM wannyan_data")) {

            // ★★★ 例外が発生する可能性がある処理をブロックで囲む ★★★
            while (rs.next()) {
                try {
                    Map<String, Object> pet = new HashMap<>();
                    pet.put("name", rs.getString("name"));
                    pet.put("text", rs.getString("text"));
                    pet.put("dogcat", rs.getString("dogcat"));
                    pets.add(pet);
                } catch (Exception innerE) {
                    // ループ内で例外が発生した場合、詳細をコンソールに出力
                    System.err.println("データ読み取り中に例外が発生しました: " + innerE.getMessage());
                    innerE.printStackTrace();
                }
            }

            //データ処理に成功したらデータをJSONで返す
            String json = new Gson().toJson(pets);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(json);
        
        } catch (SQLException e) {
                    // ★★★ DB接続またはクエリ全体で例外が発生した場合の処理を、データ処理の後ろに設定 ★★★
                    // 1. サーバーログに出力
                    System.err.println("DB接続またはクエリ実行中に例外が発生しました: " + e.getMessage());
                    // サーブレットログにも詳細を出力
                    getServletContext().log("SQLException発生", e);

                    // 2. 例外処理を受けての処理だから、このブロックが動けばクライアントにはエラー応答を返すように変更
                    // HTTPステータスコードを500に設定
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 

                    // エラー詳細をJSON形式で作成
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("status", "error");
                    errorResponse.put("message", "Database access failed.");
                    // デバッグのため、発生したSQLExceptionのメッセージをクライアントに返す
                    // 本番環境ではセキュリティのため、汎用的なメッセージに置き換えることを推奨
                    errorResponse.put("details", e.getMessage()); 

                    String errorJson = new Gson().toJson(errorResponse);
                    response.getWriter().print(errorJson);
        }        
    }
}


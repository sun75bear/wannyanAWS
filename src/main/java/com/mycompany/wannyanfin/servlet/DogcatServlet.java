package com.mycompany.wannyanfin.servlet;

import com.mycompany.wannyanfin.dao.DogcatDao;
import com.mycompany.wannyanfin.model.Dogcat;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList; 
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "DogcatServlet", urlPatterns = {"/dogcat"})
public class DogcatServlet extends HttpServlet {
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // 1. セッションからログインユーザーID（文字列）を取得
        String loggedInUserIdStr = (String)request.getSession().getAttribute("loggedInUserId");
        
        System.out.println("--- Dogcat Debug ---");
        System.out.println("セッションから取得したID (loggedInUserIdStr): [" + loggedInUserIdStr + "]");         
        try {
            if (loggedInUserIdStr != null && !loggedInUserIdStr.isEmpty()) {
                    System.out.println("★ IFブロック実行: リストをセットしました。"); 
                // 2. IDをint型に変換
                int loggedInUserId = Integer.parseInt(loggedInUserIdStr);
                
                // 3. 自分の投稿のみを取得するDAOメソッドを呼び出し
                List<Dogcat> list = DogcatDao.getLoginDogcat(loggedInUserId);
                
                request.setAttribute("dogcat", list);
            } else {
                    System.out.println("★ ELSEブロック実行: リダイレクトしました。");
                // ログインしていない場合、リストを空にする（またはログインページにリダイレクト）
//                request.setAttribute("dogcat", new ArrayList<Dogcat>());
                response.sendRedirect("login");
                return;
            }
            
            // JSPにフォワード
            request.getRequestDispatcher("dogcat.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            // IDが数値でない場合の処理
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
        
        // 送信されてくるデータのエンコーディングをUTF-8に設定
        request.setCharacterEncoding("UTF-8");
        
        // フォームから action パラメータを取得 (nullの場合がある)
        String action = request.getParameter("action");
        
        try {
            if ("delete".equals(action)) {
                // --- 削除処理 ---
                // wannyan_idを取得して削除DAOを呼び出す
                String wannyanIdParam = request.getParameter("wannyan_id");
                if (wannyanIdParam != null) {
                    int wannyanId = Integer.parseInt(wannyanIdParam);
                    DogcatDao.deleteDogcat(wannyanId); 
                }
            } else {
                // --- 登録処理 (actionがnullまたはdelete以外の場合) ---
                Dogcat m = new Dogcat();
                
                // user_idは登録フォームから必ず送られてくる
                String userIdParam = request.getParameter("user_id");
                if (userIdParam != null && !userIdParam.isEmpty()) {
                     m.setUserId(Integer.parseInt(userIdParam));
                }
                
                m.setDogcat(request.getParameter("dogcat"));
                m.setName(request.getParameter("name"));
                m.setText(request.getParameter("text"));
                
                // 登録処理を実行
                DogcatDao.addDogcat(m);
            }
        } catch (NumberFormatException e) {
            // パラメータが数値変換できない場合（通常は発生しない想定だが安全のため）
            System.err.println("数値変換エラー: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 登録・削除処理後、一覧画面にリダイレクト
        response.sendRedirect("dogcat");
    }
}

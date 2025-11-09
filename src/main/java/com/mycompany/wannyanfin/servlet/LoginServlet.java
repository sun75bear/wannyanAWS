package com.mycompany.wannyanfin.servlet;

import com.mycompany.wannyanfin.dao.UserDao;
import com.mycompany.wannyanfin.model.UserAuthResult;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // 送信されてくるデータのエンコーディングをUTF-8に設定
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password_hash");
        // validate()メソッドで判定、結果を受け取り、nullでなければ通す
        UserAuthResult result = UserDao.validate(username, password);

        if (result != null) {
            HttpSession session = request.getSession();
            
            // user_idとusernameをusersテーブルから取得し、セッションに保存
            session.setAttribute("loggedInUserId", result.getUserId());         
            session.setAttribute("loggedInUserName", result.getUsername());  
            // メンバー一覧ページにリダイレクト
            response.sendRedirect("dogcat");
            
        } else {
            request.setAttribute("error", "ログイン失敗");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
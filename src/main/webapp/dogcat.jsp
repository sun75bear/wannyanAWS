<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*,com.mycompany.wannyanfin.model.Dogcat" %>
<%
    // セッションからusersテーブルの、user_idとusernameを取得
    String loggedInUserId = (String)session.getAttribute("loggedInUserId");
    String loggedInUserName = (String)session.getAttribute("loggedInUserName");
    
    // nullチェック (ログインしていない場合は空文字にする)
    if (loggedInUserId == null) {
        // ログインしていない場合は、フォームを表示せずリダイレクトするなどの処理が望ましい
        loggedInUserId = ""; 
    }

    List<Dogcat> list = (List<Dogcat>)request.getAttribute("dogcat");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>🐶🐱犬猫投票所🐶🐱</title>
    <link href="https://fonts.googleapis.com/css2?family=Mochiy+Pop+P+One&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/idandsql/dogcat_style.css">
</head>
<body>
    <div class="main-content-container">
<h2>🐶🐱 犬or猫に投票してください！（一人一票！） 🐶🐱</h2>
<form method="post" action="dogcat">
    犬or猫: 
    <select name="dogcat">
        <option value="犬">犬🐶</option>
        <option value="猫">猫🐱</option>
    </select><br>
    表示名（任意）: <input type="text" 
                            name="name" 
                            placeholder=<%= loggedInUserName %>等(任意/10文字)>
    一言（任意）: <input type="text" 
                           name="text" 
                           placeholder="究極犬！至高猫！/30文字"><br>
    <input type="hidden" name="user_id" value="<%= loggedInUserId %>">
    <input type="submit" value="登録">
</form>

<h3>🐶🐱あなたが投票したのは🐶🐱</h3>
<table border="1">
    <tr><th>表示名（任意）</th><th>犬or猫</th><th>一言（任意）</th><th>削除</th></tr>
<% 
if (list != null) { // ★★★ この防御がNPEを防ぎます ★★★
for(Dogcat m : list) { %>
    <%
    //m.getUserid()について、後でこれとsessionに保存したIDと文字列で比較したいから、intな変数を文字列として格納する
    String rowUserId = String.valueOf(m.getUserId());
    %>
<tr>
    <td><%= m.getName() %></td>
    <td><%= m.getDogcat() %></td>
    <td><%= m.getText() %></td>
    
    <td class="action-cell">
    <% 
        // ★ 条件分岐のロジック: ログインIDと行のIDが一致するかをチェックして、一致していれば削除ボタンを表示する
        // String.equals()を使って、安全に文字列を比較する
        if (loggedInUserId.equals(rowUserId)) { 
    %>
        <!-- comment 削除ボタンが表示されている列のwannyan_idをdogcatにpostする -->
        <form method="post" action="dogcat">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="wannyan_id" value="<%= m.getWannyan_id() %>">
            <input type="submit" value="削除">
        </form>            
    <% } %>
    </td>
</tr>
<% } 
}%>

</table>
    <h3> 🐶🐱↓↓↓↓全体の結果はこちら↓↓↓↓🐶🐱</h3>
    <a href="sdrun_all.jsp">🐾🐾🐾🐾🐾🐾🐾🐾</a>
</div>
</body>
</html>
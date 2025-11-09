<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>🐶🐱 ログイン 🐶🐱</title>
    <link href="https://fonts.googleapis.com/css2?family=Mochiy+Pop+P+One&display=swap" rel="stylesheet">
    <link href="css/idandsql/login_style.css" rel="stylesheet">
</head>
<body>
<div class="login-container">
    <h2>🐶🐱 ログインしてください 🐱🐶</h2>
    
    <form action="login" method="post">
        <p>
            <label for="username">ID:</label> 
            <input type="text" id="username" name="username" required placeholder=abixx>
        </p>
        <p>
            <label for="password_hash">PASS:</label> 
            <input type="password" id="password_hash" name="password_hash" required placeholder=Passxx>
        </p>
        
        <input type="submit" value="ログイン">
    </form>
    
    <p class="error-message">${error}</p>
</div>
</html>

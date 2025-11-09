package com.mycompany.wannyanfin.model;

public class UserAuthResult {
    private String userId;
    private String username;

    // コンストラクタ
    public UserAuthResult(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    // ゲッターメソッド
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
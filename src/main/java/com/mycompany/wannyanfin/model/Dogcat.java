package com.mycompany.wannyanfin.model;

public class Dogcat {
    private int wannyan_id;
    private int user_id;
    private String dogcat;
    private String name;
    private String text;

    // getter/setter
    public int getWannyan_id() { return wannyan_id; }
    public void setWannyan_id(int wannyan_id) { this.wannyan_id = wannyan_id; }

    public int getUserId() {
        return user_id;
    }
    public void setUserId(int user_id) {
        this.user_id = user_id;
    }
    public String getDogcat() { return dogcat; }
    public void setDogcat(String dogcat) { this.dogcat = dogcat; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

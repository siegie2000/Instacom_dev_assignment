package com.thebegining.instacomdevassigment.ui.account;

public class your_data {
    private final String name;
    private String message;
    private final String avatar;
    private final String id;

    public your_data(String name, String message, String avatar,String id) {
        this.name = name;
        this.message = message;
        this.avatar = avatar;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getAvatar() {
        return avatar;
    }
    public String getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}



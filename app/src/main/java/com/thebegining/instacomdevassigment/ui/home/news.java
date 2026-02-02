package com.thebegining.instacomdevassigment.ui.home;

public class news {
    private final String name;
    private final String message;
    private final String avatar;
    private final String createdAt;

    public news(String name, String message, String avatar,String createdAt) {
        this.name = name;
        this.message = message;
        this.avatar = avatar;
        this.createdAt = createdAt;
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
    public String getCreatedAt() {
        return createdAt;
    }

}

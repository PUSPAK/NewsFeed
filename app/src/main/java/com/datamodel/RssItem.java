package com.datamodel;


public class RssItem {
    String title;
    String description;
    String link;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;


    String imageUrl;

    public String getLikebyme() {
        return likebyme;
    }

    public void setLikebyme(String likebyme) {
        this.likebyme = likebyme;
    }

    String likebyme;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

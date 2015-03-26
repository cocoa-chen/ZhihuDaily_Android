package com.example.chenaibin.zhihudaily;

/**
 * Created by chenaibin on 15/3/24.
 */
public class TopStory {
    public String image;
    public int id;
    public String title;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setImage(String images) {
        this.image = images;
    }
    public String getImage() {
        return image;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}

package com.example.chenaibin.zhihudaily;

import java.util.List;

/**
 * Created by chenaibin on 15/3/24.
 */
public class TodayStory {
    public List<String> images;
    public int id;
    public String title;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}

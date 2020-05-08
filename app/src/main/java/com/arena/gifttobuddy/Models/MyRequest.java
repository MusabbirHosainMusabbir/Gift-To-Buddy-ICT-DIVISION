package com.arena.gifttobuddy.Models;

public class MyRequest {
    String image,item,location,time;
    int status;

    public MyRequest(String image, String item, String location, String time, int status) {
        this.image = image;
        this.item = item;
        this.location = location;
        this.time = time;
        this.status = status;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

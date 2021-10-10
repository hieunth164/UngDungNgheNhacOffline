package com.example.nghenhacoffline.Model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class Song implements Serializable {
    private String name;
    private String singer;
    private String image;
    private int file;
    private int favorite;
    private int history;
    private int id;

    public Song(){}

    public Song(String name, String singer, String image, int file) {
        this.name = name;
        this.singer = singer;
        this.image = image;
        this.file = file;
    }

    public Song(String name, String singer, String image, int file,int favorite,int history,int id){
        this.name = name;
        this.singer = singer;
        this.image = image;
        this.file = file;
        this.favorite=favorite;
        this.history=history;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public String getImage() {
        return image;
    }

    public int getFile() {
        return file;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Bitmap convertStringToBitmapFromAccess(Context context, String filename){
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.example.nghenhacoffline.Model;

import java.io.Serializable;

public class PlayList implements Serializable {
    public int idPlayList;
    private String Title;
    private int idSong;

    public PlayList()  {
    }

    public PlayList(String Title, int idSong) {
        this.Title= Title;
        this.idSong= idSong;
    }
    public PlayList(int idPlayList, String Title) {
        this.Title= Title;
        this.idPlayList= idPlayList;
    }

    public PlayList(String title) {
        this.Title= title;
    }

    public PlayList(int idPlayList, String Title, int idSong) {
        this.idPlayList= idPlayList;
        this.Title= Title;
        this.idSong= idSong;
    }

    public int getIdPlayList() {
        return idPlayList;
    }

    public void setIdPlayList(int idPlayList) {
        this.idPlayList = idPlayList;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }
}

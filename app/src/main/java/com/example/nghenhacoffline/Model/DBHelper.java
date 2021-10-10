package com.example.nghenhacoffline.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Random;

public class DBHelper {
    Context context;
    Utils utils;
    String dbName = "MusicDB.db";

    public DBHelper(Context context) {
        this.context = context;
        utils = new Utils(context);
    }

    private SQLiteDatabase openDB() {
        return context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
    }
    private void closeDB(SQLiteDatabase db) {
        db.close();
    }

    public void createTable() {
        SQLiteDatabase db = openDB();
        String sqlSong = "CREATE TABLE IF NOT EXISTS tblSong (" +
                " IdSong INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " Name TEXT," +
                " Singer TEXT," +
                " Image TEXT," +
                " File INTEGER," +
                " Favorite INTEGER," +
                " History INTEGER );";
        String sqlPlayList= "CREATE TABLE IF NOT EXISTS tblPlayList (" +
                " IdPlayList INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " Title TEXT );";
        String sqlSongPlayList= "CREATE TABLE IF NOT EXISTS tblSongPlayList (" +
                " IdPlayList INTEGER," +
                " IdSong INTEGER,"+ " PRIMARY KEY(IdPlayList ,IdSong ))";

        db.execSQL(sqlSong);
        db.execSQL(sqlPlayList);
        db.execSQL(sqlSongPlayList);
        closeDB(db);
    }

    public ArrayList<Song> getALLSong() {
        SQLiteDatabase db = openDB();
        ArrayList<Song> arr = new ArrayList<>();
        String sql = "select * from tblSong";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String name = csr.getString(1);
                    String singer = csr.getString(2);
                    String image = csr.getString(3);
                    int file = csr.getInt(4);
                    int favorite = csr.getInt(5);
                    int history = csr.getInt(6);
                    arr.add(new
                            Song(name,singer,image,file,favorite,history,id));

                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }

    public void dropTable()
    {
        SQLiteDatabase db=openDB();
        String sql=" DROP TABLE IF EXISTS tblSong ";
        String sql1=" DROP TABLE IF EXISTS tblPlayList ";
        String sql2=" DROP TABLE IF EXISTS tblSongPlayList ";
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
        closeDB(db);

    }

    public void insertSong(){
        ArrayList<Song> arrSong = utils.getMockData();
        SQLiteDatabase db = openDB();
        Random random = new Random();
        for(Song s : arrSong) {
            ContentValues cv = new ContentValues();
            cv.put("Name", s.getName());
            cv.put("Singer", s.getSinger());
            cv.put("Image", s.getImage());
            cv.put("File", s.getFile());
            cv.put("Favorite", s.getFavorite());
            cv.put("History", s.getHistory());
            db.insert("tblSong", null, cv);
        }
        closeDB(db);
    }

    public void deleteSong(int idsong) {
        SQLiteDatabase db = openDB();
        db.delete("tblSong", "IdSong = "+idsong, null);
        db.close();
    }


    public ArrayList<Song> findSong(String nameSong) {
        SQLiteDatabase db = openDB();
        ArrayList<Song> arr = new ArrayList<>();
        String sql = "Select * FROM tblSong WHERE Name Like '%"+ nameSong +"%'";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String name = csr.getString(1);
                    String singer = csr.getString(2);
                    String image = csr.getString(3);
                    int file = csr.getInt(4);
                    int favorite = csr.getInt(5);
                    int history = csr.getInt(6);
                    arr.add(new
                            Song(name,singer,image,file,favorite,history,id));
                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }

    public void updateFavorite(int id,int favorite){
        SQLiteDatabase db = openDB();
        ContentValues values = new ContentValues();
        values.put("Favorite", favorite);
        db.update("tblSong", values, "IdSong="+id,null );
        db.close();
    }
    public ArrayList<Song> getSongFavorite() {
        SQLiteDatabase db = openDB();
        ArrayList<Song> arr = new ArrayList<>();
        String sql = "select * from tblSong where Favorite = 1";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String name = csr.getString(1);
                    String singer = csr.getString(2);
                    String image = csr.getString(3);
                    int file = csr.getInt(4);
                    int favorite = csr.getInt(5);
                    int history = csr.getInt(6);
                    arr.add(new
                            Song(name,singer,image,file,favorite,history,id));

                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }

    public void insertPlayList(String title) {
        SQLiteDatabase db = openDB();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        db.insert("tblPlayList", null, values);
        db.close();
    }

    public void updatePlayList(int id,String title){
        SQLiteDatabase db = openDB();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        db.update("tblPlayList", values, "IdPlayList="+id,null );
        db.close();
    }

    public void deletePlayList(int id){
        SQLiteDatabase db = openDB();
        db.delete("tblSongPlayList", "IdPlayList = "+id, null);
        db.delete("tblPlayList", "IdPlayList = "+id, null);

        closeDB(db);
    }

    public ArrayList<PlayList> getALLPlayList() {
        SQLiteDatabase db = openDB();
        ArrayList<PlayList> arr = new ArrayList<>();
        String sql = "select * from tblPlayList";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String title = csr.getString(1);
                    arr.add(new
                            PlayList(id,title));

                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }

    public ArrayList<Song> getListSongInPlayList(int idPlayList) {
        String selectQuery = "select  tblSong.IdSong,Name,Singer,Image,File,Favorite,History" +
                " from tblSong, tblSongPlayList where tblSongPlayList.IdPlayList = '" + idPlayList+"'" +
                " AND tblSong.IdSong = tblSongPlayList.IdSong";
        SQLiteDatabase db = openDB();
        ArrayList<Song> listSong = new ArrayList<Song>();
        Cursor csr = db.rawQuery(selectQuery, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String name = csr.getString(1);
                    String singer = csr.getString(2);
                    String image = csr.getString(3);
                    int file = csr.getInt(4);
                    int favorite = csr.getInt(5);
                    int history = csr.getInt(6);
                    listSong.add(new
                            Song(name,singer,image,file,favorite,history,id));
                } while (csr.moveToNext());
            }
        }
        return listSong;
    }

    public void insertSongForPlayList(int idPlayList, int idSong) {
        SQLiteDatabase db = openDB();
        ContentValues values = new ContentValues();
        values.put("IdPlayList", idPlayList);
        values.put("IdSong", idSong);
        db.insert("tblSongPlayList", null, values);
        db.close();
    }

    public void deleteSongInPlayList(int idsong,int idPlayList) {
        SQLiteDatabase db = openDB();
//        db.delete("tblSongPlayList", "IdPlayList = "+idPlayList+ "AND + IdSong ="+idsong,
//                null);
        db.delete("tblSongPlayList", "IdPlayList = ? AND IdSong  = ?",
                new String[] {String.valueOf(idPlayList),String.valueOf(idsong)});
        db.close();
    }


    public void UpdateSongHistory(int id,int history)
    {
        SQLiteDatabase db = openDB();
        ContentValues cv = new ContentValues();
        cv.put("History", history);
        db.update("tblSong", cv, "IdSong="+id,null );
        closeDB(db);
    }

    public void DeleteSongHistory()
    {
        SQLiteDatabase db = openDB();
        ContentValues cv = new ContentValues();
        cv.put("History", 0);
        db.update("tblSong", cv, null,null );
        closeDB(db);

    }

    public ArrayList<Song> getSongHistory() {
        SQLiteDatabase db = openDB();
        ArrayList<Song> arr = new ArrayList<>();
        String sql = "select * from tblSong where History = 1";
        Cursor csr = db.rawQuery(sql, null);
        if (csr != null) {
            if (csr.moveToFirst()) {
                do {
                    int id = csr.getInt(0);
                    String name = csr.getString(1);
                    String singer = csr.getString(2);
                    String image = csr.getString(3);
                    int file = csr.getInt(4);
                    int favorite = csr.getInt(5);
                    int history = csr.getInt(6);
                    arr.add(new
                            Song(name,singer,image,file,favorite,history,id));

                } while (csr.moveToNext());
            }
        }
        closeDB(db);
        return arr;
    }
}

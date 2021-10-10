package com.example.nghenhacoffline.Model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.nghenhacoffline.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Utils {
    Context context;

    public Utils(Context context) {
        this.context = context;
    }

    String filename = "database";
    public static ArrayList<Song> songHistory = new ArrayList<>();

    public void addSongHistory(Song song) {
        if (songHistory.indexOf(song) > 0) {
            this.songHistory.add(0, song);
        }
    }

    public  int KTTrung(int id,ArrayList<Song> arrayList){
        for (int i=0;i<arrayList.size();i++)
            if (id==arrayList.get(i).getId())
                return 0;
        return 1;
    }

    public ArrayList<Song> getSongHistory() {
        return this.songHistory;
    }
    public void WriteToFileInternal(ArrayList<Song> arrayList){
        try {
            File file = new File(context.getFilesDir(), filename);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new
                    ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(arrayList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Song> LoadFileInternal() {
        ArrayList<Song> arrayList = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir(), filename);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new
                    ObjectInputStream(fileInputStream);
            arrayList = (ArrayList<Song>) objectInputStream.readObject();
            Log.d("SongApp", arrayList.size() + "");
            return arrayList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Song> getMockData() {
        ArrayList<Song> tmp = new ArrayList<>();
        tmp.add(new Song(context.getString(R.string.namlaydoibantay),
                context.getString(R.string.kaytran), "namdoibantay.jpg",R.raw.namdoibantay_kaytran));
        tmp.add(new Song(context.getString(R.string.motnha),
                context.getString(R.string.dalab), "albummotnha.jpg",R.raw.motnha_dalab));
        tmp.add(new Song(context.getString(R.string.somhomqua),
                context.getString(R.string.dalab), "somhomqua.jpg",R.raw.somhomqua_dalab));
        tmp.add(new Song(context.getString(R.string.hanoigiotantam),
                context.getString(R.string.dalab), "hanoigiotantam.jpg",R.raw.hanoigiotantam_dalab));
        tmp.add(new Song(context.getString(R.string.thanhxuan),
                context.getString(R.string.dalab), "thanhxuan.jpg",R.raw.thanhxuan_dalab));
        tmp.add(new Song(context.getString(R.string.thucgiac),
                context.getString(R.string.dalab), "thucgiac.jpg",R.raw.thucgiac_dalab));
        tmp.add(new Song(context.getString(R.string.gaclaiaulo),
                context.getString(R.string.dalab), "gaclaiaulo.jpg",R.raw.gaclaiaulo_dalab));
        tmp.add(new Song(context.getString(R.string.tim),
                context.getString(R.string.min), "min.jpg",R.raw.tim_min));
        tmp.add(new Song(context.getString(R.string.idontneedaman),
                context.getString(R.string.min), "min.jpg",R.raw.idontneedaman_min));
        tmp.add(new Song(context.getString(R.string.motculua),
                context.getString(R.string.bichphuong), "motculua.jpg",R.raw.motculua_bichphuong));
        tmp.add(new Song(context.getString(R.string.embohutthuocchua),
                context.getString(R.string.bichphuong), "embohutthuocchua.jpg",R.raw.embohutthuochua_bichphuong));
        tmp.add(new Song(context.getString(R.string.dramaqueen),
                context.getString(R.string.bichphuong), "dramaqueen.jpg",R.raw.dramaqueen_bichphuong));
        tmp.add(new Song(context.getString(R.string.chumeo),
                context.getString(R.string.bichphuong), "chumeo.jpg",R.raw.chumeo_bichphuong));
        tmp.add(new Song(context.getString(R.string.rangemmaioben),
                context.getString(R.string.bichphuong), "bichphuong.jpg",R.raw.rangemmaioben_bichphuong));
        tmp.add(new Song(context.getString(R.string.guianhxanho),
                context.getString(R.string.bichphuong), "guianhxanho.jpg",R.raw.guianhxanho));
        return tmp;
    }

    public Bitmap convertStringToBitmapFromAccess(String filename){
        AssetManager assetManager = context.getAssets();

        try
        {
            InputStream is = assetManager.open(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringTimeFromDuration(int millis) {
        if (millis >= 3600000) {
            return String.format("%01d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        }
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}

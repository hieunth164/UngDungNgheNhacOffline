package com.example.nghenhacoffline.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nghenhacoffline.Adapter.SongAdapter;
import com.example.nghenhacoffline.MainActivity;
import com.example.nghenhacoffline.Model.DBHelper;
import com.example.nghenhacoffline.Model.Song;
import com.example.nghenhacoffline.Model.Utils;
import com.example.nghenhacoffline.R;

import java.util.ArrayList;

public class ShowSongActivity extends AppCompatActivity {
    private ListView listView;
    public static ArrayList<Song> arrayList;
    private SongAdapter songAdapter;
    private Utils utils;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_song);

        utils= new Utils(getApplicationContext()) ;
        dbHelper = new DBHelper(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.layout_actionbar);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }
        Intent intent = getIntent();
        int idPlayList = intent.getIntExtra("idPlayList",0);

        listView = findViewById(R.id.listView);
        arrayList=dbHelper.getALLSong();
        songAdapter = new SongAdapter(getApplicationContext(),arrayList);
        listView.setAdapter(songAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long
                    l) {
                dbHelper.insertSongForPlayList(idPlayList,arrayList.get(i).getId());
                Intent intent = new Intent(getApplicationContext(), SongOfPlaylistActivity.class);
                intent.putExtra("ShowSong",i);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(ShowSongActivity.this, SongOfPlaylistActivity.class);
        startActivity(intent);
        finish();
        return super.onSupportNavigateUp();
    }
}
package com.example.nghenhacoffline.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.nghenhacoffline.Adapter.SongAdapter;
import com.example.nghenhacoffline.MainActivity;
import com.example.nghenhacoffline.Model.DBHelper;
import com.example.nghenhacoffline.Model.Song;
import com.example.nghenhacoffline.Model.Utils;
import com.example.nghenhacoffline.R;

import java.util.ArrayList;

import me.gujun.android.taggroup.TagGroup;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    public  static ArrayList<Song> arrayList;
    Utils utils;
    ListView listView;
    SongAdapter songAdapter;
    TagGroup tagGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Search");
        }

        utils = new Utils(SearchActivity.this);

        arrayList = new ArrayList<>();

        listView = findViewById(R.id.listView);
        songAdapter = new SongAdapter(SearchActivity.this,arrayList);
        listView.setAdapter(songAdapter);

        searchView=findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContact(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchContact(newText);
                return false;
            }


        });
        tagGroup=findViewById(R.id.tag_group);
        tagGroup.setTags(new String[]{"Thức Giấc","Thanh Xuân"});
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                searchView.setQuery(tag,false);
                hideSoftKeyboard(searchView);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, PlayMusicActivity.class);
                intent.putExtra("timkiembaihat",position);
                startActivity(intent);
            }
        });
    }
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void searchContact(String keyword) {
        DBHelper databaseHelper = new DBHelper(getApplicationContext());
        ArrayList<Song> contacts = databaseHelper.findSong(keyword);
        if (contacts != null) {
            songAdapter.clear();
            songAdapter.addAll(contacts);
            songAdapter.notifyDataSetChanged();
            listView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return super.onSupportNavigateUp();
    }
}
package com.example.nghenhacoffline.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nghenhacoffline.Adapter.SongAdapter;
import com.example.nghenhacoffline.Fragment.PlayListFragment;
import com.example.nghenhacoffline.Fragment.SongFragment;
import com.example.nghenhacoffline.MainActivity;
import com.example.nghenhacoffline.Model.DBHelper;
import com.example.nghenhacoffline.Model.PlayList;
import com.example.nghenhacoffline.Model.Song;
import com.example.nghenhacoffline.R;

import java.util.ArrayList;

public class SongOfPlaylistActivity extends AppCompatActivity {
    private TextView txtTitle;
    public ArrayList<PlayList> playListArrayList;
    private int position;
    DBHelper dbHelper;
    ListView listView;
    public static ArrayList<Song> songArrayList;
    SongAdapter songAdapter;
    int idPlayList;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_of_playlist);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dbHelper= new DBHelper(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.layout_actionbar_playlist);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }


        txtTitle = findViewById(R.id.tvTenPlaylist);
        listView = findViewById(R.id.listView);

        Intent intent = getIntent();
        position=intent.getIntExtra("Playlist", 0);

        playListArrayList = new ArrayList<PlayList>();
        playListArrayList.addAll(PlayListFragment.arrayList);
        txtTitle.setText(playListArrayList.get(position).getTitle());
        idPlayList = playListArrayList.get(position).getIdPlayList();
        songArrayList=dbHelper.getListSongInPlayList(idPlayList);
        songAdapter = new SongAdapter(SongOfPlaylistActivity.this,songArrayList);
        listView.setAdapter(songAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long
                    l) {

                Intent intent = new Intent(getApplicationContext(), PlayMusicActivity.class);
                intent.putExtra("SongOfPlaylist",i);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Xóa bài hát")
                        .setMessage("Bạn muốn xóa bài hát này?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dbHelper.deleteSongInPlayList(songArrayList.get(i).getId(),idPlayList);
                                songArrayList.remove(i);
                                songAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Hủy",null)
                        .show();
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(SongOfPlaylistActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.playlistmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int time;
        switch (item.getItemId()) {

            case R.id.xoaPlaylist:

                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Xóa Playlist")
                        .setMessage("Bạn muốn xóa Playlist này?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dbHelper.deletePlayList(playListArrayList.get(position).getIdPlayList());
                                Intent intent = new Intent(SongOfPlaylistActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Hủy",null)
                        .show();
                break;

            case R.id.themBaiHat:
                Intent intent1 = new Intent(SongOfPlaylistActivity.this, ShowSongActivity.class);
                intent1.putExtra("idPlayList",playListArrayList.get(position).getIdPlayList());
                startActivity(intent1);
                finish();
                break;

            case R.id.suaPlaylist:
                final Dialog dialog = new Dialog(SongOfPlaylistActivity.this);
                dialog.setContentView(R.layout.dialog_playlist);

                EditText namePlayList = dialog.findViewById(R.id.etPlayList);
                Button btnOk = dialog.findViewById(R.id.btnOk);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = namePlayList.getText().toString();
                        if (title.length() == 0 || title.equals("") || title == null) {
                            Toast.makeText(getApplicationContext(),
                                    "Bạn chưa nhập tên cho PlayList..!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        dbHelper.updatePlayList(playListArrayList.get(position).getIdPlayList(),title);
                        dialog.dismiss();
                        Intent intent2 = new Intent(SongOfPlaylistActivity.this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                });

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                break;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
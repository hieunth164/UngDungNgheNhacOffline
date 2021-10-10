package com.example.nghenhacoffline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.nghenhacoffline.Activity.PlayMusicActivity;
import com.example.nghenhacoffline.Activity.SearchActivity;
import com.example.nghenhacoffline.Activity.SongOfPlaylistActivity;
import com.example.nghenhacoffline.Adapter.PageAdapter;
import com.example.nghenhacoffline.Fragment.HistoryFragment;
import com.example.nghenhacoffline.Fragment.PlayListFragment;
import com.example.nghenhacoffline.Fragment.SongFragment;
import com.example.nghenhacoffline.Model.DBHelper;
import com.example.nghenhacoffline.Model.PlayList;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private TabItem tabItem1, tabItem2, tabItem3,tabItem4;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    DBHelper dbHelper;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.layout_actionbar);
        }

        dbHelper = new DBHelper(getApplicationContext());
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabItem1 = (TabItem) findViewById(R.id.tab1);
        tabItem2 = (TabItem) findViewById(R.id.tab2);
        tabItem3 = (TabItem) findViewById(R.id.tab3);
        tabItem4 = (TabItem) findViewById(R.id.tab4);
        viewPager = (ViewPager) findViewById(R.id.viewPaper);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3)
                    pageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuOption:
                Toast.makeText(this, "Option button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mnuSearch:
                Toast.makeText(this, "Search button selected", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, SearchActivity.class);
                startActivity(i);
                return true;

            case R.id.mnuPlaylist:
                final Dialog dialog = new Dialog(MainActivity.this);
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
                        dbHelper.insertPlayList(title);
                        PlayListFragment.arrayList.clear();
                        PlayListFragment.arrayList.addAll(dbHelper.getALLPlayList());
                        PlayListFragment.playListAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                return true;

            case R.id.mnuXoaLichSu:
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Xóa lịch sử")
                        .setMessage("Bạn muốn xóa lịch sử?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dbHelper.DeleteSongHistory();
                                HistoryFragment.arrayList.clear();
                                HistoryFragment.songAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Hủy",null)
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
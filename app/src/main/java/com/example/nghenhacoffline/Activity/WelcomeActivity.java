package com.example.nghenhacoffline.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.nghenhacoffline.MainActivity;
import com.example.nghenhacoffline.Model.DBHelper;
import com.example.nghenhacoffline.R;

public class WelcomeActivity extends AppCompatActivity {
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        dbHelper = new DBHelper(WelcomeActivity.this);
        //Khởi tạo bảng
        dbHelper.createTable();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dbHelper.getALLSong().size() > 0){
            Intent intent=new Intent(WelcomeActivity.this, LoadingActivity.class);
            startActivity(intent);
        }else {
            dbHelper.insertSong();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(WelcomeActivity.this, LoadingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);

        }
    }
}
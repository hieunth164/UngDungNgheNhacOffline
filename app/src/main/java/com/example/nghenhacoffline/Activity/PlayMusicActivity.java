package com.example.nghenhacoffline.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nghenhacoffline.Fragment.FavoriteFragment;
import com.example.nghenhacoffline.Fragment.HistoryFragment;
import com.example.nghenhacoffline.Fragment.SongFragment;
import com.example.nghenhacoffline.MainActivity;
import com.example.nghenhacoffline.Model.DBHelper;
import com.example.nghenhacoffline.Model.Song;
import com.example.nghenhacoffline.Model.Utils;
import com.example.nghenhacoffline.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class PlayMusicActivity extends AppCompatActivity {
    private TextView txtTitle, txtSinger, txtStartTime, txtEndTime,tvTimeSleep;
    private Button btnOk,btnCancel;
    private SeekBar seekBar;
    private ImageButton btnPlay, btnNext, btnPrev, btnLoop,btnRandom,btnHeart;
    private ImageView imageBackground,imageView,imgClock;
    private MediaPlayer mediaPlayer;
    private int position;
    private boolean repeat = false;
    private boolean checkrandom = false;
    ArrayList<Song> arrayListSong;
    CountDownTimer timerSleep;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.layout_actionbar_play_music);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }

        txtTitle = findViewById(R.id.tvTenBaiHat);
        txtSinger = findViewById(R.id.tvTenCaSi);
        txtStartTime = findViewById(R.id.textViewStartTime);
        txtEndTime = findViewById(R.id.textViewEndTime);
        imageView = findViewById(R.id.imageView);
        imageBackground = findViewById(R.id.imageBackgroup);
        seekBar = findViewById(R.id.seekBar);
        btnPlay = findViewById(R.id.imageButtonPlay);
        btnNext = findViewById(R.id.imageButtonNext);
        btnPrev = findViewById(R.id.imageButtonPrevious);
        btnLoop = findViewById(R.id.imageButtonLoop);
        btnRandom = findViewById(R.id.imageButtonRandom);
        imgClock = findViewById(R.id.imgClock);
        tvTimeSleep = findViewById(R.id.tvTimeSleep);
        btnHeart=findViewById(R.id.imageButtonHeart);
        dbHelper = new DBHelper(PlayMusicActivity.this);


        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("baihat")){
                position=intent.getIntExtra("baihat", 0);
                arrayListSong = new ArrayList<Song>();
                arrayListSong.addAll(SongFragment.arrayList);
            }
            if (intent.hasExtra("baihatyeuthich")){
                position=intent.getIntExtra("baihatyeuthich", 0);
                arrayListSong = new ArrayList<Song>();
                arrayListSong.addAll(FavoriteFragment.arrayList);

            }
            if (intent.hasExtra("timkiembaihat")){
                position=intent.getIntExtra("timkiembaihat", 0);
                arrayListSong = new ArrayList<Song>();
                arrayListSong.addAll(SearchActivity.arrayList);

            }
            if (intent.hasExtra("SongOfPlaylist")) {
                position = intent.getIntExtra("SongOfPlaylist", 0);
                arrayListSong = new ArrayList<Song>();
                arrayListSong.addAll(SongOfPlaylistActivity.songArrayList);
            }
            if (intent.hasExtra("lichsu")) {
                position = intent.getIntExtra("lichsu", 0);
                arrayListSong = new ArrayList<Song>();
                arrayListSong.addAll(HistoryFragment.arrayList);
            }
        }
        txtTitle.setText(arrayListSong.get(position).getName());
        txtSinger.setText(arrayListSong.get(position).getSinger());
        imageView.setImageBitmap(Song.convertStringToBitmapFromAccess(getApplication(),arrayListSong.get(position).getImage()));
        imageBackground.setImageBitmap(Song.convertStringToBitmapFromAccess(getApplication(),arrayListSong.get(position).getImage()));
        if(arrayListSong.get(position).getFavorite() == 0){
            btnHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }else{
            btnHeart.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        if (arrayListSong.get(position).getHistory() == 0){
            dbHelper.UpdateSongHistory(arrayListSong.get(position).getId(),1);
        }
        mediaPlayer=MediaPlayer.create(PlayMusicActivity.this,arrayListSong.get(position).getFile());
        mediaPlayer.start();
        startAnimation();

        btnPlay.setImageResource(R.drawable.pause);
        TimeSong();
        UpdateTime();

        btnHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayListSong.get(position).getFavorite() == 0){
                    arrayListSong.get(position).setFavorite(1);
                    Animation animation = AnimationUtils.loadAnimation(PlayMusicActivity.this, R.anim.anim_heartclick);
                    btnHeart.setImageResource(R.drawable.ic_baseline_favorite_24);
                    view.startAnimation(animation);
                }else{
                    arrayListSong.get(position).setFavorite(0);
                    btnHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
//
                dbHelper.updateFavorite(arrayListSong.get(position).getId(), arrayListSong.get(position).getFavorite());

            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                    stopAnimation();

                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                    TimeSong();
                    UpdateTime();
                    startAnimation();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayListSong.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < (arrayListSong.size())) {
                        btnPlay.setImageResource(R.drawable.pause);
                        position--;
                        if (position < 0) {
                            position = arrayListSong.size() - 1;
                        }
                        if (repeat == true) {
                            position += 1;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(arrayListSong.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        txtTitle.setText(arrayListSong.get(position).getName());
                        txtSinger.setText(arrayListSong.get(position).getSinger());
                        imageView.setImageBitmap(Song.convertStringToBitmapFromAccess(getApplication(),arrayListSong.get(position).getImage()));
                        imageBackground.setImageBitmap(Song.convertStringToBitmapFromAccess(getApplication(),arrayListSong.get(position).getImage()));
                        if(arrayListSong.get(position).getFavorite() == 0){
                            btnHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        }else{
                            btnHeart.setImageResource(R.drawable.ic_baseline_favorite_24);
                        }
                        if (arrayListSong.get(position).getHistory() == 0){
                            dbHelper.UpdateSongHistory(arrayListSong.get(position).getId(),1);
                        }
                        mediaPlayer=MediaPlayer.create(PlayMusicActivity.this,arrayListSong.get(position).getFile());
                        mediaPlayer.start();
                        TimeSong();
                        UpdateTime();
                    }
                }
                btnNext.setClickable(false);
                btnPrev.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnPrev.setClickable(true);
                        btnNext.setClickable(true);
                    }
                }, 3000);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayListSong.size() > 0){
                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < (arrayListSong.size())){
                        btnPlay.setImageResource(R.drawable.pause);
                        position++;
                        if (repeat == true){
                            if (position == 0){
                                position = arrayListSong.size();
                            }
                            position -= 1;
                        }
                        if (checkrandom == true){
                            Random random = new Random();
                            int index = random.nextInt(arrayListSong.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > arrayListSong.size() - 1){
                            position = 0;
                        }

                        txtTitle.setText(arrayListSong.get(position).getName());
                        txtSinger.setText(arrayListSong.get(position).getSinger());
                        imageView.setImageBitmap(Song.convertStringToBitmapFromAccess(getApplication(),arrayListSong.get(position).getImage()));
                        imageBackground.setImageBitmap(Song.convertStringToBitmapFromAccess(getApplication(),arrayListSong.get(position).getImage()));
                        if(arrayListSong.get(position).getFavorite() == 0){
                            btnHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        }else{
                            btnHeart.setImageResource(R.drawable.ic_baseline_favorite_24);
                        }
                        if (arrayListSong.get(position).getHistory() == 0){
                            dbHelper.UpdateSongHistory(arrayListSong.get(position).getId(),1);
                        }
                        mediaPlayer=MediaPlayer.create(PlayMusicActivity.this,arrayListSong.get(position).getFile());
                        mediaPlayer.start();
                        TimeSong();
                        UpdateTime();
                    }
                }
                btnNext.setClickable(false);
                btnPrev.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnNext.setClickable(true);
                        btnPrev.setClickable(true);
                    }
                }, 3000);
            }
        });

        btnLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeat == false){
                    if (checkrandom == true){
                        checkrandom = false;
                        btnLoop.setImageResource(R.drawable.loop1);
                        btnRandom.setImageResource(R.drawable.random);
                        repeat = true;
                    }else {
                        btnLoop.setImageResource(R.drawable.loop1);
                        repeat = true;
                    }
                }else {
                    btnLoop.setImageResource(R.drawable.loop);
                    repeat = false;
                }
            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkrandom == false){
                    if (repeat == true){
                        repeat = false;
                        btnRandom.setImageResource(R.drawable.random1);
                        btnLoop.setImageResource(R.drawable.loop);
                        checkrandom = true;
                    }else {
                        btnRandom.setImageResource(R.drawable.random1);
                        checkrandom = true;
                    }
                }else {
                    btnRandom.setImageResource(R.drawable.random);
                    checkrandom = false;
                }
            }
        });
    }
    private void UpdateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                txtStartTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (position < (arrayListSong.size())) {
                            position++;
                            if (repeat == true) {
                                position --;
                            }
                            if (checkrandom == true) {
                                Random random = new Random();
                                int index = random.nextInt(arrayListSong.size());
                                if (index == position) {
                                    position = index - 1;
                                }
                                position = index;
                            }
                            if (position > arrayListSong.size() - 1) {
                                position = 0;
                            }
                            try {
                                txtTitle.setText(arrayListSong.get(position).getName());
                                txtSinger.setText(arrayListSong.get(position).getSinger());
                                imageView.setImageBitmap(Song.convertStringToBitmapFromAccess(getApplication(),arrayListSong.get(position).getImage()));
                                imageBackground.setImageBitmap(Song.convertStringToBitmapFromAccess(getApplication(),arrayListSong.get(position).getImage()));
                                if(arrayListSong.get(position).getFavorite() == 0){
                                    btnHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                }else{
                                    btnHeart.setImageResource(R.drawable.ic_baseline_favorite_24);
                                }
                                if (arrayListSong.get(position).getHistory() == 0){
                                    dbHelper.UpdateSongHistory(arrayListSong.get(position).getId(),1);
                                }
                                mediaPlayer=MediaPlayer.create(PlayMusicActivity.this,arrayListSong.get(position).getFile());
                                mediaPlayer.start();
                                TimeSong();
                                UpdateTime();

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }, 500);
    }

    private void TimeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtEndTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    @Override
    public boolean onSupportNavigateUp() {
        mediaPlayer.stop();
        Intent intent = new Intent(PlayMusicActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.playmusicmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int time;
        switch (item.getItemId()) {

            case R.id.action_5:
                time = 300000;
                stop_sleepp(time, 1000);
                tvTimeSleep.setVisibility(View.INVISIBLE);
                imgClock.setVisibility(View.INVISIBLE);
                break;
            case R.id.action_10:
                time = 600000;
                stop_sleepp(time, 1000);
                tvTimeSleep.setVisibility(View.INVISIBLE);
                imgClock.setVisibility(View.INVISIBLE);
                break;
            case R.id.action_15:
                time = 900000;
                stop_sleepp(time, 1000);
                tvTimeSleep.setVisibility(View.INVISIBLE);
                imgClock.setVisibility(View.INVISIBLE);
                break;
            case R.id.action_30:
                time = 1800000;
                stop_sleepp(time, 1000);
                tvTimeSleep.setVisibility(View.INVISIBLE);
                imgClock.setVisibility(View.INVISIBLE);
                break;
            case R.id.action_60:
                time = 3600000;
                stop_sleepp(time, 1000);
                tvTimeSleep.setVisibility(View.INVISIBLE);
                imgClock.setVisibility(View.INVISIBLE);
                break;
            case R.id.action:
                final Dialog dialog = new Dialog(PlayMusicActivity.this);
                dialog.setContentView(R.layout.dialog_clock);

                EditText nhapSoPhut = dialog.findViewById(R.id.etPlayList);
                btnOk = dialog.findViewById(R.id.btnOk);
                btnCancel = dialog.findViewById(R.id.btnCancel);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if (nhapSoPhut.length() == 0 || nhapSoPhut.equals("") || nhapSoPhut == null) {
                            Toast.makeText(PlayMusicActivity.this, "Bạn chưa nhập số phút..", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else{
                            int number = Integer.parseInt(nhapSoPhut.getText().toString());
                            int time2 =number* 60000;
                            stop_sleepp(time2, 1000);
                            tvTimeSleep.setVisibility(View.INVISIBLE);
                            imgClock.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                        }

                    }
                });

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();

                break;
            case R.id.action_clear:
                timerSleep.cancel();
                tvTimeSleep.setVisibility(View.INVISIBLE);
                imgClock.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Đã tắt hẹn giờ ", Toast.LENGTH_LONG).show();

                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void stop_sleepp(int time, int interval) {
        tvTimeSleep.setVisibility(View.VISIBLE);
        imgClock.setVisibility(View.VISIBLE);
        timerSleep = new CountDownTimer(time, interval) {

            public void onTick(long millisUntilFinished) {
                String countDown = Utils.getStringTimeFromDuration((int) millisUntilFinished);
                tvTimeSleep.setText(countDown);
            }

            public void onFinish() {
                mediaPlayer.pause();
                btnPlay.setImageResource(R.drawable.play);
                stopAnimation();
            }
        };
        timerSleep.start();
        Toast.makeText(this, "Đã bật hẹn giờ tắt sau " + Utils.getStringTimeFromDuration(time), Toast.LENGTH_LONG).show();
    }

    private void startAnimation(){
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                imageView.animate().rotationBy(360).withEndAction(this).setDuration(10000).setInterpolator(new LinearInterpolator()).start();
            }
        };
        imageView.animate().rotationBy(360).withEndAction(runnable).setDuration(10000).setInterpolator(new LinearInterpolator()).start();
    }

    private void stopAnimation(){
        imageView.animate().cancel();
    }
}
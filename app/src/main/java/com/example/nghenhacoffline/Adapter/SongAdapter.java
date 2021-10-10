package com.example.nghenhacoffline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nghenhacoffline.Model.DBHelper;
import com.example.nghenhacoffline.Model.Song;
import com.example.nghenhacoffline.R;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    DBHelper dbHelper;
    public SongAdapter(@NonNull Context context, @NonNull List<Song> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_song, parent, false);
        Song song = getItem(position);
        TextView tvNameSong = convertView.findViewById(R.id.tvNameSong);
        TextView tvNameSinger = convertView.findViewById(R.id.tvNameSinger);
        ImageView imageSinger = convertView.findViewById(R.id.imageSong);


        tvNameSong.setText(song.getName());
        tvNameSinger.setText(song.getSinger());
        //imageSinger.setImageBitmap(song.getImage());
        imageSinger.setImageBitmap(Song.convertStringToBitmapFromAccess(getContext(),song.getImage()));
        return convertView;
    }

}

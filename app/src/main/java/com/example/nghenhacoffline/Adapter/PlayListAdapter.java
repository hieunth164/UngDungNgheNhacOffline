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

import com.example.nghenhacoffline.Model.PlayList;
import com.example.nghenhacoffline.R;

import java.util.List;

public class PlayListAdapter extends ArrayAdapter<PlayList> {
    public PlayListAdapter(@NonNull Context context, @NonNull List<PlayList> objects){
        super(context,0,objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_playlist, parent, false);
        PlayList playList = getItem(position);
        TextView tvTitle = convertView.findViewById(R.id.tvNamePlayList);
        ImageView imageView = convertView.findViewById(R.id.imagePlayList);


        tvTitle.setText(playList.getTitle());
        imageView.setImageResource(R.drawable.playlist);
        return convertView;
    }
}

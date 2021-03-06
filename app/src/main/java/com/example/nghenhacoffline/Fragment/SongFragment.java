package com.example.nghenhacoffline.Fragment;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nghenhacoffline.Activity.PlayMusicActivity;
import com.example.nghenhacoffline.Adapter.SongAdapter;
import com.example.nghenhacoffline.Model.DBHelper;
import com.example.nghenhacoffline.Model.Song;
import com.example.nghenhacoffline.Model.Utils;
import com.example.nghenhacoffline.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongFragment extends Fragment {
    private ListView listView;
    public static ArrayList<Song> arrayList;
    private SongAdapter songAdapter;
    private Utils utils;
    DBHelper dbHelper;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongFragment newInstance(String param1, String param2) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        utils= new Utils(getContext()) ;
        dbHelper = new DBHelper(getContext());
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.listView);
        arrayList=dbHelper.getALLSong();
        songAdapter = new SongAdapter(getContext(),arrayList);
        listView.setAdapter(songAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long
                    l) {
                if (arrayList.get(i).getHistory() == 0){
                    dbHelper.UpdateSongHistory(arrayList.get(i).getId(),1);
                }

                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                intent.putExtra("baihat",i);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {

                new AlertDialog.Builder(SongFragment.this.getContext()).setIcon(android.R.drawable.ic_delete)
                        .setTitle("X??a b??i h??t")
                        .setMessage("B???n mu???n x??a b??i h??t n??y?")
                        .setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.deleteSong(arrayList.get(pos).getId());
                                arrayList.remove(pos);
                                songAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("H???y",null)
                        .show();

                return false;
            }
        });
    }

}
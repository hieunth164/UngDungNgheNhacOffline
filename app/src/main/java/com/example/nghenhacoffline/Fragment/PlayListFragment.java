package com.example.nghenhacoffline.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nghenhacoffline.Activity.SongOfPlaylistActivity;
import com.example.nghenhacoffline.Adapter.PlayListAdapter;
import com.example.nghenhacoffline.Adapter.SongAdapter;
import com.example.nghenhacoffline.MainActivity;
import com.example.nghenhacoffline.Model.DBHelper;
import com.example.nghenhacoffline.Model.PlayList;
import com.example.nghenhacoffline.Model.Song;
import com.example.nghenhacoffline.Model.Utils;
import com.example.nghenhacoffline.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListFragment extends Fragment {
    private ListView listView;
    public static ArrayList<PlayList> arrayList;
    public static PlayListAdapter playListAdapter;
    private Utils utils;
    DBHelper dbHelper;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListFragment newInstance(String param1, String param2) {
        PlayListFragment fragment = new PlayListFragment();
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
        return inflater.inflate(R.layout.fragment_play_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView =view.findViewById(R.id.listView);

        arrayList=dbHelper.getALLPlayList();
        playListAdapter = new PlayListAdapter(getContext(),arrayList);
        listView.setAdapter(playListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long
                    l) {

                Intent intent = new Intent(getActivity(), SongOfPlaylistActivity.class);
                intent.putExtra("Playlist",i);
                intent.putExtra("id",arrayList.get(i).getIdPlayList());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                new AlertDialog.Builder(PlayListFragment.this.getContext()).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Xóa Playlist")
                        .setMessage("Bạn muốn xóa Playlist này?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dbHelper.deletePlayList(arrayList.get(i).getIdPlayList());
                                arrayList.remove(i);
                                playListAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Hủy",null)
                        .show();

                return false;
            }
        });

    }


}
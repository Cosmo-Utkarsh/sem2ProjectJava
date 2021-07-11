package com.example.musicplayerdraft;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class NowPlayingBtmFrag extends Fragment {
    ImageView nextbtn,art;
    TextView songname;
    FloatingActionButton playpausebtn;
    View view;

    public NowPlayingBtmFrag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_now_playing_btm, container, false);
        songname= view.findViewById(R.id.Btm_song_name);
        nextbtn= view.findViewById(R.id.Btm_skip_next);
        art = view.findViewById(R.id.Btm_art);
        playpausebtn = view.findViewById(R.id.Btm_playPause);
        return view;
    }
}
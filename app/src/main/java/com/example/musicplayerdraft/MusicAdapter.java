package com.example.musicplayerdraft;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mcontext;
    private ArrayList<MusicFiles> mFiles;

    MusicAdapter(Context mcontext, ArrayList<MusicFiles> mFiles){
        this.mcontext= mcontext;
        this.mFiles= mFiles;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.music_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MusicAdapter.MyViewHolder holder, int position) {
        holder.file_name.setText(mFiles.get(position).getTitle());
        byte[] image = getAlbumArt(mFiles.get(position).getPath());
        if(image != null){
            Glide.with(mcontext).asBitmap().load(image).into(holder.album_art);
        }
        else{
            Glide.with(mcontext).load(R.drawable.nice).into(holder.album_art);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mcontext,PlayerActivity.class);
                intent.putExtra("position",position);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView file_name;
        ImageView album_art;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name= itemView.findViewById(R.id.music_file_name);
            album_art= itemView.findViewById(R.id.music_img);
        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}

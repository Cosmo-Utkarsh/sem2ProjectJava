package com.example.musicplayerdraft;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.example.musicplayerdraft.PlayerActivity.listofsongs;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    IBinder mbinder= new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    Uri uri;
    int position=-1;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("bind","method");
        return mbinder;
    }



    public class MyBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myposition = intent.getIntExtra("serviceposition",-1);
        if(myposition!=-1){
            playMedia(myposition);
        }
        return START_STICKY;
    }

    private void playMedia(int Startposition) {
        musicFiles = listofsongs;
        position = Startposition;
        if(mediaPlayer !=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(musicFiles!=null){
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        }
        else{
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    void start(){
        mediaPlayer.start();
    }
    boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    void stop(){
        mediaPlayer.stop();
    }
    void release(){
        mediaPlayer.release();
    }
    int getDuration(){
        return mediaPlayer.getDuration();
    }
    void seekto(int position){
        mediaPlayer.seekTo(position);
    }
    int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }
    void pause(){
        mediaPlayer.pause();
    }
    void createMediaPlayer(int positon){
        uri = Uri.parse(musicFiles.get(positon).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(),uri);
    }
    void OnCompleted(){
        mediaPlayer.setOnCompletionListener(this);
    }
    @Override
    public void onCompletion(MediaPlayer mp) {

    }
    void seekTo(int position){
        mediaPlayer.seekTo(position);
    }
}

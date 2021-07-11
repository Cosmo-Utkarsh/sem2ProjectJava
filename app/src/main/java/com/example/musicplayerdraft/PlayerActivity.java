package com.example.musicplayerdraft;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.example.musicplayerdraft.MainActivity.musicFiles;
import static com.example.musicplayerdraft.MainActivity.repeatboolean;
import static com.example.musicplayerdraft.MainActivity.shuffleboolean;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,ActionPlaying,ServiceConnection{

    TextView song_name,artist_name,duration_played,duration_total;
    ImageView cover_art,nextbtn,prevbtn,shufflebtn,repeatbtn;
    FloatingActionButton playpausebtn;
    SeekBar seekBar;
    int position = -1;
    static ArrayList<MusicFiles> listofsongs;
    static Uri uri;
    static MediaPlayer musicPlayer;
    private Handler handler = new Handler();
    private Thread playThread,prevThread,nextThread;
    //MusicService musicService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();
        song_name.setText(listofsongs.get(position).getTitle());
        artist_name.setText(listofsongs.get(position).getArtist());
        musicPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(musicPlayer!=null&& fromUser){
                    musicPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(musicPlayer!=null){
                    int mCurrentPosition= musicPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
        shufflebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleboolean){
                    shuffleboolean=false;
                    shufflebtn.setImageResource(R.drawable.ic_shuffle_on);
                }
                else{
                    shuffleboolean=true;
                    shufflebtn.setImageResource(R.drawable.ic_shuffle_off);
                }
            }
        });
        repeatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatboolean){
                    repeatboolean=false;
                    repeatbtn.setImageResource(R.drawable.ic_repeat_on);
                }
                else{
                    repeatboolean= true;
                    repeatbtn.setImageResource(R.drawable.ic_repeat_off);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        playThreadbtn();
        pervThreadbtn();
        nextThreadbtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void nextThreadbtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextbtnclicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    public void nextbtnclicked() {
        if(musicPlayer.isPlaying()){
            musicPlayer.stop();
            musicPlayer.release();
            if(shuffleboolean && !repeatboolean){
                position= getRandom(listofsongs.size()-1);
            }else if(!shuffleboolean && !repeatboolean){
                position=((position+1)%listofsongs.size());
            }
            uri = Uri.parse(listofsongs.get(position).getPath());
            musicPlayer = MediaPlayer.create(getBaseContext(),uri);
            metadeta(uri);
            song_name.setText(listofsongs.get(position).getTitle());
            artist_name.setText(listofsongs.get(position).getArtist());
            seekBar.setMax(musicPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicPlayer!=null){
                        int mCurrentPosition= musicPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicPlayer.setOnCompletionListener(this);
            playpausebtn.setBackgroundResource(R.drawable.ic_pause);
            musicPlayer.start();
        }
        else{
            musicPlayer.stop();
            musicPlayer.release();
            if(shuffleboolean && !repeatboolean){
                position= getRandom(listofsongs.size()-1);
            }else if(!shuffleboolean && !repeatboolean){
                position=((position+1)%listofsongs.size());
            }
            uri = Uri.parse(listofsongs.get(position).getPath());
            musicPlayer= MediaPlayer.create(getBaseContext(),uri);
            metadeta(uri);
            song_name.setText(listofsongs.get(position).getTitle());
            artist_name.setText(listofsongs.get(position).getArtist());
            seekBar.setMax(musicPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicPlayer!=null){
                        int mCurrentPosition= musicPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicPlayer.setOnCompletionListener(this);
            playpausebtn.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i+1);
    }

    private void pervThreadbtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                prevbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pervbtnclicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    public void pervbtnclicked() {
        if(musicPlayer.isPlaying()){
            musicPlayer.stop();
            musicPlayer.release();
            position=((position-1)<0?(listofsongs.size()-1):(position-1));
            uri = Uri.parse(listofsongs.get(position).getPath());
            musicPlayer = MediaPlayer.create(getBaseContext(),uri);
            metadeta(uri);
            song_name.setText(listofsongs.get(position).getTitle());
            artist_name.setText(listofsongs.get(position).getArtist());
            seekBar.setMax(musicPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicPlayer!=null){
                        int mCurrentPosition= musicPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicPlayer.setOnCompletionListener(this);
            playpausebtn.setBackgroundResource(R.drawable.ic_pause);
            musicPlayer.start();
        }
        else{
            musicPlayer.stop();
            musicPlayer.release();
            position=((position-1)<0?(listofsongs.size()-1):(position-1));
            uri = Uri.parse(listofsongs.get(position).getPath());
            musicPlayer = MediaPlayer.create(getBaseContext(),uri);
            metadeta(uri);
            song_name.setText(listofsongs.get(position).getTitle());
            artist_name.setText(listofsongs.get(position).getArtist());
            seekBar.setMax(musicPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicPlayer!=null){
                        int mCurrentPosition= musicPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicPlayer.setOnCompletionListener(this);
            playpausebtn.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void playThreadbtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playpausebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playpausebtnclicked();
                    }
                });
            }
        };
        playThread.start();
    }

    public void playpausebtnclicked() {
        if(musicPlayer.isPlaying()){
            playpausebtn.setImageResource(R.drawable.ic_play);
            musicPlayer.pause();
            seekBar.setMax(musicPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicPlayer!=null){
                        int mCurrentPosition= musicPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
        else {
            playpausebtn.setImageResource(R.drawable.ic_pause);
            musicPlayer.start();
            seekBar.setMax(musicPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicPlayer!=null){
                        int mCurrentPosition= musicPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition/60);
        totalOut= minutes+":"+seconds;
        totalNew = minutes+":"+"0"+seconds;
        if(seconds.length()==0){
            return totalNew;
        }
        else{
            return totalOut;
        }

    }

    private void getIntentMethod() {
        position= getIntent().getIntExtra("position",-1);
        listofsongs = musicFiles;
        if(listofsongs!=null){
            playpausebtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listofsongs.get(position).getPath());
        }
        if(musicPlayer!=null){
            musicPlayer.stop();
            musicPlayer.release();
        }
        musicPlayer = MediaPlayer.create(getBaseContext(),uri);
        musicPlayer.start();

        seekBar.setMax(musicPlayer.getDuration()/1000);
        metadeta(uri);
    }

    private void initViews() {
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.duration);
        duration_total = findViewById(R.id.durationTotal);
        cover_art = findViewById(R.id.cover_art);
        nextbtn = findViewById(R.id.skip_next);
        prevbtn = findViewById(R.id.skip_previous);
        shufflebtn = findViewById(R.id.shuffle);
        repeatbtn = findViewById(R.id.repeat);
        playpausebtn = findViewById(R.id.play_pause);
        seekBar= findViewById(R.id.seek_bar);
    }
    private void metadeta(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listofsongs.get(position).getDuration())/1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if(art != null){
            bitmap = BitmapFactory.decodeByteArray(art,0,art.length);
            ImageAnimation(this,cover_art,bitmap);


        }
        else{
            Glide.with(this).asBitmap().load(R.drawable.nice).into(cover_art);
            ImageView gradient = findViewById(R.id.imageViewGradient);
            RelativeLayout mContainer = findViewById(R.id.mContainer);
            gradient.setBackgroundResource(R.drawable.gradient_bg);
            mContainer.setBackgroundResource(R.drawable.main_bg);
            song_name.setTextColor(Color.WHITE);
            artist_name.setTextColor(Color.DKGRAY);
        }
    }
    public void ImageAnimation(Context context, ImageView imageView, Bitmap bitmap ){
        Animation animout = AnimationUtils.loadAnimation(context,android.R.anim.fade_out);
        Animation animin = AnimationUtils.loadAnimation(context , android.R.anim.fade_in);
        animout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animin.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animout);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextbtnclicked();
        if(musicPlayer!=null){
            musicPlayer = MediaPlayer.create(getBaseContext(),uri);
            musicPlayer.start();
            musicPlayer.setOnCompletionListener(this);
        }
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
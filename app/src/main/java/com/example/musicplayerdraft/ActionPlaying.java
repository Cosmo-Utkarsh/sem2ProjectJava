package com.example.musicplayerdraft;

import android.media.MediaPlayer;

public interface ActionPlaying {

    void pervbtnclicked();
    void playpausebtnclicked();
    void nextbtnclicked();

    void onCompletion(MediaPlayer mp);
}

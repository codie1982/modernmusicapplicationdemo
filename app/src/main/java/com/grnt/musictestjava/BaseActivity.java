package com.grnt.musictestjava;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.grnt.musictestjava.feature.PlayerFeature;
import com.grnt.musictestjava.ui.PlayerActivity;
import com.grnt.musictestjava.viewmodel.MediaViewModel;

public class BaseActivity extends AppCompatActivity {
    private MediaViewModel mediaViewModel;
    protected LinearLayout playerSection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "CHANNEL_ID",
                    "Music Player",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }

    public void setPlayer(){
       //playerSection.addView(new PlayerFeature(this));
    }
}

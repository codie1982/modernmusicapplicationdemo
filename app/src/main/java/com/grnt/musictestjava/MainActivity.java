package com.grnt.musictestjava;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.grnt.musictestjava.services.MyMusicService;

public class MainActivity extends AppCompatActivity {
    private MediaBrowserCompat mediaBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "CHANNEL_ID",
                    "Music Player",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Button playButton = findViewById(R.id.play_button);
        Button pauseButton = findViewById(R.id.pause_button);
        mediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MyMusicService.class),
                new MediaBrowserCompat.ConnectionCallback() {
                    @Override
                    public void onConnected() {
                        try {
                            MediaControllerCompat mediaController = new MediaControllerCompat(MainActivity.this, mediaBrowser.getSessionToken());
                            MediaControllerCompat.setMediaController(MainActivity.this, mediaController);
                            updateUI();
                            mediaController.registerCallback(controllerCallback);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnectionSuspended() {
                        // The Service has crashed. Disable transport controls until it automatically reconnects
                    }

                    @Override
                    public void onConnectionFailed() {
                        // The Service has refused our connection
                    }
                }, null);
        playButton.setOnClickListener(v -> {
                    String url = "https://rr2---sn-4g5e6nsy.googlevideo.com/videoplayback?expire=1719194578&ei=cn94ZqWGD5rj6dsP1MiIiAk&ip=142.132.205.100&id=o-AFgNDnZ6UsVI1uS_xNL7xzpPr25vKhsg74Yos-TeJWKH&itag=140&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&vprv=1&svpuc=1&mime=audio%2Fmp4&rqh=1&gir=yes&clen=1081309&dur=66.757&lmt=1676287343425989&keepalive=yes&c=ANDROID_TESTSUITE&txp=5432434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRQIhAPUxXBgTtt0pqgtaYjvG03pgoNjWTbCuDf9UtTddMvaXAiBt0TT3H5aArKWrkd5feTFbcwPdoeNecmlYChB3MtIM7Q%3D%3D&rm=sn-4g5ekz7l&fexp=24350485&req_id=7da21620e91aa3ee&ipbypass=yes&cm2rm=sn-u0g3uxax3-pnul7d,sn-hgnly7e&redirect_counter=3&cms_redirect=yes&cmsv=e&mh=oK&mip=78.181.37.209&mm=34&mn=sn-4g5e6nsy&ms=ltu&mt=1719172834&mv=m&mvi=2&pl=24&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AHlkHjAwRQIhAOWQ4THnu7Dq4lo38Wej5NxsGUtHOKdwgQEEO-9IGGrVAiAuXRNkJrSj88zYYPirSzjPQBNidznY9JQ-0saDQNSnng%3D%3D";
                    if (!url.isEmpty()) {
                        Intent intent = new Intent(MainActivity.this, MyMusicService.class);
                        intent.setAction("ACTION_PLAY");
                        intent.putExtra("URL", url);
                        startService(intent);
                    }
                }
        );

        pauseButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(MainActivity.this, MyMusicService.class);
            intent.setAction("ACTION_PAUSE");
            startService(intent);
        });
    }

    private void updateUI() {
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(MainActivity.this);
        if (mediaController != null) {
            PlaybackStateCompat state = mediaController.getPlaybackState();
            if (state != null && state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                //playPauseButton.setText("Pause");
            } else {
                //playPauseButton.setText("Play");
            }

            MediaMetadataCompat metadata = mediaController.getMetadata();
            if (metadata != null) {
                //songTitle.setText(metadata.getDescription().getTitle());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaBrowser.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MediaControllerCompat.getMediaController(this) != null) {
            MediaControllerCompat.getMediaController(this).unregisterCallback(controllerCallback);
        }
        mediaBrowser.disconnect();
    }

    private MediaControllerCompat.Callback controllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            // Update your UI based on playback state
        }
    };
}
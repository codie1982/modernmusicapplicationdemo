package com.grnt.musictestjava.ui;

import android.content.ComponentName;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.grnt.musictestjava.R;
import com.grnt.musictestjava.databinding.ActivityPlayerBinding;
import com.grnt.musictestjava.services.MyMusicService;

public class PlayerActivity extends AppCompatActivity {
    private MediaBrowserCompat mediaBrowser;
    private TextView songTitle;
    private Button playPauseButton, nextButton, previousButton;
    private AppBarConfiguration appBarConfiguration;
    private ActivityPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        songTitle = binding.songTitle;
        playPauseButton = binding.playPauseButton;
        previousButton = binding.previousButton;
        nextButton = binding.nextButton;

        mediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MyMusicService.class),
                new MediaBrowserCompat.ConnectionCallback() {

                    @Override
                    public void onConnected() {
                        super.onConnected();
                        MediaControllerCompat controllerCompat = new MediaControllerCompat(PlayerActivity.this, mediaBrowser.getSessionToken());
                        MediaControllerCompat.setMediaController(PlayerActivity.this, controllerCompat);
                        updateUI();
                        controllerCompat.registerCallback(controllerCallback);
                    }

                    @Override
                    public void onConnectionSuspended() {
                        super.onConnectionSuspended();
                    }

                    @Override
                    public void onConnectionFailed() {
                        super.onConnectionFailed();
                    }
                }, null);

        playPauseButton.setOnClickListener(v -> {

           /* Song song = songList.get(position);
            MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(PlayerActivity.this);
            mediaController.getTransportControls().playFromMediaId(song.getId(), null);*/

            MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(PlayerActivity.this);
            PlaybackStateCompat state = mediaController.getPlaybackState();
            if (state != null && state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mediaController.getTransportControls().pause();
            } else {
                mediaController.getTransportControls().play();
            }
        });

        nextButton.setOnClickListener(v -> {
            MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(PlayerActivity.this);
            mediaController.getTransportControls().skipToNext();
        });

        previousButton.setOnClickListener(v -> {
            MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(PlayerActivity.this);
            mediaController.getTransportControls().skipToPrevious();
        });
    }

    private MediaControllerCompat.Callback controllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            // Update your UI based on playback state
            updateUI();
        }
        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            updateUI();
        }
    };
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

    private void updateUI() {
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(PlayerActivity.this);
        if (mediaController != null) {
            PlaybackStateCompat state = mediaController.getPlaybackState();
            if (state != null && state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                playPauseButton.setText("Pause");
            } else {
                playPauseButton.setText("Play");
            }

            MediaMetadataCompat metadata = mediaController.getMetadata();
            if (metadata != null) {
                songTitle.setText(metadata.getDescription().getTitle());
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_player);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();*/
        return super.onSupportNavigateUp();
    }
}
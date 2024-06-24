package com.grnt.musictestjava.services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.IBinder;
import android.service.media.MediaBrowserService;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.MediaBrowserServiceCompat;
import androidx.media.session.MediaButtonReceiver;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;


import com.grnt.musictestjava.MainActivity;
import com.grnt.musictestjava.R;

import java.util.List;

public class MyMusicService extends MediaBrowserServiceCompat {
    private static MediaSessionCompat mediaSession;
    private ExoPlayer player;

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSessionCompat(this, "MyMusicService");
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                startService(new Intent(getApplicationContext(), MyMusicService.class));
                mediaSession.setActive(true);
                player.play();
                updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
                showNotification();
            }

            @Override
            public void onPause() {
                super.onPause();
                player.pause();
                updatePlaybackState(PlaybackStateCompat.STATE_PAUSED);
                showNotification();
            }

            @Override
            public void onStop() {
                super.onStop();
                player.stop();
                mediaSession.setActive(false);
                stopSelf();
                updatePlaybackState(PlaybackStateCompat.STATE_STOPPED);
                stopSelf();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                // Logic to skip to the next track
                updatePlaybackState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT);
                showNotification();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                // Logic to skip to the previous track
                updatePlaybackState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS);
                showNotification();
            }
        });

        setSessionToken(mediaSession.getSessionToken());
        player = new ExoPlayer.Builder(this).build();
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.Listener.super.onPlayerStateChanged(playWhenReady, playbackState);
                if (playbackState == Player.STATE_ENDED) {
                    mediaSession.getController().getTransportControls().stop();
                }
            }
        });
    }



    private void updatePlaybackState(int state) {
        PlaybackStateCompat playbackState = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_STOP)
                .setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1)
                .build();
        mediaSession.setPlaybackState(playbackState);
    }

    private void showNotification() {
        MediaControllerCompat mediaController = mediaSession.getController();
        MediaMetadataCompat metadata = mediaController.getMetadata();
        PlaybackStateCompat state = mediaController.getPlaybackState();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle(metadata.getDescription().getTitle())
                .setContentText(metadata.getDescription().getSubtitle())
                .setSmallIcon(R.drawable.music_note_icon)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .addAction(new NotificationCompat.Action(
                        R.drawable.previous_icon,
                        "Previous",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)))
                .addAction(new NotificationCompat.Action(
                        state.getState() == PlaybackStateCompat.STATE_PLAYING ? R.drawable.pause_icon : R.drawable.play_icon,
                        state.getState() == PlaybackStateCompat.STATE_PLAYING ? "Pause" : "Play",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)))
                .addAction(new NotificationCompat.Action(
                        R.drawable.next_icon,
                        "Next",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
    public static MediaSessionCompat getMediaSession() {
        return mediaSession;
    }
    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new BrowserRoot("root",null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "ACTION_PLAY":
                    String url = intent.getStringExtra("URL");
                    if (url != null) {
                        MediaItem mediaItem = MediaItem.fromUri(url);
                        player.setMediaItem(mediaItem);
                        player.prepare();

                        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "id")
                                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "title")
                                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "artist")
                                .build();
                        mediaSession.setMetadata(metadata);

                        mediaSession.getController().getTransportControls().play();
                    }
                    break;
                case "ACTION_PAUSE":
                    mediaSession.getController().getTransportControls().pause();
                    break;
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaSession.release();
        player.release();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}

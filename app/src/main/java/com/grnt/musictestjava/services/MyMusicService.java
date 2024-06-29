package com.grnt.musictestjava.services;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
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
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;


import com.grnt.musictestjava.R;
import com.grnt.musictestjava.util.SongManager;
import com.grnt.musictestjava.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MyMusicService extends MediaBrowserServiceCompat {
    private static MediaSessionCompat mediaSession;
    private ExoPlayer player;
    private SongManager songManager;

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onCreate() {
        super.onCreate();
        songManager = new SongManager();
        mediaSession = new MediaSessionCompat(this, "MyMusicService");
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                Song song = songManager.getSongs().get(0);
                if (song != null) {
                    player.prepare();
                    player.play();
                    mediaSession.setActive(true);
                    updateMetadata(song);
                    updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
                    showNotification();
                }
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
                updatePlaybackState(PlaybackStateCompat.STATE_STOPPED);
                stopSelf();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                // Logic to skip to the next track
                player.seekToNext();
                MediaItem item  = player.getCurrentMediaItem();
                Song _song = songManager.getSongById(item.mediaId);
                updateMetadata(_song);

                updatePlaybackState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT);
                showNotification();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                // Logic to skip to the previous track
                player.seekToPrevious();
                MediaItem item  = player.getCurrentMediaItem();
                Song _song = songManager.getSongById(item.mediaId);
                updateMetadata(_song);

                updatePlaybackState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS);
                showNotification();
            }
            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
                player.seekTo(pos);
                updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
            }
            @Override
            public void onPlayFromMediaId(String mediaId, Bundle extras) {
                super.onPlayFromMediaId(mediaId, extras);
                Song song = songManager.getSongById(mediaId);
                if (song != null) {
                    MediaItem mediaItem = MediaItem.fromUri(song.getUrl());
                    player.setMediaItem(mediaItem);
                    player.prepare();
                    player.play();
                    updateMetadata(song);
                }
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

    private void updateMetadata(Song song) {
        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, song.getId())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getArtist())
                .build();
        mediaSession.setMetadata(metadata);
        //showNotification();
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
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        for (Song song : songManager.getSongs()) {
            MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
                    .setMediaId(song.getId())
                    .setTitle(song.getTitle())
                    .setSubtitle(song.getArtist())
                    .build();
            mediaItems.add(new MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
        }
        //result.sendResult(mediaItems);
        result.sendResult(null); // Loa
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "ACTION_PREPARE":
                    Bundle bundle2 = intent.getExtras();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        assert bundle2 != null;
                        songManager = bundle2.getSerializable("manager",SongManager.class);
                    }
                    addSongs(songManager.getSongs());
                    break;
                case "ACTION_PLAY":
                    Bundle bundle = intent.getExtras();
                    Song song = songManager.getSongs().get(0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        assert bundle != null;
                        song = bundle.getSerializable("song",Song.class);
                    }
                    updateMetadata(song);
                    MediaItem mediaItem = MediaItem.fromUri(song.getUrl());
                        player.setMediaItem(mediaItem);
                        player.prepare();
                        mediaSession.getController().getTransportControls().play();
                    break;
                case "ACTION_PAUSE":
                    mediaSession.getController().getTransportControls().pause();
                    break;
            }
        }

        return START_STICKY;
    }
    public void addSongs(ArrayList<Song> songs) {
        for (Song song : songs) {
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(Uri.parse(song.getUrl()))
                    .setMediaId(song.getId())
                    .setMediaMetadata(new MediaMetadata.Builder()
                            .setTitle(song.getTitle())
                            .setArtist(song.getArtist())
                            .build())
                    .build();
            player.addMediaItem(mediaItem);
        }
        player.prepare();
        // Notify any active MediaBrowser clients to reload the media items
        mediaSession.sendSessionEvent("mediaItemsUpdated", null);
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

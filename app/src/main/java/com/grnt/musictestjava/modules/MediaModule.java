package com.grnt.musictestjava.modules;

import android.content.ComponentName;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;

import com.grnt.musictestjava.services.MyMusicService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MediaModule {

    @Provides
    @Singleton
    public MediaBrowserCompat provideMediaBrowser(@ApplicationContext Context context) {
        return new MediaBrowserCompat(context,
                new ComponentName(context, MyMusicService.class),
                new MediaBrowserCompat.ConnectionCallback() {
                    @Override
                    public void onConnected() {
                        // Connection successful
                    }

                    @Override
                    public void onConnectionSuspended() {
                        // Connection suspended
                    }

                    @Override
                    public void onConnectionFailed() {
                        // Connection failed
                    }
                }, null);
    }
    @Provides
    @Singleton
    public MediaControllerCompat provideMediaController(@ApplicationContext Context context, MediaBrowserCompat mediaBrowser) {
        MediaControllerCompat mediaController = null;
        try {
            mediaController = new MediaControllerCompat(context, mediaBrowser.getSessionToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaController;
    }
}

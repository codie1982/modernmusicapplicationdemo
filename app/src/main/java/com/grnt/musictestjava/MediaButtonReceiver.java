package com.grnt.musictestjava;

import android.content.Context;
import android.content.Intent;

import com.grnt.musictestjava.services.MyMusicService;

public class MediaButtonReceiver extends androidx.media.session.MediaButtonReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        MediaButtonReceiver.handleIntent(MyMusicService.getMediaSession(), intent);

    }
}

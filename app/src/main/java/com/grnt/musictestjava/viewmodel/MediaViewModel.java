package com.grnt.musictestjava.viewmodel;

import android.support.v4.media.session.MediaControllerCompat;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MediaViewModel extends ViewModel {

    private final MediaControllerCompat mediaController;

    @Inject
    public MediaViewModel(MediaControllerCompat mediaController) {
        this.mediaController = mediaController;
    }

    public MediaControllerCompat getMediaController() {
        return mediaController;
    }
}

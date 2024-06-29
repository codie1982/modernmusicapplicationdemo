package com.grnt.musictestjava.mvvm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.grnt.musictestjava.model.Album

class AlbumModelView: ViewModel() {
    private val albumRepository:AlbumRepository = AlbumRepository()
    val albumList: LiveData<List<Album>> = albumRepository.getAlbum()
}
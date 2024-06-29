package com.grnt.musictestjava.mvvm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.grnt.musictestjava.model.Album

class AlbumRepository {
    val firestore : FirebaseFirestore = FirebaseFirestore.getInstance();
    private val albumCollection = firestore.collection("album")
    fun getAlbum(): LiveData<List<Album>> {
        val albumLiveData = MutableLiveData<List<Album>>()

        albumCollection.get().addOnSuccessListener { result ->
            val albumList = mutableListOf<Album>()
            for (document in result) {
                val album = document.toObject(Album::class.java)
                albumList.add(album)
            }
            albumLiveData.value = albumList
        }
        return albumLiveData
    }

}
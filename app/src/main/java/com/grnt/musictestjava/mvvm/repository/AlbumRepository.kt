package com.grnt.musictestjava.mvvm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User
import com.grnt.musictestjava.model.Album


private const val ALBUM = "Album"

class AlbumRepository {
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance();
    private val albumCollection = firestore.collection(ALBUM)

    fun getAlbum(): Task<List<Album>> {
        return albumCollection.get().continueWith { task: Task<QuerySnapshot> ->
            val albumList: MutableList<Album> = ArrayList()
            if (task.isSuccessful) {
                for (document in task.result) {
                    val album =   document.let {
                       Album(
                            document.id,
                            document.getString("albumName")!!,
                            document.getString("artistName")!!,
                            document.getString("category")!!
                        )
                    }
                    if (album != null) {
                        albumList.add(album)
                    }
                }

            }
            albumList
        }
    }
}


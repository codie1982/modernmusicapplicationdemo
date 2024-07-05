package com.grnt.musictestjava.mvvm.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.grnt.musictestjava.model.Album;
import com.grnt.musictestjava.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicRepository {
    public static  FirebaseFirestore firebase_instance;
    private final  String ALBUMCOLLECTION = "Album";
    private final  String SONGCOLLECTION = "Song";
    FirebaseFirestore firestore;

    public MusicRepository() {
        if(firebase_instance == null){
            firebase_instance = FirebaseFirestore.getInstance();
        }
        firestore = firebase_instance;
    }

    public Task<List<Album>> getAlbums() {
        CollectionReference albumCollection = firestore.collection(ALBUMCOLLECTION);
        return albumCollection.get().continueWith(task -> {
                    List<Album> albumList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Album album = new Album(
                                    document.getId(),
                                    document.getString("albumName"),
                                    document.getString("artistName"),
                                    document.getString("category"),
                                    document.getString("imageurl")


                        );
                            Album album2 = new Album(
                                    document.getId(),
                                    document.getString("albumName"),
                                    document.getString("artistName"),
                                    document.getString("category"),
                                    document.getString("imageurl")


                            );
                            albumList.add(album);
                            albumList.add(album2);
                            albumList.add(album);
                            albumList.add(album2);
                            albumList.add(album);
                            albumList.add(album2);
                        }
                    }
                    return albumList;
                });
    }

    public Task<List<Song>> getSongsByAlbumId(String albumId) {
        CollectionReference albumCollection = firestore.collection(SONGCOLLECTION);
        return albumCollection
                .whereEqualTo("albumid", albumId)
                .get()
                .continueWith(task -> {
                    List<Song> songList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Song song =  new Song(
                                    document.getId(),
                                    document.getString("albumid"),
                                    document.getString("name"),
                                    document.getString("artist"),
                                    document.getString("streamurl")
                            );
                            songList.add(song);
                        }
                    }
                    return songList;
                });
    }

}

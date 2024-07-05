package com.grnt.musictestjava.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.grnt.musictestjava.model.Album;
import com.grnt.musictestjava.model.Song;
import com.grnt.musictestjava.mvvm.repository.MusicRepository;

import java.util.List;

public class MusicViewModel extends ViewModel {
    private final MusicRepository musicRepository;
    private MutableLiveData<List<Album>> albumList;
    private MutableLiveData<List<Song>> songlist;
    private MutableLiveData<Boolean> isLoading;

    public MusicViewModel() {
        this.musicRepository = new MusicRepository();
        albumList = new MutableLiveData<>();
        songlist    = new MutableLiveData<>();
        isLoading   = new MutableLiveData<>();
    }
    public LiveData<List<Album>> getAlbums() {
        return albumList;
    }

    public LiveData<List<Song>> getSongs() {
        return songlist;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public void loadAlbums() {
        isLoading.setValue(true);
        musicRepository.getAlbums().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                albumList.setValue(task.getResult());
            }else {
                albumList.setValue(null);
            }
            isLoading.setValue(false);
        });
    }

    public void loadSongsFromAblumId(String albumid){
        isLoading.setValue(true);
        musicRepository.getSongsByAlbumId(albumid).addOnCompleteListener( task->{
           if(task.isSuccessful()){
               songlist.setValue(task.getResult());
           }else {
               songlist.setValue(null);
           }
           isLoading.setValue(false);
        });
    }
}

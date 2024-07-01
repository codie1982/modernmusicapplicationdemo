package com.grnt.musictestjava.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.grnt.musictestjava.model.Album;
import com.grnt.musictestjava.mvvm.repository.AlbumRepository;
import java.util.List;

public class AlbumViewModel extends ViewModel {

    private AlbumRepository albumRepository;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<Album>> albumList;

    AlbumViewModel() {
        albumRepository = new AlbumRepository();
        isLoading = new MutableLiveData<>();
        albumList = new MutableLiveData<>();
    }

    public LiveData<List<Album>> getAlbumList() {
        loadAlbums();
        return albumList;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadAlbums() {
        isLoading.setValue(true);
        albumRepository.getAlbum().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                albumList.setValue(task.getResult());
            } else {
                isLoading.setValue(false);
            }
            isLoading.setValue(false);
        });
    }
}

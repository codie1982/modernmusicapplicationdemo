package com.grnt.musictestjava.ui;

import static com.grnt.musictestjava.constant.AlbumConstant.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grnt.musictestjava.R;
import com.grnt.musictestjava.adapter.album.AlbumAdapter;
import com.grnt.musictestjava.adapter.album.IAlbumAdapter;
import com.grnt.musictestjava.databinding.FragmentAlbumBinding;
import com.grnt.musictestjava.model.Album;
import com.grnt.musictestjava.mvvm.MusicViewModel;

import java.util.List;

public class AlbumFragment extends Fragment implements IAlbumAdapter {
    private MusicViewModel musicViewModel;
    private FragmentAlbumBinding binding;
    private AlbumAdapter albumAdapter;
    private RecyclerView rvAlbumList;
private List<Album> albumList;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        rvAlbumList = binding.rvAlbum;
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);
        musicViewModel.loadAlbums();
        musicViewModel.getAlbums().observe(getViewLifecycleOwner(), albums -> {
            albumList = albums;
            albumAdapter = new AlbumAdapter(albums,getContext(),this);
            rvAlbumList.setAdapter(albumAdapter);
            rvAlbumList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        });
        musicViewModel.getIsLoading().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.loading.setVisibility(View.VISIBLE);
            } else {
                binding.loading.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAlbumClick(Album selectedAlbum) {
        Bundle bundle = new Bundle();
        bundle.putString(ALBUMID,selectedAlbum.getAlbumid());
        bundle.putString(ALBUMNAME,selectedAlbum.getAlbumName());
        bundle.putString(ARTISTNAME,selectedAlbum.getArtistName());
        bundle.putString(CATEGORY,selectedAlbum.getCategory());
        bundle.putString(IMAGEURL,selectedAlbum.getImageurl());
        NavHostFragment.findNavController(AlbumFragment.this).navigate(R.id.action_AlbumFragment_to_SongListFragment,bundle);
    }
}
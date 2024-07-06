package com.grnt.musictestjava.ui;

import static com.grnt.musictestjava.constant.AlbumConstant.ALBUMID;
import static com.grnt.musictestjava.constant.AlbumConstant.ALBUMNAME;
import static com.grnt.musictestjava.constant.AlbumConstant.ARTISTNAME;
import static com.grnt.musictestjava.constant.AlbumConstant.CATEGORY;
import static com.grnt.musictestjava.constant.AlbumConstant.IMAGEURL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grnt.musictestjava.BaseFragment;
import com.grnt.musictestjava.R;
import com.grnt.musictestjava.adapter.song.ISongAdapter;
import com.grnt.musictestjava.adapter.song.SongAdapter;
import com.grnt.musictestjava.databinding.FragmentSonglistBinding;
import com.grnt.musictestjava.feature.PlayerFeature;
import com.grnt.musictestjava.model.Album;
import com.grnt.musictestjava.model.Song;
import com.grnt.musictestjava.mvvm.MusicViewModel;

import java.util.List;
import java.util.Objects;

public class SongListFragment extends BaseFragment implements ISongAdapter {

    LinearLayout playerSection;
    private FragmentSonglistBinding binding;
    private Album selectedAlbum;
    private ImageView imgAlbumImage;
    private TextView txtCategoryName,txtAlbumName,txtArtistName;

    private SongAdapter songAdapter;
    private RecyclerView rvSongList;
    private MusicViewModel musicViewModel;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSonglistBinding.inflate(inflater, container, false);
        imgAlbumImage = binding.imgAlbumImage;
        txtCategoryName = binding.txtCategoryName;
        txtAlbumName = binding.txtAlbumName;
        txtArtistName = binding.txtArtistName;

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle !=null){
            selectedAlbum = new Album(Objects.requireNonNull(bundle != null ? bundle.getString(ALBUMID) : null),
                    Objects.requireNonNull(bundle.getString(ALBUMNAME)),
                    Objects.requireNonNull(bundle.getString(ARTISTNAME)),
                    Objects.requireNonNull(bundle.getString(CATEGORY)),
                    Objects.requireNonNull(bundle.getString(IMAGEURL))
            );

            Glide.with(getContext()).load(selectedAlbum.getImageurl()).into(imgAlbumImage);
            txtAlbumName.setText(selectedAlbum.getAlbumName());
            txtArtistName.setText(selectedAlbum.getArtistName());
            txtCategoryName.setText(selectedAlbum.getCategory());
            rvSongList = binding.rvSongList;

            musicViewModel =new ViewModelProvider(this).get(MusicViewModel.class);
            musicViewModel.loadSongsFromAblumId(selectedAlbum.getAlbumid());
            musicViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                @Override
                public void onChanged(List<Song> songs) {
                    songAdapter = new SongAdapter(songs,SongListFragment.this);
                    rvSongList.setAdapter(songAdapter);
                    rvSongList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }
            });

            musicViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        binding.loading.setVisibility(View.VISIBLE);
                    } else {
                        binding.loading.setVisibility(View.GONE);
                    }
                }
            });
        }


        binding.buttonSecond.setOnClickListener(v ->
                NavHostFragment.findNavController(SongListFragment.this)
                        .navigate(R.id.action_SongListFragment_to_AlbumFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClickItem() {
        baseActivity.setPlayer();
    }
}
package com.grnt.musictestjava.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.grnt.musictestjava.databinding.FragmentFirstBinding;
import com.grnt.musictestjava.mvvm.AlbumViewModel;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private AlbumViewModel albumViewModel;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* binding.buttonFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        );*/
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);
        albumViewModel.getAlbumList().observe(getViewLifecycleOwner(), albums -> {
            System.out.println("Albums " + albums);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
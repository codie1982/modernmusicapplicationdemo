package com.grnt.musictestjava.ui;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.grnt.musictestjava.BaseFragment;
import com.grnt.musictestjava.R;

public class BaseMotionFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         return inflater.inflate(R.layout.base_frame_layout,container,false);

    }
}

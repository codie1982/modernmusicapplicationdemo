package com.grnt.musictestjava;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    protected BaseActivity baseActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();
    }
}

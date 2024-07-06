package com.grnt.musictestjava.feature;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.grnt.musictestjava.R;

public class PlayerFeature  extends LinearLayout {

    public PlayerFeature(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fragment_player,this,true);
    }

    public PlayerFeature(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_player,this,true);
    }
}

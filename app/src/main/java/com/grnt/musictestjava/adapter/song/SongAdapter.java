package com.grnt.musictestjava.adapter.song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grnt.musictestjava.R;
import com.grnt.musictestjava.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    List<Song> songs = new ArrayList<>();
    ISongAdapter iSongAdapter;
    public SongAdapter(List<Song> songs,ISongAdapter iSongAdapter){
        this.songs = songs;
        this.iSongAdapter = iSongAdapter;
    }
    public SongAdapter(){
    }
    @NonNull
    @Override
    public SongAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item,parent,false);
        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongViewHolder holder, int position) {
        holder.txtSongName.setText(songs.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iSongAdapter.onClickItem();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void updateSongList(List<Song> songs){
        this.songs.clear();
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        TextView txtSongName;
        LinearLayout section;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSongName = itemView.findViewById(R.id.txtSongName);
            section = itemView.findViewById(R.id.section);
        }
    }
}

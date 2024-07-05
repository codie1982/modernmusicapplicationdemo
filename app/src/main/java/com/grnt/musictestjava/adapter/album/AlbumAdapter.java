package com.grnt.musictestjava.adapter.album;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grnt.musictestjava.R;
import com.grnt.musictestjava.model.Album;

import java.time.temporal.Temporal;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    IAlbumAdapter iAlbumAdapter;
    List<Album> albumList;
    Context context;
    public AlbumAdapter(List<Album> albumList, Context context,IAlbumAdapter iAlbumAdapter) {
        this.albumList =albumList;
        this.context = context;
        this.iAlbumAdapter = iAlbumAdapter;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new AlbumViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Glide.with(context).load(Uri.parse(albumList.get(position).getImageurl())).into(holder.imgAlbumImage);
        holder.txtAlbumName.setText(albumList.get(position).getAlbumName());
        holder.txtArtistName.setText(albumList.get(position).getArtistName());
        holder.txtCategoryName.setText(albumList.get(position).getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAlbumAdapter.onAlbumClick(albumList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.albumList.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAlbumImage;
        TextView txtAlbumName, txtArtistName, txtCategoryName;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbumImage = itemView.findViewById(R.id.imgAlbumImage);
            txtAlbumName = itemView.findViewById(R.id.txtAlbumName);
            txtArtistName = itemView.findViewById(R.id.txtArtistName);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
        }
    }
}

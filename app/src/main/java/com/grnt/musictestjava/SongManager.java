package com.grnt.musictestjava;

import com.grnt.musictestjava.model.Song;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SongManager implements Serializable {
    private ArrayList<Song> songList;

    public SongManager() {
        songList = new ArrayList<>();
    }

    public void addSong(Song song) {
        songList.add(song);
    }

    public void removeSong(String id) {
        for (int i = 0; i < songList.size(); i++) {
            if (songList.get(i).getId().equals(id)) {
                songList.remove(i);
                break;
            }
        }
    }

    public ArrayList<Song> getSongs() {
        return songList;
    }

    public Song getSongById(String id) {
        for (Song song : songList) {
            if (song.getId().equals(id)) {
                return song;
            }
        }
        return null;
    }
    public void clearSongs() {
        if(songList !=null) songList.clear();
    }
}
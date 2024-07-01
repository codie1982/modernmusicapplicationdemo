package com.grnt.musictestjava.ui;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.grnt.musictestjava.BaseActivity;
import com.grnt.musictestjava.R;
import com.grnt.musictestjava.util.SongManager;
import com.grnt.musictestjava.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private MediaBrowserCompat mediaBrowser;
    private ListView lstSong;
    private ArrayAdapter<String> adapter;
    private SongManager songManager;
    private List<Song> songList = new ArrayList<>();
    private Button btnOpenPlayer,btnLoadSongs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


       /* mediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MyMusicService.class),
                new MediaBrowserCompat.ConnectionCallback() {
                    @Override
                    public void onConnected() {
                        try {
                            MediaControllerCompat mediaController = new MediaControllerCompat(MainActivity.this, mediaBrowser.getSessionToken());
                            MediaControllerCompat.setMediaController(MainActivity.this, mediaController);
                            mediaBrowser.subscribe(mediaBrowser.getRoot(), subscriptionCallback);

                            SongManager songManager1 = new SongManager();
                            String url1 = "https://rr6---sn-u0g3uxax3-pnul.googlevideo.com/videoplayback?expire=1719244334&ei=zkF5ZtmPEq2svdIPxp65gAQ&ip=144.76.140.134&id=o-ADUh024WOhoS_jyA-aIM8BfhQ0Uf20aPriNo6a4fhzhX&itag=140&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&vprv=1&svpuc=1&mime=audio%2Fmp4&rqh=1&gir=yes&clen=1081309&dur=66.757&lmt=1676287343425989&keepalive=yes&c=ANDROID_TESTSUITE&txp=5432434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRAIgIXlzkAeeZr1mr8dg0wj6VaqswbxDxf8sdZmsMrhdw-ECIGRBqbASfP7JOht3MKXTFoV3RZ0jG_hWiYe-xIAQh965&redirect_counter=1&rm=sn-4g5ekz7l&fexp=24350485&req_id=4282feb44d5fa3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=oK&mip=78.181.37.209&mm=31&mn=sn-u0g3uxax3-pnul&ms=au&mt=1719222537&mv=m&mvi=6&pl=24&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AHlkHjAwRQIhAMdYD9zbdUhb7nISZmyOCKSnK3HD0XR6cxjuiBiiTstqAiAXXEcRv6wCG6W-B8IOIdoJmuW8Bu4F1QMfOLNukKPg8Q%3D%3D";
                            String url2 = "https://rr4---sn-u0g3uxax3-pnuz.googlevideo.com/videoplayback?expire=1719244417&ei=IUJ5ZtnnOJPD6dsPyqS1iAc&ip=23.88.68.133&id=o-ADwLuskWv_UL_Yo7WIdetoksx602EzaBeWpmV5XjGvPi&itag=140&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&vprv=1&svpuc=1&mime=audio%2Fmp4&rqh=1&gir=yes&clen=3828135&dur=236.495&lmt=1548209419771165&keepalive=yes&c=ANDROID_TESTSUITE&txp=5535432&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRAIgcumvtBn-hdkJSNtKQgU_R0WWnAoovSZ8HeijfwHczpwCIDyhVZSJXm9xSRMhykkXBe89SBWCa2VBjAs91VVgFV5k&redirect_counter=1&rm=sn-4g5ek67z&fexp=24350485&req_id=ea7adeac21c7a3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=BL&mip=78.181.37.209&mm=31&mn=sn-u0g3uxax3-pnuz&ms=au&mt=1719222537&mv=m&mvi=4&pl=24&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AHlkHjAwRgIhAP_GCkAbBDi-Cs404jctWZht53a5bUiL2OnaD0iuJRWiAiEA3WwsY-8TD7IObMuMDT_hXF0I9uBCE8UmNFPxTcIf088%3D";

                            songManager1.addSong(new Song("1", "Song Title 1", "Artist 1", url1));
                            songManager1.addSong(new Song("2", "Song Title 2", "Artist 2", url2));
                            Intent intent = new Intent(MainActivity.this, MyMusicService.class);
                            intent.setAction("ACTION_PREPARE");
                            intent.putExtra("manager",songManager1);
                            startService(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnectionSuspended() {
                        // The Service has crashed. Disable transport controls until it automatically reconnects
                    }

                    @Override
                    public void onConnectionFailed() {
                        // The Service has refused our connection
                    }
                }, null);*/

    }
    private final MediaBrowserCompat.SubscriptionCallback subscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
            ArrayList<Song> newSongs = new ArrayList<>();
            //newSongs.add(new Song("1", "Song Title 1", "Artist 1", "https://www.example.com/song1.mp3"));
            //newSongs.add(new Song("2", "Song Title 2", "Artist 2", "https://www.example.com/song2.mp3"));
            // Load more songs as needed

            //MyMusicService service = new MyMusicService();
            //service.addSongs(newSongs);

          /*  songManager = new SongManager();
            String url1 = "https://rr6---sn-u0g3uxax3-pnul.googlevideo.com/videoplayback?expire=1719244334&ei=zkF5ZtmPEq2svdIPxp65gAQ&ip=144.76.140.134&id=o-ADUh024WOhoS_jyA-aIM8BfhQ0Uf20aPriNo6a4fhzhX&itag=140&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&vprv=1&svpuc=1&mime=audio%2Fmp4&rqh=1&gir=yes&clen=1081309&dur=66.757&lmt=1676287343425989&keepalive=yes&c=ANDROID_TESTSUITE&txp=5432434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRAIgIXlzkAeeZr1mr8dg0wj6VaqswbxDxf8sdZmsMrhdw-ECIGRBqbASfP7JOht3MKXTFoV3RZ0jG_hWiYe-xIAQh965&redirect_counter=1&rm=sn-4g5ekz7l&fexp=24350485&req_id=4282feb44d5fa3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=oK&mip=78.181.37.209&mm=31&mn=sn-u0g3uxax3-pnul&ms=au&mt=1719222537&mv=m&mvi=6&pl=24&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AHlkHjAwRQIhAMdYD9zbdUhb7nISZmyOCKSnK3HD0XR6cxjuiBiiTstqAiAXXEcRv6wCG6W-B8IOIdoJmuW8Bu4F1QMfOLNukKPg8Q%3D%3D";
            String url2 = "https://rr4---sn-u0g3uxax3-pnuz.googlevideo.com/videoplayback?expire=1719244417&ei=IUJ5ZtnnOJPD6dsPyqS1iAc&ip=23.88.68.133&id=o-ADwLuskWv_UL_Yo7WIdetoksx602EzaBeWpmV5XjGvPi&itag=140&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&vprv=1&svpuc=1&mime=audio%2Fmp4&rqh=1&gir=yes&clen=3828135&dur=236.495&lmt=1548209419771165&keepalive=yes&c=ANDROID_TESTSUITE&txp=5535432&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRAIgcumvtBn-hdkJSNtKQgU_R0WWnAoovSZ8HeijfwHczpwCIDyhVZSJXm9xSRMhykkXBe89SBWCa2VBjAs91VVgFV5k&redirect_counter=1&rm=sn-4g5ek67z&fexp=24350485&req_id=ea7adeac21c7a3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=BL&mip=78.181.37.209&mm=31&mn=sn-u0g3uxax3-pnuz&ms=au&mt=1719222537&mv=m&mvi=4&pl=24&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AHlkHjAwRgIhAP_GCkAbBDi-Cs404jctWZht53a5bUiL2OnaD0iuJRWiAiEA3WwsY-8TD7IObMuMDT_hXF0I9uBCE8UmNFPxTcIf088%3D";
            // Şarkı listesi ekleyin
            songManager.addSong(new Song("1", "Song Title 1", "Artist 1", url1));
            songManager.addSong(new Song("2", "Song Title 2", "Artist 2", url2));
            adapter.clear();
            songList.clear();
            List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
            for (Song song : songManager.getSongs()) {
                MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
                        .setMediaId(song.getId())
                        .setMediaUri(Uri.parse(song.getUrl()))
                        .setTitle(song.getTitle())
                        .setSubtitle(song.getArtist())
                        .build();
                mediaItems.add(new MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));

                adapter.add(description.getTitle().toString());
            }

            adapter.notifyDataSetChanged();*/
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //mediaBrowser.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mediaBrowser.disconnect();
    }

}
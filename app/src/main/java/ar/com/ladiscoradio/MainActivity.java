package ar.com.ladiscoradio;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jean.jcplayer.JcAudio;
import com.example.jean.jcplayer.JcPlayerService;
import com.example.jean.jcplayer.JcPlayerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private JcPlayerView jcplayerView;
    ImageButton playPauseButton;
    Boolean isPlaying = false;
    private String streamURL = "http://streamlky.alsolnet.com:443/ladiscoradio";
    private String streamTitle;
    TextView metadataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ar.com.ladiscoradio.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(ar.com.ladiscoradio.R.id.toolbar);
        setSupportActionBar(toolbar);

        streamTitle = getString(ar.com.ladiscoradio.R.string.app_name);

        initPlayer();

        playPauseButton = (ImageButton)findViewById(ar.com.ladiscoradio.R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    //mRadioManager.stopRadio();
                } else {
                    //mRadioManager.startRadio(streamURL);
                }
            }
        });
        playPauseButton.setImageResource(ar.com.ladiscoradio.R.drawable.ic_play_arrow_black_24dp);

        metadataTextView = findViewById(ar.com.ladiscoradio.R.id.metadata);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(ar.com.ladiscoradio.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == ar.com.ladiscoradio.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == ar.com.ladiscoradio.R.id.nav_camera) {
            // Handle the camera action
        } else if (id == ar.com.ladiscoradio.R.id.nav_gallery) {

        } else if (id == ar.com.ladiscoradio.R.id.nav_slideshow) {

        } else if (id == ar.com.ladiscoradio.R.id.nav_manage) {

        } else if (id == ar.com.ladiscoradio.R.id.nav_share) {

        } else if (id == ar.com.ladiscoradio.R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(ar.com.ladiscoradio.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mRadioManager.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mRadioManager.disconnect();
    }
    /*
    @Override
    public void onRadioLoading() {

    }

    @Override
    public void onRadioConnected() {
    }

    @Override
    public void onRadioStarted() {
        isPlaying = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playPauseButton.setImageResource(ar.com.ladiscoradio.R.drawable.ic_pause_black_24dp);
            }
        });
    }

    @Override
    public void onRadioStopped() {
        isPlaying = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playPauseButton.setImageResource(ar.com.ladiscoradio.R.drawable.ic_play_arrow_black_24dp);
            }
        });
    }

    @Override
    public void onMetaDataReceived(String s, String s1) {
        if(s != null && s.equals("StreamTitle")){
            // set radio art
            mRadioManager.updateNotification(streamTitle,s1, ar.com.ladiscoradio.R.drawable.ic_notification, ar.com.ladiscoradio.R.mipmap.ic_launcher);
            final String metaData = s1;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    metadataTextView.setText(metaData);
                }
            });
        }
    }

    @Override
    public void onError() {

    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }
/*
    @Override
    public void onAudioSessionId(int i) {

    }*/

    private void initPlayer() {
        jcplayerView = (JcPlayerView) findViewById(R.id.jcplayer);
        ArrayList<JcAudio> jcAudios = new ArrayList<>();
        jcAudios.add(JcAudio.createFromURL(streamTitle,streamURL));
        jcplayerView.createNotification();
        jcplayerView.initPlaylist(jcAudios);

        View buttonNext = (View) findViewById(R.id.btn_next);
        buttonNext.setVisibility(View.GONE);
        View buttonPrevious = (View) findViewById(R.id.btn_prev);
        buttonPrevious.setVisibility(View.GONE);
    }
}

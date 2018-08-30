package radio.streaming.streamplayer;

import android.os.Bundle;
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

import co.mobiwise.library.RadioListener;
import co.mobiwise.library.RadioManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RadioListener {

    RadioManager mRadioManager;
    ImageButton playPauseButton;
    Boolean isPlaying = false;
    private String streamURL = "http://www.alsolnet.com/stream/ladiscoradio/listen.pls";
    private String streamTitle;
    TextView metadataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        streamTitle = getString(R.string.app_name);

        mRadioManager = RadioManager.with(this);
        mRadioManager.enableNotification(true);
        mRadioManager.registerListener(this);

        playPauseButton = (ImageButton)findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    mRadioManager.stopRadio();
                    playPauseButton.setImageResource(R.drawable.btn_playback_play);
                } else {
                    mRadioManager.startRadio(streamURL);
                    playPauseButton.setImageResource(R.drawable.btn_playback_pause);
                }
            }
        });

        metadataTextView = findViewById(R.id.metadata);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRadioManager.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRadioManager.disconnect();
    }

    @Override
    public void onRadioConnected() {
    }

    @Override
    public void onRadioStarted() {
        isPlaying = true;
//        playPauseButton.setImageResource(R.drawable.btn_playback_pause);
    }

    @Override
    public void onRadioStopped() {
        isPlaying = false;
//        playPauseButton.setImageResource(R.drawable.btn_playback_play);
    }

    @Override
    public void onMetaDataReceived(String s, String s1) {
        if(s != null && s.equals("StreamTitle")){
            // set radio art
            mRadioManager.updateNotification(streamTitle,s1, R.drawable.default_art);
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
    protected void onResume() {
        super.onResume();
        try {
            if(mRadioManager.isPlaying()){
                playPauseButton.setImageResource(R.drawable.btn_playback_pause);
            } else {
                playPauseButton.setImageResource(R.drawable.btn_playback_play);
            }
        } catch (Exception e) {
            playPauseButton.setImageResource(R.drawable.btn_playback_play);
        }
    }
}

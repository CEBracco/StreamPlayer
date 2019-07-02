package ar.com.ladiscoradio;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ExoPlayer.EventListener {

    ImageButton playPauseButton;
    Boolean isPlaying = false;
    private String streamURL = "http://streamlky.alsolnet.com:443/ladiscoradio";
    private String streamTitle;
    TextView metadataTextView;
    private boolean mBound = false;
    private MusicService musicService;
    private static final int READ_PHONE_STATE_REQUEST_CODE = 22;
    private ServiceConnection serviceConnection = getServiceConnection();
    private Snackbar connectionAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ar.com.ladiscoradio.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(ar.com.ladiscoradio.R.id.toolbar);
        setSupportActionBar(toolbar);

        streamTitle = getString(ar.com.ladiscoradio.R.string.app_name);

        playPauseButton = (ImageButton)findViewById(ar.com.ladiscoradio.R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicService != null){
                    if(isPlaying){
                        musicService.stop();
                    } else {
                        musicService.play(streamURL);
                    }
                }
            }
        });
        playPauseButton.setImageResource(ar.com.ladiscoradio.R.drawable.ic_play_arrow_black_24dp);

        metadataTextView = findViewById(ar.com.ladiscoradio.R.id.metadata);

        processPermissions();
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
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(serviceConnection);
            mBound = false;
        }
    }

    public void onRadioStarted() {
        isPlaying = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playPauseButton.setImageResource(ar.com.ladiscoradio.R.drawable.ic_pause_black_24dp);
            }
        });
    }

    public void onRadioStopped() {
        isPlaying = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playPauseButton.setImageResource(ar.com.ladiscoradio.R.drawable.ic_play_arrow_black_24dp);
            }
        });
    }

    /*
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
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if(mBound){
            musicService.setListener(this);
        }
    }

    private void processPermissions() {
        processPhoneListenerPermission();
        MainActivity activity = this;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
                if (tm != null) {
                    if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
                        if (musicService.isPlaying()) {
                            musicService.stop();
                            //playStopBtn.setImageResource(R.drawable.ic_play);
                        }
                    }
                }

                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                        dismissConnectionAlert();
                    } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                        showConnectionAlert();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, filter);
    }

    private void processPhoneListenerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_PHONE_STATE_REQUEST_CODE) {
            if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "Permiso no concedido\nNo se pausará la reproducción cuando el teléfono suene", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY) {
            onRadioStarted();
        } else {
            if(playbackState == Player.STATE_IDLE) {
                onRadioStopped();
            }
        }
    }

    private ServiceConnection getServiceConnection() {
        if (this.serviceConnection == null) {
            this.serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    MusicService.MusicBinder mServiceBinder = (MusicService.MusicBinder) iBinder;
                    musicService = mServiceBinder.getService();
                    mBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    System.exit(0);
                }
            };
        }
        return serviceConnection;
    }

    private void showConnectionAlert() {
        if(this.connectionAlert == null) {
            this.connectionAlert = Snackbar.make(findViewById(android.R.id.content), "Se perdió la conexión", Snackbar.LENGTH_INDEFINITE);
        }
        connectionAlert.show();
    }

    private void dismissConnectionAlert() {
        if(this.connectionAlert != null) {
            connectionAlert.dismiss();
        }
    }
}

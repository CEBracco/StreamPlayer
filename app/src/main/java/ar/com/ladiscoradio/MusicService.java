package ar.com.ladiscoradio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class MusicService extends Service {

    private SimpleExoPlayer player;
    private ExoPlayer.EventListener eventListener;
    private final Binder mBinder = new MusicBinder();
    private final Integer NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
//        DefaultBandwidthMeter.Builder bandwidthMeterBuilder = new DefaultBandwidthMeter.Builder(getApplicationContext());
//        DefaultBandwidthMeter bandwidthMeter = bandwidthMeterBuilder.build();
//        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
//        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        player = new SimpleExoPlayer.Builder(getApplicationContext()).build();
        super.onCreate();
    }

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        this.stop();
        player.release();
        super.onDestroy();
    }

    public void play(String channelUrl) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "ExoPlayerDemo");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        Handler mainHandler = new Handler();
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory).createMediaSource(Uri.parse(channelUrl));
//        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(channelUrl), dataSourceFactory, extractorsFactory, mainHandler, null);
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, "AppNotificationChannel")
                .setContentTitle("VINILO 98.9")
                .setContentText("Reproduciendo...")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSilent(true)
                .setPriority(5)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    public void stop() {
        stopForeground(true);

        player.setPlayWhenReady(false);
        player.stop();
    }

    public boolean isPlaying() {
        return player.getPlaybackState() == Player.STATE_READY;
    }

    public void setListener(ExoPlayer.EventListener eventListener) {
        if(this.eventListener != eventListener) {
            this.eventListener = eventListener;
            player.addListener(eventListener);
            Log.d("LISTENER", "setListener: We got listener!");
        }
    }


}



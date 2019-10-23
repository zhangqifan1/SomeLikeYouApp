package com.as.somelikeyouapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.as.somelikeyouapp.application.App;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * -----------------------------
 * Created by zqf on 2019/10/16.
 * ---------------------------
 */
public class LocalMusicService extends Service {

    private static final String CHANNEL_ONE_ID = "SomeLikeYou_CHANNEL_ID";
    private static final CharSequence CHANNEL_ONE_NAME = "SomeLikeYou_CHANNEL_NAME";
    private MediaPlayer mMediaPlayer;

    public static final String ACTION_Play = "com.play.ACTION_Play";
    public static final String ACTION_Pause = "com.play.ACTION_Pause";
    public static final String ACTION_Seek = "com.play.ACTION_Seek";

    private boolean isPause = false;
    private Intent mIntent_for_progress;
    private NotificationManager mNotificationManager;
    private int duration;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mIntent_for_progress = new Intent();
        mIntent_for_progress.setAction("media_progress");

        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
//                // 设置PendingIntent
//                .setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
//                // 设置状态栏内的小图标
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(getResources().getString(R.string.app_name))
//                // 设置上下文内容
//                .setContentText("正在上传...")
//                // 设置该通知发生的时间
//                .setWhen(System.currentTimeMillis());


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //修改安卓8.1以上系统报错
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_MIN);
            //如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
            notificationChannel.enableLights(false);
            //是否显示角标
            notificationChannel.setShowBadge(false);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId(CHANNEL_ONE_ID);
        }

        // 获取构建好的Notification
        Notification notification = builder.build();
        //设置为默认的声音
        notification.defaults = Notification.DEFAULT_SOUND;
        startForeground(1, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null && !TextUtils.isEmpty(action)) {

            if (TextUtils.equals(ACTION_Play, action)) {
                if (isPause) {
                    mMediaPlayer.start();
                } else {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            mMediaPlayer.reset();
                            AssetFileDescriptor fd = getAssets().openFd("xuyaorenpei.mp3");
                            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                            mMediaPlayer.prepare();
                            duration = mMediaPlayer.getDuration();
                            mMediaPlayer.start();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        while (mMediaPlayer.isPlaying()) {

                            int currentPosition = mMediaPlayer.getCurrentPosition();
                            mIntent_for_progress.putExtra("current_progress", currentPosition);
                            mIntent_for_progress.putExtra("total_progress", duration);
                            LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(mIntent_for_progress);
                        }

                    });




                }
            } else if (TextUtils.equals(ACTION_Pause, action)) {

                mMediaPlayer.pause();
                isPause = true;

            } else if (TextUtils.equals(ACTION_Seek, action)) {

                int seek = intent.getIntExtra("seek", mMediaPlayer.getCurrentPosition());
                mMediaPlayer.seekTo(seek);

            }

        }

        return START_STICKY;


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }

    }

}

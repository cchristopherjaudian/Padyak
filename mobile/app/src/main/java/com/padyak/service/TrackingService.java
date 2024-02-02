package com.padyak.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.padyak.R;
import com.padyak.activity.frmRide;
import com.padyak.utility.Helper;

public class TrackingService extends Service {
    String distance,time;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Helper.getInstance().log_code, "onStartCommand: Starting Service");
        distance = intent.getStringExtra("distance");
        time = intent.getStringExtra("time");
        createNotification();
        return START_STICKY;
    }
    private void createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent notificationIntent = new Intent(this, frmRide.class);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Ride . ".concat(distance).concat(" . ").concat(time))
                .setContentText("")
                .setContentInfo("")
                .setContentIntent(pendingIntent);

        notificationManager.notify(Integer.parseInt(getString(R.string.foreground_notif_channel)), notificationBuilder.build());
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Log.d(Helper.getInstance().log_code, "Service onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(Helper.getInstance().log_code, "Service onDestroy");
    }


}

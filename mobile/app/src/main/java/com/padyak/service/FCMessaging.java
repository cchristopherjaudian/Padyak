package com.padyak.service;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.padyak.utility.Helper;

public class FCMessaging extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(Helper.getInstance().log_code, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(@androidx.annotation.NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("FCMPadyak", "onMessageReceived: " + message.getFrom());
        if (message.getData().size() > 0) {
            Log.d("FCMPadyak", "Message data payload: " + message.getData());
            Intent intent = new Intent("FCMIntentService");
            intent.putExtra("message", message.getData().toString());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        if (message.getNotification() != null) {
            Log.d("FCMPadyak", "Message Notification Body: " + message.getNotification().getBody());
        }
    }
}

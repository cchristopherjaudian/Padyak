package com.padyak.utility;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.datatransport.runtime.dagger.Component;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MessagingHelper {
    static MessagingHelper messagingHelper;

    public static MessagingHelper getInstance(){
        if(messagingHelper == null) messagingHelper = new MessagingHelper();
        return messagingHelper;
    }
    public void subscribeMessageTopic(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    Log.d(Helper.getInstance().log_code, "Subscribing to: " + topic);
                    if (task.isSuccessful()) {
                        Log.d(Helper.getInstance().log_code, "Subscribed");
                    } else{
                        Log.d(Helper.getInstance().log_code, "Failed to subscribe");
                    }
                });
    }
}

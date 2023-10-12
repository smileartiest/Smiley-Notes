package com.smilearts.smilenotes.notification;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Objects;

public class FireBaseMessage extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("Push Message " , Objects.requireNonNull(Objects.requireNonNull(remoteMessage.getNotification()).getBody()));
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("NEW TOKEN ", s);
        super.onNewToken(s);
    }
}

package com.example.yourfamouscoach.notifications;

import static com.example.yourfamouscoach.utils.StorageUtils.checkBuildPermissions;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.ui.views.activitys.MainActivity;
import com.example.yourfamouscoach.utils.StorageUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.example.yourfamouscoach.di.MyApplication;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (NotificationManagerCompat.from(this).areNotificationsEnabled())
            showNotification(createNotification(message));
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.i("newToken", token);
    }

    @SuppressLint("MissingPermission")
    private void showNotification(Notification notification) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);
    }

    private Notification createNotification(RemoteMessage message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent;
        if (checkBuildPermissions(Build.VERSION_CODES.M, StorageUtils.GREATER)) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            pendingIntent = PendingIntent.getActivity(this, 0, intent,0);
        }

        return new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setSmallIcon(R.drawable.buddha)
                .setAutoCancel(true)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setCategory(Notification.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    } //Pedir permisso api 33


}


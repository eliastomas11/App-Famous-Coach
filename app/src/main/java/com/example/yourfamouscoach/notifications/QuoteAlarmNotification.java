package com.example.yourfamouscoach.notifications;

import static com.example.yourfamouscoach.utils.StorageUtils.checkBuildPermissions;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.di.MyApplication;
import com.example.yourfamouscoach.ui.views.activitys.MainActivity;
import com.example.yourfamouscoach.utils.StorageUtils;
import com.google.firebase.messaging.RemoteMessage;

public class QuoteAlarmNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        QuoteNotificationService quoteNotificationService = new QuoteNotificationService();
        quoteNotificationService.showNotification(context,);
    }


}

package com.example.yourfamouscoach.di;

import static com.example.yourfamouscoach.notifications.QuoteNotificationService.NOTIFICATION_ID;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.impl.WorkManagerImpl;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.notifications.NotificationWorker;
import com.example.yourfamouscoach.notifications.QuoteAlarmNotification;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {

    public static final String CHANNEL_ID = String.valueOf(R.string.quote_channel_id);
    public AppContainer appContainer;

    public static final UUID NOTIFICATION_WORK_ID = UUID.randomUUID();


    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer(this);
        createNotificationChannel();
        sendNotification();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.quote_notification_channel_name);
            String description = getString(R.string.quote_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotification() {

    }

    private void sendNotification() {
        WorkRequest getNofiy = new PeriodicWorkRequest.Builder(NotificationWorker.class,1,TimeUnit.DAYS)
                .setInitialDelay(1,TimeUnit.DAYS)
                .setId(NOTIFICATION_WORK_ID)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build())
                .build();
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueue(getNofiy);
    }

}

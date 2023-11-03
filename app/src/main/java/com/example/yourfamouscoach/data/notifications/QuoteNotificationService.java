package com.example.yourfamouscoach.data.notifications;

import static com.example.yourfamouscoach.utils.StorageUtils.checkBuildPermissions;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.data.datasources.remote.quotesource.service.ApiClient;
import com.example.yourfamouscoach.data.datasources.remote.quotesource.service.ApiService;
import com.example.yourfamouscoach.di.MyApplication;
import com.example.yourfamouscoach.ui.views.activitys.MainActivity;
import com.example.yourfamouscoach.utils.StorageUtils;

public class QuoteNotificationService {

    public static final int NOTIFICATION_ID = 1;
    private final ApiService apiService;

    public QuoteNotificationService () {
        apiService = ApiClient.getApiClientInstance();
    }


    private Notification createNotification(Context context, String quote, String author) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("not",quote);
        intent.putExtra("aut",author);
        PendingIntent pendingIntent;
        if (checkBuildPermissions(Build.VERSION_CODES.M, StorageUtils.GREATER)) {
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }else{
            pendingIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
                .setContentTitle(context.getResources().getString(R.string.quote_notification_title))
                .setContentText(quote)
                .setSmallIcon(R.drawable.buddha)
                .setAutoCancel(true)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setCategory(Notification.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    public void makeNotification(Context context,String quote,String author){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,createNotification(context,quote,author));
    }

    public void showNotification(Context context, String noty,String author){

        makeNotification(context,noty,author);

    }

}

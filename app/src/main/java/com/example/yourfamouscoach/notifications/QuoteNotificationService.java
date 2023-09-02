package com.example.yourfamouscoach.notifications;

import static com.example.yourfamouscoach.utils.StorageUtils.checkBuildPermissions;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.data.model.QuoteDto;
import com.example.yourfamouscoach.di.MyApplication;
import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.ui.views.activitys.MainActivity;
import com.example.yourfamouscoach.utils.StorageUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuoteNotificationService {

    public static final int NOTIFICATION_ID = 1;
    private IQuotesRepository quotesRepository;

    public QuoteNotificationService quoteNotificationService(IQuotesRepository quotesRepository) {
        this.quotesRepository = quotesRepository;
    }


    private Notification createNotification(Context context, String quote) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent;
        if (checkBuildPermissions(Build.VERSION_CODES.M, StorageUtils.GREATER)) {
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            pendingIntent = PendingIntent.getActivity(context, 0, intent,0);
        }

        return new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
                .setContentTitle(context.getResources().getResourceName(R.string.quote_notification_title))
                .setContentText(quote)
                .setSmallIcon(R.drawable.buddha)
                .setAutoCancel(true)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setCategory(Notification.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    } //Pedir permisso api 33

    public void showNotification(Context context,String quote){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,createNotification(context,quote));
    }

    private String getTodaysQuote(){
        quotesRepository.getSingleQuote().enqueue(new Callback<List<QuoteDto>>() {
            @Override
            public void onResponse(Call<List<QuoteDto>> call, Response<List<QuoteDto>> response) {

            }

            @Override
            public void onFailure(Call<List<QuoteDto>> call, Throwable t) {

            }
        });
    }

}

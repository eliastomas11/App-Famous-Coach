package com.example.yourfamouscoach.notifications;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.yourfamouscoach.notifications.QuoteNotificationService.NOTIFICATION_ID;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.yourfamouscoach.data.datasources.remote.quotesource.service.ApiClient;
import com.example.yourfamouscoach.data.datasources.remote.quotesource.service.ApiService;
import com.example.yourfamouscoach.data.model.QuoteDto;
import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.ui.model.QuotePresentation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationWorker extends Worker {

    ApiService apiService;
    Context context;
    QuoteNotificationService quoteNotificationService;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        apiService = ApiClient.getApiClientInstance();
        this.context = context;
        quoteNotificationService = new QuoteNotificationService();
    }

    @NonNull
    @Override
    public Result doWork() {
        apiService.getQuoteForNotification().enqueue(new Callback<List<QuoteDto>>() {
            @Override
            public void onResponse(Call<List<QuoteDto>> call, Response<List<QuoteDto>> response) {
                if(response.body() != null){
                    makeNoty(response.body().get(0).getQuote(),response.body().get(0).getAuthor());
                }
            }

            @Override
            public void onFailure(Call<List<QuoteDto>> call, Throwable t) {
                Log.i("failureWorker","fail");
            }
        });

        return Result.success();
    }

    private void makeNoty(String quote,String author){
//        Intent intent = new Intent(context, QuoteAlarmNotification.class);
//        intent.putExtra("noty",quote);
//        intent.putExtra("auth",author);
//        PendingIntent notificationIntent = PendingIntent.getBroadcast(context,NOTIFICATION_ID,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, 5000,notificationIntent);
        quoteNotificationService.showNotification(context,quote,author);
    }
}

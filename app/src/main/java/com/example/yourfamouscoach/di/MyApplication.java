package com.example.yourfamouscoach.di;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.data.datasources.local.cache.CacheUpdateWorker;
import com.example.yourfamouscoach.data.datasources.preferences.RxDataStoreManager;
import com.example.yourfamouscoach.data.notifications.NotificationWorker;

import org.reactivestreams.Subscription;

import java.util.UUID;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class MyApplication extends Application {

    public static final String CHANNEL_ID = String.valueOf(R.string.quote_channel_id);
    public AppContainer appContainer;
    public static final String NOTIFICATION_REQUEST_TAG = "NotificationRequest";
    public WorkManager workManager;


    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer(this);
        workManager = WorkManager.getInstance(this);
        createNotificationChannel();
        scheduleCacheRefresh();
        scheduleNotification();
    }

    @SuppressLint("CheckResult")
    private void scheduleNotification() {
        RxDataStoreManager dataStoreManager = RxDataStoreManager.getInstance(this);

        Flowable<Boolean> dataStoreReader = dataStoreManager.getNotifPref();
        dataStoreReader.subscribeOn(Schedulers.io()).subscribe(new DisposableSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean isActive) {
                if (!isActive) {
                    workManager.cancelAllWorkByTag(NOTIFICATION_REQUEST_TAG);
                } else {
                    sendNotification();
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.d("TAG", "Error Reading prefs: " + t.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });



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

    private void sendNotification() {
        WorkRequest getNofiy = new PeriodicWorkRequest.Builder(NotificationWorker.class,24,TimeUnit.HOURS)
                .setInitialDelay(24,TimeUnit.HOURS)
                .addTag(NOTIFICATION_REQUEST_TAG)
                .build();
        workManager.enqueue(getNofiy);
    }

    private void scheduleCacheRefresh() {
        WorkRequest cacheRefreshRequest = new PeriodicWorkRequest.Builder(CacheUpdateWorker.class,24,TimeUnit.HOURS)
                .setInitialDelay(24,TimeUnit.HOURS)
                .build();
        workManager.enqueue(cacheRefreshRequest);

    }


}

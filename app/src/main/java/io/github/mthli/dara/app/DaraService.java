package io.github.mthli.dara.app;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.mthli.dara.event.ResponseNotificationListEvent;
import io.github.mthli.dara.event.RequestNotificationListEvent;
import io.github.mthli.dara.util.RxBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class DaraService extends NotificationListenerService {

    public static boolean sIsAlive = false;

    @Override
    public IBinder onBind(Intent intent) {
        setupRxBus();
        sIsAlive = true;
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sIsAlive = false;
        return super.onUnbind(intent);
    }

    private void setupRxBus() {
        RxBus.getInstance().toObservable(RequestNotificationListEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RequestNotificationListEvent>() {
                    @Override
                    public void call(RequestNotificationListEvent event) {
                        onRequestActiveNotificationsEvent();
                    }
                });
    }

    private void onRequestActiveNotificationsEvent() {
        List<StatusBarNotification> list = new ArrayList<>();
        list.addAll(Arrays.asList(getActiveNotifications()));
        RxBus.getInstance().post(new ResponseNotificationListEvent(list));
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        onRequestActiveNotificationsEvent();
        // TODO
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // DO NOTHING
    }
}

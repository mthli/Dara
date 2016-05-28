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
import rx.functions.Action1;

public class DaraService extends NotificationListenerService {

    public static boolean sIsAlive = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setupRxBus();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
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

package io.github.mthli.dara.app;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import io.github.mthli.dara.util.RxBus;

public class DaraService extends NotificationListenerService {
    public static boolean sIsAlive = false;

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

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        RxBus.getInstance().post(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // TODO
    }
}

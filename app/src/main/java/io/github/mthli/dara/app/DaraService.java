package io.github.mthli.dara.app;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;

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
}

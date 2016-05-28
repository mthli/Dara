package io.github.mthli.dara.widget.item;

import android.service.notification.StatusBarNotification;

public class Notifi {

    private StatusBarNotification mNotification;

    public Notifi(StatusBarNotification notification) {
        mNotification = notification;
    }

    public StatusBarNotification getNotification() {
        return mNotification;
    }
}

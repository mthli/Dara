package io.github.mthli.dara.widget.item;

import android.service.notification.StatusBarNotification;

public class Notice {
    private StatusBarNotification mNotification;

    public Notice(StatusBarNotification notification) {
        mNotification = notification;
    }

    public StatusBarNotification getNotification() {
        return mNotification;
    }
}

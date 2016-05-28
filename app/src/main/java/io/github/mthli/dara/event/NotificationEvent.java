package io.github.mthli.dara.event;

import android.service.notification.StatusBarNotification;

public class NotificationEvent {
    private StatusBarNotification mNotification;

    public NotificationEvent(StatusBarNotification notification) {
        mNotification = notification;
    }

    public StatusBarNotification getNotification() {
        return mNotification;
    }
}

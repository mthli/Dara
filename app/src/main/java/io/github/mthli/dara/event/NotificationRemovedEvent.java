package io.github.mthli.dara.event;

import android.service.notification.StatusBarNotification;

public class NotificationRemovedEvent {
    private StatusBarNotification mStatusBarNotification;

    public NotificationRemovedEvent(StatusBarNotification statusBarNotification) {
        mStatusBarNotification = statusBarNotification;
    }

    public StatusBarNotification getStatusBarNotification() {
        return mStatusBarNotification;
    }
}

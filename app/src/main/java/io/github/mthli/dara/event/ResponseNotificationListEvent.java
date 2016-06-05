package io.github.mthli.dara.event;

import android.service.notification.StatusBarNotification;

import java.util.List;

public class ResponseNotificationListEvent {
    private List<StatusBarNotification> mStatusBarNotificationList;

    public ResponseNotificationListEvent(List<StatusBarNotification> statusBarNotificationList) {
        mStatusBarNotificationList = statusBarNotificationList;
    }

    public List<StatusBarNotification> getStatusBarNotificationList() {
        return mStatusBarNotificationList;
    }
}

package io.github.mthli.dara.event;

import android.service.notification.StatusBarNotification;

import java.util.List;

public class NotificationListEvent {
    private List<StatusBarNotification> mList;

    public NotificationListEvent(List<StatusBarNotification> list) {
        mList = list;
    }

    public List<StatusBarNotification> getList() {
        return mList;
    }
}

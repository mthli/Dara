package io.github.mthli.dara.event;

import android.service.notification.StatusBarNotification;

import java.util.List;

public class ResponseNotificationListEvent {
    private List<StatusBarNotification> mList;

    public ResponseNotificationListEvent(List<StatusBarNotification> list) {
        mList = list;
    }

    public List<StatusBarNotification> getList() {
        return mList;
    }
}

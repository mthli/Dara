package io.github.mthli.dara.widget.item;

import android.service.notification.StatusBarNotification;

public class Notifi {
    private StatusBarNotification mNotification;
    private int mPosition;

    public Notifi() {
        this(null, 0);
    }

    public Notifi(StatusBarNotification notification, int position) {
        mNotification = notification;
        mPosition = position;
    }

    public StatusBarNotification getNotification() {
        return mNotification;
    }

    public void setNotification(StatusBarNotification notification) {
        mNotification = notification;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }
}

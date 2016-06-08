package io.github.mthli.dara.app;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mthli.dara.event.NotificationRemovedEvent;
import io.github.mthli.dara.event.ResponseNotificationListEvent;
import io.github.mthli.dara.event.RequestNotificationListEvent;
import io.github.mthli.dara.event.UpdateRecordEvent;
import io.github.mthli.dara.record.Record;
import io.github.mthli.dara.util.RegExUtils;
import io.github.mthli.dara.util.RxBus;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DaraService extends NotificationListenerService {
    public static boolean sIsAlive;

    private List<Record> mRecordList;
    private CompositeSubscription mSubscription;

    @Override
    public IBinder onBind(Intent intent) {
        sIsAlive = true;

        mRecordList = Select.from(Record.class).list();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = new CompositeSubscription();

        setupRxBus();
        return super.onBind(intent);
    }

    private void setupRxBus() {
        Subscription subscription = RxBus.getInstance()
                .toObservable(RequestNotificationListEvent.class)
                .subscribe(new Subscriber<RequestNotificationListEvent>() {
                    @Override
                    public void onCompleted() {
                        // DO NOTHING
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(RequestNotificationListEvent event) {
                        onRequestNotificationListEvent();
                    }
                });
        mSubscription.add(subscription);

        subscription = RxBus.getInstance()
                .toObservable(UpdateRecordEvent.class)
                .subscribe(new Subscriber<UpdateRecordEvent>() {
                    @Override
                    public void onCompleted() {
                        // DO NOTHING
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UpdateRecordEvent event) {
                        onUpdateRecordEvent();
                    }
                });
        mSubscription.add(subscription);
    }

    // Filter isOngoing()
    private void onRequestNotificationListEvent() {
        List<StatusBarNotification> list = new ArrayList<>();
        List<String> group = new ArrayList<>();

        for (StatusBarNotification notification : getActiveNotifications()) {
            if (!notification.isOngoing() && !group.contains(notification.getGroupKey())) {
                group.add(notification.getGroupKey());
                list.add(notification);
            }
        }

        RxBus.getInstance().post(new ResponseNotificationListEvent(list));
    }

    private void onUpdateRecordEvent() {
        onRequestNotificationListEvent();
        mRecordList = Select.from(Record.class).list();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sIsAlive = false;

        mRecordList.clear();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }

        return super.onUnbind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        filterNotification(sbn);
        onRequestNotificationListEvent();
    }

    private void filterNotification(StatusBarNotification sbn) {
        for (Record record : mRecordList) {
            if (record.packageName.equals(sbn.getPackageName())) {
                if (record.isRegEx) {
                    filterNotificationByRegEx(record, sbn);
                } else {
                    filterNotificationByHashTags(record, sbn);
                }
            }
        }
    }

    private void filterNotificationByRegEx(Record record, StatusBarNotification sbn) {
        Bundle bundle = sbn.getNotification().extras;

        if (!TextUtils.isEmpty(record.title)) {
            String title = bundle.getString(Notification.EXTRA_TITLE);
            if (!TextUtils.isEmpty(title)) {
                Matcher matcher = Pattern.compile(record.title).matcher(title);
                if (matcher.matches()) {
                    cancelNotification(sbn.getKey());
                    return;
                }
            }
        }

        if (!TextUtils.isEmpty(record.content)) {
            String content = bundle.getString(Notification.EXTRA_TEXT);
            if (!TextUtils.isEmpty(content)) {
                Matcher matcher = Pattern.compile(record.content).matcher(content);
                if (matcher.matches()) {
                    cancelNotification(sbn.getKey());
                }
            }
        }
    }

    private void filterNotificationByHashTags(Record record, StatusBarNotification sbn) {
        Bundle bundle = sbn.getNotification().extras;

        if (!TextUtils.isEmpty(record.title)) {
            String title = bundle.getString(Notification.EXTRA_TITLE);
            if (!TextUtils.isEmpty(title)) {
                for (String tag : RegExUtils.getHashTags(record.title)) {
                    tag = tag.substring("#".length());
                    if (title.contains(tag)) {
                        cancelNotification(sbn.getKey());
                        return;
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(record.content)) {
            String content = bundle.getString(Notification.EXTRA_TEXT);
            if (!TextUtils.isEmpty(content)) {
                for (String tag : RegExUtils.getHashTags(record.content)) {
                    tag = tag.substring("#".length());
                    if (content.contains(tag)) {
                        cancelNotification(sbn.getKey());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onNotificationRankingUpdate(RankingMap rankingMap) {
        onRequestNotificationListEvent();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        onRequestNotificationListEvent();
        RxBus.getInstance().post(new NotificationRemovedEvent(sbn));
    }
}

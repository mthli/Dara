package io.github.mthli.dara.app;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.github.mthli.dara.event.NotificationRemovedEvent;
import io.github.mthli.dara.event.ResponseNotificationListEvent;
import io.github.mthli.dara.event.RequestNotificationListEvent;
import io.github.mthli.dara.event.UpdateRecordEvent;
import io.github.mthli.dara.record.Record;
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
        // TODO
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
        onRequestNotificationListEvent();
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

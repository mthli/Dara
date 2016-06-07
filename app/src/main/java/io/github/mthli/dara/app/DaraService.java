package io.github.mthli.dara.app;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.ArrayList;
import java.util.List;

import io.github.mthli.dara.event.NotificationRemovedEvent;
import io.github.mthli.dara.event.ResponseNotificationListEvent;
import io.github.mthli.dara.event.RequestNotificationListEvent;
import io.github.mthli.dara.event.UpdateRecordEvent;
import io.github.mthli.dara.util.RxBus;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DaraService extends NotificationListenerService {
    public static boolean sIsAlive = false;
    private CompositeSubscription mSubscription;

    @Override
    public IBinder onBind(Intent intent) {
        setupRxBus();
        sIsAlive = true;
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sIsAlive = false;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        return super.onUnbind(intent);
    }

    private void setupRxBus() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = new CompositeSubscription();

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
                        onRequestActiveNotificationsEvent();
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
                        onRequestActiveNotificationsEvent();
                    }
                });
        mSubscription.add(subscription);
    }

    // Filter isOngoing()
    private void onRequestActiveNotificationsEvent() {
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

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        onRequestActiveNotificationsEvent();
    }

    @Override
    public void onNotificationRankingUpdate(RankingMap rankingMap) {
        onRequestActiveNotificationsEvent();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        onRequestActiveNotificationsEvent();
        RxBus.getInstance().post(new NotificationRemovedEvent(sbn));
    }
}

package io.github.mthli.dara.widget;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.event.RequestNotificationListEvent;
import io.github.mthli.dara.event.ResponseNotificationListEvent;
import io.github.mthli.dara.record.Record;
import io.github.mthli.dara.util.AppInfoUtils;
import io.github.mthli.dara.util.DisplayUtils;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.adapter.DaraAdapter;
import io.github.mthli.dara.widget.item.Filter;
import io.github.mthli.dara.widget.item.Label;
import io.github.mthli.dara.widget.item.Notice;
import io.github.mthli.dara.widget.item.Space;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecyclerLayout extends FrameLayout {
    private DaraAdapter mAdapter;
    private List<Object> mList;

    private Subscription mResponseSubscription;

    public RecyclerLayout(Context context) {
        super(context);
    }

    public RecyclerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setupRecyclerView();
        setupRxBus();
        RxBus.getInstance().post(new RequestNotificationListEvent());
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mResponseSubscription != null) {
            mResponseSubscription.unsubscribe();
        }
    }

    private void setupRecyclerView() {
        mList = new ArrayList<>();
        mAdapter = new DaraAdapter(getContext(), mList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new DaraItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    private void setupRxBus() {
        mResponseSubscription = RxBus.getInstance()
                .toObservable(ResponseNotificationListEvent.class)
                .subscribeOn(Schedulers.newThread())
                .lift(new Observable.Operator<List<Notice>, ResponseNotificationListEvent>() {
                    @Override
                    public Subscriber<? super ResponseNotificationListEvent> call(final Subscriber<? super List<Notice>> subscriber) {
                        return new Subscriber<ResponseNotificationListEvent>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(ResponseNotificationListEvent event) {
                                subscriber.onNext(buildNoticeList(event));
                            }
                        };
                    }
                })
                .lift(new Observable.Operator<List<Object>, List<Notice>>() {
                    @Override
                    public Subscriber<? super List<Notice>> call(final Subscriber<? super List<Object>> subscriber) {
                        return new Subscriber<List<Notice>>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(List<Notice> list) {
                                subscriber.onNext(buildObjectList(list));
                            }
                        };
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Object>>() {
                    @Override
                    public void onCompleted() {
                        // DO NOTHING
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Object> list) {
                        // Simple and crude
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private List<Notice> buildNoticeList(ResponseNotificationListEvent event) {
        List<String> group = new ArrayList<>();
        List<Notice> list = new ArrayList<>();

        for (StatusBarNotification notification : event.getStatusBarNotificationList()) {
            if (!notification.isOngoing() && !group.contains(notification.getGroupKey())) {
                group.add(notification.getGroupKey());
                list.add(new Notice(notification));
            }
        }

        return list;
    }

    private List<Object> buildObjectList(List<Notice> noticeList) {
        List<Object> objectList = new ArrayList<>();
        objectList.add(new Space(DisplayUtils.getStatusBarHeight(getContext())));

        if (noticeList.size() > 0) {
            objectList.add(new Label(getResources().getString(R.string.label_notification_center)));
            objectList.addAll(noticeList);
        }

        List<Record> recordList = Select.from(Record.class)
                .orderBy(NamingHelper.toSQLNameDefault("mPackageName")).list();

        List<String> packageList = new ArrayList<>();
        for (Record record : recordList) {
            packageList.add(record.getPackageName());
        }
        HashSet<String> labelSet = new HashSet<>(packageList);
        packageList.clear();
        packageList.addAll(labelSet);

        // TODO sort by label
        int hint = ContextCompat.getColor(getContext(), R.color.text_hint);
        int teal = ContextCompat.getColor(getContext(), R.color.teal_500);
        int i = 0;
        for (String packageName : packageList) {
            String packageLabel = AppInfoUtils.getAppLabel(getContext(), packageName);
            if (TextUtils.isEmpty(packageLabel)) {
                continue;
            }

            objectList.add(new Label(packageLabel));
            for (Record record : recordList) {
                if (record.getPackageName().equals(packageName)) {
                    Filter filter = new Filter();
                    filter.setColor(i++ % 2 == 0 ? hint : teal);
                    filter.setRegEx(record.getRegEx());
                    filter.setTitle(record.getTitle());
                    filter.setContent(record.getContent());
                    objectList.add(filter);
                }
            }
        }

        return objectList;
    }
}

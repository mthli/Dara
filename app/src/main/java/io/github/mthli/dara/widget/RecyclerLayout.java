package io.github.mthli.dara.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.app.EditActivity;
import io.github.mthli.dara.event.ClickFilterEvent;
import io.github.mthli.dara.event.ClickNoticeEvent;
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
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RecyclerLayout extends BottomSheetLayout
        implements CustomMenuSheetView.OnMenuItemClickListener {
    private CustomMenuSheetView mMenuSheetView;
    private Record mRecord;

    private CustomRecyclerView mRecyclerView;
    private DaraAdapter mAdapter;
    private List<Object> mList;

    private Subscription mFilterSubscription;
    private Subscription mNoticeSubscription;
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
        setupMenuSheetView();
        setupRecyclerView();
        setupRxBus();
        RxBus.getInstance().post(new RequestNotificationListEvent());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dismissSheet();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mFilterSubscription != null) {
            mFilterSubscription.unsubscribe();
        }

        if (mNoticeSubscription != null) {
            mNoticeSubscription.unsubscribe();
        }

        if (mResponseSubscription != null) {
            mResponseSubscription.unsubscribe();
        }
    }

    public void setupMenuSheetView() {
        mMenuSheetView = new CustomMenuSheetView(getContext(),
                CustomMenuSheetView.MenuType.LIST, null, this);
        mMenuSheetView.inflateMenu(R.menu.menu_sheet);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                // TODO
                break;
            case R.id.delete:
                // TODO
                break;
            default:
                break;
        }

        return true;
    }

    private void setupRecyclerView() {
        mList = new ArrayList<>();
        mAdapter = new DaraAdapter(getContext(), mList);

        mRecyclerView = (CustomRecyclerView) findViewById(R.id.recycler);
        ((LayoutParams) mRecyclerView.getLayoutParams()).gravity = Gravity.CENTER;
        mRecyclerView.addItemDecoration(new DaraItemDecoration(getContext()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupRxBus() {
        mFilterSubscription = RxBus.getInstance()
                .toObservable(ClickFilterEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ClickFilterEvent>() {
                    @Override
                    public void call(ClickFilterEvent event) {
                        onClickFilterEvent(event);
                    }
                });

        mNoticeSubscription = RxBus.getInstance().toObservable(ClickNoticeEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ClickNoticeEvent>() {
                    @Override
                    public void call(ClickNoticeEvent event) {
                        onClickNoticeEvent(event);
                    }
                });

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

    private void onClickFilterEvent(ClickFilterEvent event) {
        mRecord = event.getFilter().getRecord();
        showWithSheetView(mMenuSheetView);
    }

    private void onClickNoticeEvent(ClickNoticeEvent event) {
        // Caused by: java.lang.RuntimeException: Not allowed to write file descriptors here
        StatusBarNotification notification = event.getNotice().getNotification().clone();
        notification.getNotification().extras = null;
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra(EditActivity.EXTRA, notification);
        getContext().startActivity(intent);
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
        Collections.sort(packageList, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                lhs = AppInfoUtils.getAppLabel(getContext(), lhs);
                rhs = AppInfoUtils.getAppLabel(getContext(), rhs);
                if (TextUtils.isEmpty(lhs) || TextUtils.isEmpty(rhs)) {
                    return 0;
                } else {
                    return lhs.compareTo(rhs);
                }
            }
        });

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
                    filter.setRecord(record);
                    objectList.add(filter);
                }
            }
        }

        return objectList;
    }
}

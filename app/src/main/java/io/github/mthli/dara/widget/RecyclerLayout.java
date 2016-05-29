package io.github.mthli.dara.widget;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MenuItem;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.ArrayList;
import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.event.ClickNoticeEvent;
import io.github.mthli.dara.event.RequestNotificationListEvent;
import io.github.mthli.dara.event.ResponseNotificationListEvent;
import io.github.mthli.dara.util.DisplayUtils;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.adapter.DaraAdapter;
import io.github.mthli.dara.widget.item.Label;
import io.github.mthli.dara.widget.item.Notice;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RecyclerLayout extends BottomSheetLayout
        implements MenuSheetView.OnMenuItemClickListener {
    private MenuSheetView mMenuSheetView;
    private RecyclerView mRecyclerView;
    private DaraAdapter mAdapter;
    private List<Object> mList;

    private Subscription mResponseSubscription;
    private Subscription mClickSubscription;

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

        setupBottomSheet();
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

        if (mClickSubscription != null) {
            mClickSubscription.unsubscribe();
        }
    }

    private void setupBottomSheet() {
        mMenuSheetView = new MenuSheetView(getContext(), MenuSheetView.MenuType.LIST, null, this);
        mMenuSheetView.inflateMenu(R.menu.filter);
        mMenuSheetView.getMenu().findItem(R.id.space)
                .setVisible(DisplayUtils.hasNavigationBar(getContext()));
        mMenuSheetView.updateMenu();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.keywords:
                onClickFilterByKeywordsItem();
                break;
            case R.id.time:
                onClickFilterByTimeItem();
                break;
            case R.id.custom:
                onClickCustomReminderItem();
                break;
            default:
                break;
        }

        // Don't dismiss when click
        return false;
    }

    private void onClickFilterByKeywordsItem() {
        // TODO
    }

    private void onClickFilterByTimeItem() {
        // TODO
    }

    private void onClickCustomReminderItem() {
        // TODO
    }

    private void setupRecyclerView() {
        mList = new ArrayList<>();
        mAdapter = new DaraAdapter(getContext(), mList);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.addItemDecoration(new DaraItemDecoration(getContext()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupRxBus() {
        mResponseSubscription = RxBus.getInstance().toObservable(ResponseNotificationListEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseNotificationListEvent>() {
                    @Override
                    public void call(ResponseNotificationListEvent event) {
                        onResponseNotificationListEvent(event);
                    }
                });

        mClickSubscription = RxBus.getInstance().toObservable(ClickNoticeEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ClickNoticeEvent>() {
                    @Override
                    public void call(ClickNoticeEvent event) {
                        onClickNoticeHolderEvent(event);
                    }
                });
    }

    private void onResponseNotificationListEvent(ResponseNotificationListEvent event) {
        List<String> group = new ArrayList<>();

        List<Notice> list = new ArrayList<>();
        for (StatusBarNotification notification : event.getList()) {
            if (!notification.isOngoing() && !group.contains(notification.getGroupKey())) {
                group.add(notification.getGroupKey());
                list.add(new Notice(notification));
            }
        }

        buildRecyclerList(list);
    }

    private void buildRecyclerList(List<Notice> list) {
        mList.clear();

        if (list != null && list.size() > 0) {
            mList.add(new Label(getResources().getString(R.string.label_notification_center)));
            mList.addAll(list);
        }

        // TODO
        mAdapter.notifyDataSetChanged();
    }

    private void onClickNoticeHolderEvent(ClickNoticeEvent event) {
        showWithSheetView(mMenuSheetView);
    }
}

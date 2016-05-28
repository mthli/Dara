package io.github.mthli.dara.widget;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.OnSheetDismissedListener;
import com.flipboard.bottomsheet.commons.MenuSheetView;

import java.util.ArrayList;
import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.event.RequestNotificationListEvent;
import io.github.mthli.dara.event.ResponseNotificationListEvent;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.adapter.DaraAdapter;
import io.github.mthli.dara.widget.item.Label;
import io.github.mthli.dara.widget.item.Notifi;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RecyclerLayout extends FrameLayout
        implements OnSheetDismissedListener, MenuSheetView.OnMenuItemClickListener {

    private RecyclerView mRecyclerView;
    private DaraAdapter mAdapter;
    private List<Object> mList;

    private BottomSheetLayout mBottomSheetLayout;
    private MenuSheetView mMenuSheetView;

    private Subscription mSubscription;

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

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    private void setupBottomSheet() {
        mBottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottom_sheet);
        mBottomSheetLayout.addOnSheetDismissedListener(this);

        mMenuSheetView = new MenuSheetView(getContext(), MenuSheetView.MenuType.LIST, null, this);
        mMenuSheetView.inflateMenu(R.menu.bottom_sheet);
    }

    @Override
    public void onDismissed(BottomSheetLayout layout) {
        // TODO
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO
        return false;
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
        mSubscription = RxBus.getInstance().toObservable(ResponseNotificationListEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseNotificationListEvent>() {
                    @Override
                    public void call(ResponseNotificationListEvent event) {
                        onResponseNotificationListEvent(event);
                    }
                });
    }

    private void onResponseNotificationListEvent(ResponseNotificationListEvent event) {
        List<String> group = new ArrayList<>();

        List<Notifi> list = new ArrayList<>();
        for (StatusBarNotification notification : event.getList()) {
            if (!notification.isOngoing() && !group.contains(notification.getGroupKey())) {
                group.add(notification.getGroupKey());
                list.add(new Notifi(notification, 0));
            }
        }

        buildRecyclerList(list);
    }

    private void buildRecyclerList(List<Notifi> list) {
        mList.clear();

        if (list != null && list.size() > 0) {
            mList.add(new Label(getResources().getString(R.string.label_notification_center)));
            mList.addAll(list);
        }

        // TODO
        mAdapter.notifyDataSetChanged();
    }
}

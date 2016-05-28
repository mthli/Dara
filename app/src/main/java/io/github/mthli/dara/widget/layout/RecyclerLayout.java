package io.github.mthli.dara.widget.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.event.ResponseNotificationListEvent;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.adapter.DaraAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RecyclerLayout extends FrameLayout {

    private RecyclerView mRecyclerView;
    private DaraAdapter mAdapter;
    private List<Object> mList;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        setupRecyclerView();
        setupRxBus();
    }

    private void setupRecyclerView() {
        mList = new ArrayList<>();
        mAdapter = new DaraAdapter(getContext(), mList);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupRxBus() {
        RxBus.getInstance().toObservable(ResponseNotificationListEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseNotificationListEvent>() {
                    @Override
                    public void call(ResponseNotificationListEvent event) {

                    }
                });
    }
}

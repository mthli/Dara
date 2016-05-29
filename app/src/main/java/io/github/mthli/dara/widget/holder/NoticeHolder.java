package io.github.mthli.dara.widget.holder;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import io.github.mthli.dara.R;
import io.github.mthli.dara.event.ClickNoticeEvent;
import io.github.mthli.dara.util.DisplayUtils;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.item.Notice;

public class NoticeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private FrameLayout mWrapperView;
    private Notice mNotice;

    public NoticeHolder(View view) {
        super(view);
        view.setOnClickListener(this);

        mWrapperView = (FrameLayout) view.findViewById(R.id.wrapper);
        ViewCompat.setElevation(mWrapperView, DisplayUtils.dp2px(view.getContext(), 2.0f));
    }

    @Override
    public void onClick(View view) {
        if (mNotice != null) {
            RxBus.getInstance().post(new ClickNoticeEvent(mNotice));
        }
    }

    public void setNotice(Notice notice) {
        mNotice = notice;

        mWrapperView.removeAllViews();
        View view = notice.getNotification().getNotification().contentView
                .apply(mWrapperView.getContext().getApplicationContext(), mWrapperView);
        mWrapperView.addView(view);
    }
}

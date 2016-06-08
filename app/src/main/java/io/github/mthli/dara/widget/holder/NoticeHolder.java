package io.github.mthli.dara.widget.holder;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import io.github.mthli.dara.event.ClickNoticeEvent;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.item.Notice;

public class NoticeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private FrameLayout mFrameNotice;
    private Notice mNotice;

    public NoticeHolder(View view) {
        super(view);
        mFrameNotice = (FrameLayout) view;
        mFrameNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mNotice != null) {
            RxBus.getInstance().post(new ClickNoticeEvent(mNotice));
        }
    }

    public void setNotice(Notice notice) {
        mNotice = notice;

        mFrameNotice.removeAllViews();
        View view = notice.getNotification().getNotification().contentView
                .apply(mFrameNotice.getContext().getApplicationContext(), mFrameNotice);
        mFrameNotice.addView(view);
    }
}

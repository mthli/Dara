package io.github.mthli.dara.widget.holder;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import io.github.mthli.dara.R;
import io.github.mthli.dara.event.ClickNotifiHolderEvent;
import io.github.mthli.dara.util.DisplayUtils;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.item.Notifi;

public class NotifiHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private FrameLayout mWrapperView;
    private Notifi mNotifi;

    public NotifiHolder(View view) {
        super(view);
        view.setOnClickListener(this);

        mWrapperView = (FrameLayout) view.findViewById(R.id.wrapper);
        ViewCompat.setElevation(mWrapperView, DisplayUtils.dp2px(view.getContext(), 2.0f));
    }

    @Override
    public void onClick(View view) {
        if (mNotifi != null) {
            RxBus.getInstance().post(new ClickNotifiHolderEvent(mNotifi));
        }
    }

    public void setNotifi(Notifi notifi) {
        mNotifi = notifi;

        mWrapperView.removeAllViews();
        View view = notifi.getNotification().getNotification().contentView
                .apply(mWrapperView.getContext().getApplicationContext(), mWrapperView);
        mWrapperView.addView(view);
    }
}

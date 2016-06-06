package io.github.mthli.dara.widget.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import io.github.mthli.dara.R;
import io.github.mthli.dara.widget.item.Space;

public class SpaceHolder extends RecyclerView.ViewHolder {
    private FrameLayout mFrameLayout;

    public SpaceHolder(View view) {
        super(view);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.space);
    }

    public void setSpace(Space space) {
        mFrameLayout.getLayoutParams().height = space.getHeight();
        mFrameLayout.requestLayout();
    }
}

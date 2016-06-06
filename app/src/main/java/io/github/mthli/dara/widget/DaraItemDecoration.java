package io.github.mthli.dara.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.mthli.dara.R;
import io.github.mthli.dara.util.DisplayUtils;

public class DaraItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public DaraItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            if (child.getId() == R.id.frame_label || i + 1 >= count) {
                continue;
            }

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int right = parent.getWidth() - parent.getPaddingRight();
            int bottom = top + mDivider.getIntrinsicHeight();

            int left = parent.getPaddingLeft();
            if (child.getId() == R.id.frame_filter) {
                left += (int) DisplayUtils.dp2px(parent.getContext(), 4.0f);
            } else {
                left += (int) DisplayUtils.dp2px(parent.getContext(), 64.0f);
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}

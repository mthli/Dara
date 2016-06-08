package io.github.mthli.dara.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import io.github.mthli.dara.R;
import io.github.mthli.dara.util.DisplayUtils;

public class PermissionLayout extends FrameLayout implements View.OnClickListener {
    public interface PermissionLayoutListener {
        void onPositionClick();
        void onNegativeClick();
        void onNeutralClick();
    }

    private PermissionLayoutListener mPermissionLayoutListener;

    public PermissionLayout(Context context) {
        super(context);
    }

    public PermissionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PermissionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPermissionLayoutListener(PermissionLayoutListener permissionLayoutListener) {
        mPermissionLayoutListener = permissionLayoutListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.positive).setOnClickListener(this);
        findViewById(R.id.negative).setOnClickListener(this);
        findViewById(R.id.neutral).setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            int dp480 = (int) DisplayUtils.dp2px(getContext(), 480.0f);
            width = width < dp480 ? width : dp480;
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.positive) {
            if (mPermissionLayoutListener != null) {
                mPermissionLayoutListener.onPositionClick();
            }
        } else if (view.getId() == R.id.negative) {
            if (mPermissionLayoutListener != null) {
                mPermissionLayoutListener.onNegativeClick();
            }
        } else if (view.getId() == R.id.neutral) {
            if (mPermissionLayoutListener != null) {
                mPermissionLayoutListener.onNeutralClick();
            }
        }
    }
}

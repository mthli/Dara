package io.github.mthli.dara.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import io.github.mthli.dara.R;

public class PermissionLayout extends FrameLayout implements View.OnClickListener {

    public interface PermissionLayoutListener {
        void onPositionClick();
        void onNegativeClick();
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

        AppCompatButton positive = (AppCompatButton) findViewById(R.id.positive);
        positive.setOnClickListener(this);

        AppCompatButton negative = (AppCompatButton) findViewById(R.id.negative);
        negative.setOnClickListener(this);
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
        }
    }
}

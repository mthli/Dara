package io.github.mthli.dara.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ButtonBarLayout extends RelativeLayout implements View.OnClickListener {
    public interface ButtonBarLayoutListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
        void onNeutralButtonClick();
    }

    private ButtonBarLayoutListener mButtonBarLayoutListener;

    private AppCompatButton mPositiveButton;
    private AppCompatButton mNegivateButton;
    private AppCompatButton mNeutralButton;

    public ButtonBarLayout(Context context) {
        super(context);
    }

    public ButtonBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setButtonBarLayoutListener(ButtonBarLayoutListener buttonBarLayoutListener) {
        mButtonBarLayoutListener = buttonBarLayoutListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // TODO

        mPositiveButton.setOnClickListener(this);
        mNegivateButton.setOnClickListener(this);
        mNeutralButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mPositiveButton) {
            if (mButtonBarLayoutListener != null) {
                mButtonBarLayoutListener.onPositiveButtonClick();
            }
        } else if (view == mNegivateButton) {
            if (mButtonBarLayoutListener != null) {
                mButtonBarLayoutListener.onNegativeButtonClick();
            }
        } else if (view == mNeutralButton) {
            if (mButtonBarLayoutListener != null) {
                mButtonBarLayoutListener.onNeutralButtonClick();
            }
        }
    }
}

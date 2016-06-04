package io.github.mthli.dara.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import io.github.mthli.dara.R;

public class RuleContentLayout extends LinearLayout {
    private SwitchCompat mSwitchRegular;
    private AppCompatEditText mTitleView;
    private AppCompatEditText mContentView;

    public RuleContentLayout(Context context) {
        super(context);
    }

    public RuleContentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RuleContentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCustomEnable(boolean enabled) {
        mSwitchRegular.setEnabled(enabled);
        mTitleView.setEnabled(enabled);
        mContentView.setEnabled(enabled);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mSwitchRegular = (SwitchCompat) findViewById(R.id.switch_regular);
        mTitleView = (AppCompatEditText) findViewById(R.id.title);
        mContentView = (AppCompatEditText) findViewById(R.id.content);
        setCustomEnable(false);
    }
}

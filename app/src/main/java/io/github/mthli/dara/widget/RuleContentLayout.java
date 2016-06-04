package io.github.mthli.dara.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import io.github.mthli.dara.R;

public class RuleContentLayout extends LinearLayout {
    private SwitchCompat mSwitchRegular;

    public RuleContentLayout(Context context) {
        super(context);
    }

    public RuleContentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RuleContentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mSwitchRegular = (SwitchCompat) findViewById(R.id.switch_regular);
        // TODO
    }
}

package io.github.mthli.dara.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import io.github.mthli.dara.R;

public class PagerLayout extends LinearLayout {

    private TabHost mTabHost;
    private TabWidget mTabWidget;
    private FrameLayout mTabContent;
    private ViewPager mViewPager;

    public PagerLayout(Context context) {
        super(context);
    }

    public PagerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mTabContent = (FrameLayout) findViewById(android.R.id.tabcontent);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        setupTabHost();
    }

    private void setupTabHost() {

    }
}

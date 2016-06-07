package io.github.mthli.dara.app;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.github.mthli.dara.R;
import io.github.mthli.dara.util.DisplayUtils;
import io.github.mthli.dara.widget.RecyclerLayout;
import io.github.mthli.dara.widget.PermissionLayout;

public class MainActivity extends AppCompatActivity
        implements PermissionLayout.PermissionLayoutListener {
    private FrameLayout mContainer;
    private boolean isFirstResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTaskDescription();

        mContainer = (FrameLayout) findViewById(R.id.container);
        isFirstResume = true;
    }

    private void setupTaskDescription() {
        setTaskDescription(new ActivityManager.TaskDescription(
                getString(R.string.app_name),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                ContextCompat.getColor(MainActivity.this, R.color.white)
        ));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!DaraService.sIsAlive) {
            setupWhenPermissionDenied();
        } else if (isFirstResume) {
            setupWhenPermissionGrant();
            isFirstResume = false;
        }
    }

    private void setupWhenPermissionDenied() {
        PermissionLayout layout = (PermissionLayout) getLayoutInflater()
                .inflate(R.layout.layout_permission, null, false);
        layout.setPermissionLayoutListener(this);
        mContainer.removeAllViews();
        mContainer.addView(layout);
    }

    @Override
    public void onPositionClick() {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }

    @Override
    public void onNegativeClick() {
        finish();
    }

    @Override
    public void onNeutralClick() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.blue_grey_900));
        builder.build().launchUrl(this,
                Uri.parse("https://support.google.com/nexus/answer/6111294"));
    }

    private void setupWhenPermissionGrant() {
        RecyclerLayout layout = (RecyclerLayout) getLayoutInflater()
                .inflate(R.layout.layout_recycler, null, false);
        mContainer.removeAllViews();
        mContainer.addView(layout);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mContainer.getLayoutParams().width = (int) DisplayUtils.dp2px(this, 480.0f);
            mContainer.requestLayout();
        } else {
            mContainer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            mContainer.requestLayout();
        }
    }
}
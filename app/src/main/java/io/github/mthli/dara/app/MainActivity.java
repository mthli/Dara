package io.github.mthli.dara.app;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.github.mthli.dara.R;
import io.github.mthli.dara.event.ClickNoticeEvent;
import io.github.mthli.dara.util.DisplayUtils;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.RecyclerLayout;
import io.github.mthli.dara.widget.PermissionLayout;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity
        implements PermissionLayout.PermissionLayoutListener {
    private FrameLayout mContainer;
    private boolean isFirstResume;

    private Subscription mClickSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTaskDescription();
        setupRxBus();

        mContainer = (FrameLayout) findViewById(R.id.container);
        isFirstResume = true;
    }

    private void setupTaskDescription() {
        setTaskDescription(new ActivityManager.TaskDescription(
                getString(R.string.app_name),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                ContextCompat.getColor(MainActivity.this, R.color.blue_grey_900)
        ));
    }

    private void setupRxBus() {
        mClickSubscription = RxBus.getInstance().toObservable(ClickNoticeEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ClickNoticeEvent>() {
                    @Override
                    public void call(ClickNoticeEvent event) {
                        onClickNoticeHolderEvent(event);
                    }
                });
    }

    private void onClickNoticeHolderEvent(ClickNoticeEvent event) {
        // Caused by: java.lang.RuntimeException: Not allowed to write file descriptors here
        StatusBarNotification notification = event.getNotice().getNotification().clone();
        notification.getNotification().extras = null;

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.EXTRA, notification);
        startActivityForResult(intent, EditActivity.REQUEST);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mClickSubscription != null) {
            mClickSubscription.unsubscribe();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO
    }
}
package io.github.mthli.dara.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.FrameLayout;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.github.mthli.dara.R;
import io.github.mthli.dara.util.ConstantUtils;
import io.github.mthli.dara.util.DisplayUtils;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.PermissionLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends RxAppCompatActivity implements PermissionLayout.PermissionLayoutListener {

    private FrameLayout mContainer;
    private boolean isFirstResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainer = (FrameLayout) findViewById(R.id.container);
        isFirstResume = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!DaraService.sIsAlive) {
            setupWhenPermissionDenied();
        } else if (isFirstResume) {
            setupWhenPermissionGrant();
            setupRxBus();
            isFirstResume = false;
        }
    }

    private void setupWhenPermissionDenied() {
        PermissionLayout layout = (PermissionLayout) getLayoutInflater().inflate(R.layout.layout_permission, null, false);
        layout.setPermissionLayoutListener(this);
        mContainer.removeAllViews();
        mContainer.addView(layout);
    }

    @Override
    public void onPositionClick() {
        Intent intent = new Intent(ConstantUtils.PERMISIION_NAME);
        startActivity(intent);
    }

    @Override
    public void onNegativeClick() {
        finish();
    }

    @Override
    public void onNeutralClick() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        builder.build().launchUrl(this, Uri.parse(ConstantUtils.PERMISSION_DETAIL_URL));
    }

    // TODO
    private void setupWhenPermissionGrant() {
        mContainer.removeAllViews();
    }

    private void setupRxBus() {
        RxBus.getInstance().toObservable(StatusBarNotification.class)
                .compose(this.<StatusBarNotification>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StatusBarNotification>() {
                    @Override
                    public void call(StatusBarNotification sbn) {
                        onSbnEvent(sbn);
                    }
                });
    }

    private void onSbnEvent(StatusBarNotification sbn) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        int dp8 = (int) DisplayUtils.dp2px(this, 8.0f);
        params.topMargin = params.bottomMargin = dp8;
        params.leftMargin = params.rightMargin = dp8;

        mContainer.removeAllViews();
        mContainer.addView(getNotificationView(sbn), params);
    }

    private View getNotificationView(StatusBarNotification sbn) {
        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.layout_notification, null, false);
        View view =  sbn.getNotification().contentView.apply(getApplicationContext(), layout);
        layout.removeAllViews();
        layout.addView(view);
        ViewCompat.setElevation(layout, DisplayUtils.dp2px(this, 2.0f));
        return layout;
    }
}
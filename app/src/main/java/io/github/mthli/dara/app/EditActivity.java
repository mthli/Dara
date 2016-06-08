package io.github.mthli.dara.app;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.event.NotificationRemovedEvent;
import io.github.mthli.dara.event.UpdateRecordEvent;
import io.github.mthli.dara.record.Record;
import io.github.mthli.dara.util.AppInfoUtils;
import io.github.mthli.dara.util.DisplayUtils;
import io.github.mthli.dara.util.RxBus;
import io.github.mthli.dara.widget.EditLayout;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class EditActivity extends AppCompatActivity
        implements View.OnClickListener, EditLayout.EditLayoutListener {
    public static final String EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME";
    public static final String EXTRA_IS_REG_EX = "EXTRA_IS_REG_EX";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_CONTENT = "EXTRA_CONTENT";
    public static final String EXTRA_RECORD_ID = "EXTRA_RECORD_ID";

    private String mPackageName;
    private boolean mIsRegEx;
    private String mTitle;
    private String mContent;
    private long mRecordId;

    private Subscription mRemovedSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setFinishOnTouchOutside(false);
        setupTaskDescription();

        mPackageName = getIntent().getStringExtra(EXTRA_PACKAGE_NAME);
        mIsRegEx = getIntent().getBooleanExtra(EXTRA_IS_REG_EX, false);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mContent = getIntent().getStringExtra(EXTRA_CONTENT);
        mRecordId = getIntent().getLongExtra(EXTRA_RECORD_ID, -1L);

        setupToolbar();
        setupEditLayout();
        setupRxBus();
    }

    private void setupTaskDescription() {
        setTaskDescription(new ActivityManager.TaskDescription(
                getString(R.string.app_name),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                ContextCompat.getColor(EditActivity.this, R.color.white)
        ));
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleAppearance);
        toolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleAppearance);
        ViewCompat.setElevation(toolbar, 0.0f);

        CharSequence appName = AppInfoUtils.getAppLabel(this, mPackageName);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setSubtitle(AppInfoUtils.isSystemApp(this, mPackageName)
                ? R.string.subtitle_system_app : R.string.subtitle_user_app);

        Drawable appIcon = AppInfoUtils.getAppIcon(this, mPackageName);
        if (appIcon != null && appIcon instanceof BitmapDrawable) {
            int dp30 = (int) DisplayUtils.dp2px(this, 30.0f);
            Bitmap bitmap = ((BitmapDrawable) appIcon).getBitmap();
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, dp30, dp30, false);
            appIcon = new BitmapDrawable(getResources(), resize);
            toolbar.setNavigationIcon(appIcon);
            toolbar.setNavigationOnClickListener(this);
        }
    }

    private void setupEditLayout() {
        EditLayout layout = (EditLayout) findViewById(R.id.edit);
        layout.setEditLayoutListener(this);
        layout.setInfo(mIsRegEx, mTitle, mContent);
    }

    private void setupRxBus() {
        mRemovedSubscription = RxBus.getInstance()
                .toObservable(NotificationRemovedEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NotificationRemovedEvent>() {
                    @Override
                    public void onCompleted() {
                        // DO NOTHING
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NotificationRemovedEvent event) {
                        onNotificationRemovedEvent(event);
                    }
                });
    }

    private void onNotificationRemovedEvent(NotificationRemovedEvent event) {
        if (event.getStatusBarNotification().getPackageName().equals(mPackageName)) {
            if (event.getStatusBarNotification().getId()
                    == event.getStatusBarNotification().getId()) {
                onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dara, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                onClick(null);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        ApplicationInfo info = AppInfoUtils.getAppInfo(this, mPackageName);
        if (info == null) {
            return;
        }

        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", mPackageName);
        intent.putExtra("app_uid", info.uid);
        startActivity(intent);
        onBackPressed();
    }

    @Override
    public void onHashTagsReady(final List<String> titleTags, final List<String> contentTags) {
        Observable.create(
                new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        Record record = new Record();
                        if (mRecordId >= 0) {
                            record.setId(mRecordId);
                        }
                        record.packageName = mPackageName;
                        record.isRegEx = false;
                        record.title = TextUtils.join(" ", titleTags);
                        record.content = TextUtils.join(" ", contentTags);
                        record.save();
                        subscriber.onNext(0);
                        subscriber.onCompleted();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        // DO NOTHING
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(EditActivity.this, R.string.toast_action_failed,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Toast.makeText(EditActivity.this, R.string.toast_action_successful,
                                Toast.LENGTH_SHORT).show();
                        RxBus.getInstance().post(new UpdateRecordEvent());
                        finish();
                    }
                });
    }

    @Override
    public void onRegExReady(final String titleRegEx, final String contentRegEx) {
        Observable.create(
                new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        Record record = new Record();
                        if (mRecordId >= 0) {
                            record.setId(mRecordId);
                        }
                        record.packageName = mPackageName;
                        record.isRegEx = true;
                        record.title = titleRegEx;
                        record.content = contentRegEx;
                        record.save();
                        subscriber.onNext(0);
                        subscriber.onCompleted();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        // DO NOTHING
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(EditActivity.this, R.string.toast_action_failed,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Toast.makeText(EditActivity.this, R.string.toast_action_successful,
                                Toast.LENGTH_SHORT).show();
                        RxBus.getInstance().post(new UpdateRecordEvent());
                        finish();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mRemovedSubscription != null) {
            mRemovedSubscription.unsubscribe();
        }
    }
}

package io.github.mthli.dara.app;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.orm.SugarRecord;

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
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditActivity extends AppCompatActivity
        implements View.OnClickListener, EditLayout.EditLayoutListener {
    public static final String EXTRA = "EXTRA";

    private StatusBarNotification mStatusBarNotification;
    private Toolbar mToolbar;

    private Subscription mRemovedSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setFinishOnTouchOutside(false);
        setupTaskDescription();

        mStatusBarNotification = getIntent().getParcelableExtra(EXTRA);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ((EditLayout) findViewById(R.id.edit)).setEditLayoutListener(this);
        setupToolbar();
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
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleAppearance);
        mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleAppearance);
        ViewCompat.setElevation(mToolbar, 0.0f);

        String packageName = mStatusBarNotification.getPackageName();
        CharSequence appName = AppInfoUtils.getAppLabel(this, packageName);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setSubtitle(AppInfoUtils.isSystemApp(this, packageName)
                ? R.string.subtitle_system_app : R.string.subtitle_user_app);

        Drawable appIcon = AppInfoUtils.getAppIcon(this, packageName);
        if (appIcon != null && appIcon instanceof BitmapDrawable) {
            int dp30 = (int) DisplayUtils.dp2px(this, 30.0f);
            Bitmap bitmap = ((BitmapDrawable) appIcon).getBitmap();
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, dp30, dp30, false);
            appIcon = new BitmapDrawable(getResources(), resize);
            mToolbar.setNavigationIcon(appIcon);
            mToolbar.setNavigationOnClickListener(this);
        }
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
        if (event.getStatusBarNotification().getPackageName()
                .equals(mStatusBarNotification.getPackageName())) {
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
        String packageName = mStatusBarNotification.getPackageName();
        ApplicationInfo info = AppInfoUtils.getAppInfo(this, packageName);
        if (info == null) {
            return;
        }

        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", packageName);
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
                        record.setPackageName(mStatusBarNotification.getPackageName());
                        record.setRegEx(false);
                        record.setTitle(TextUtils.join(" ", titleTags));
                        record.setContent(TextUtils.join(" ", contentTags));
                        SugarRecord.save(record);
                        subscriber.onNext(0);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Toast.makeText(EditActivity.this, R.string.toast_rule_add_successful,
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
                        record.setPackageName(mStatusBarNotification.getPackageName());
                        record.setRegEx(true);
                        record.setTitle(titleRegEx);
                        record.setContent(contentRegEx);
                        SugarRecord.save(record);
                        subscriber.onNext(0);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Toast.makeText(EditActivity.this, R.string.toast_rule_add_successful,
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

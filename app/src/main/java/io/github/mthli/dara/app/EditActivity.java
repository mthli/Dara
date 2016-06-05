package io.github.mthli.dara.app;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.github.mthli.dara.R;
import io.github.mthli.dara.util.DisplayUtils;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST = 0x100;
    public static final int RESPONSE_CONFIRM = 0x101;
    public static final int RESPONSE_CANCEL = 0x102;
    public static final int RESPONSE_DISMISS = 0x103;

    public static final String EXTRA = "EXTRA";

    private StatusBarNotification mNotification;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setFinishOnTouchOutside(false);
        setupTaskDescription();

        mNotification = getIntent().getParcelableExtra(EXTRA);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();
    }

    private void setupTaskDescription() {
        setTaskDescription(new ActivityManager.TaskDescription(
                getString(R.string.app_name),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                ContextCompat.getColor(EditActivity.this, R.color.blue_grey_900)
        ));
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleAppearance);
        mToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleAppearance);
        ViewCompat.setElevation(mToolbar, 0.0f);

        CharSequence appName = null;
        try {
            ApplicationInfo info = getPackageManager()
                    .getApplicationInfo(mNotification.getPackageName(), 0);
            appName = info != null ? getPackageManager().getApplicationLabel(info) : null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setSubtitle(isSystemApp()
                ? R.string.subtitle_system_app : R.string.subtitle_user_app);

        Drawable appIcon = null;
        try {
            appIcon = getPackageManager().getApplicationIcon(mNotification.getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (appIcon != null && appIcon instanceof BitmapDrawable) {
            int dp30 = (int) DisplayUtils.dp2px(this, 30.0f);
            Bitmap bitmap = ((BitmapDrawable) appIcon).getBitmap();
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, dp30, dp30, false);
            appIcon = new BitmapDrawable(getResources(), resize);
            mToolbar.setNavigationIcon(appIcon);
            mToolbar.setNavigationOnClickListener(this);
        }
    }

    private boolean isSystemApp() {
        try {
            ApplicationInfo info = getPackageManager()
                    .getApplicationInfo(mNotification.getPackageName(), 0);
            return (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
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
        ApplicationInfo info;
        try {
            info = getPackageManager().getApplicationInfo(mNotification.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", mNotification.getPackageName());
        intent.putExtra("app_uid", info.uid);
        startActivity(intent);
        // TODO finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESPONSE_CANCEL, null);
        finish();
    }
}

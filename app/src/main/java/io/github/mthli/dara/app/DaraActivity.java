package io.github.mthli.dara.app;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import io.github.mthli.dara.R;
import io.github.mthli.dara.util.DisplayUtils;

public class DaraActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {
    public static final String EXTRA = "EXTRA";

    private StatusBarNotification mNotification;
    private Toolbar mToolbar;
    private SwitchCompat mSwitzh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dara);
        setupTaskDescription();

        mNotification = getIntent().getParcelableExtra(EXTRA);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();
    }

    private void setupTaskDescription() {
        setTaskDescription(new ActivityManager.TaskDescription(
                getString(R.string.app_name),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                ContextCompat.getColor(DaraActivity.this, R.color.blue_grey_900)
        ));
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.text_primary));
        mToolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.text_secondary));

        CharSequence appName = null;
        try {
            ApplicationInfo info = getPackageManager()
                    .getApplicationInfo(mNotification.getPackageName(), 0);
            appName = info != null ? getPackageManager().getApplicationLabel(info) : null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setSubtitle(getString(R.string.subtitle_rules_default));

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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dara, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSwitzh = (SwitchCompat) menu.findItem(R.id.menu_switzh)
                .getActionView().findViewById(R.id.switzh);
        mSwitzh.setOnCheckedChangeListener(this);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        getSupportActionBar().setSubtitle(isChecked
                ? R.string.subtitle_rules_custom : R.string.subtitle_rules_default);
    }
}

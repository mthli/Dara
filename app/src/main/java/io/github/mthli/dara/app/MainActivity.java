package io.github.mthli.dara.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.github.mthli.dara.R;
import io.github.mthli.dara.util.ConstantUtils;
import io.github.mthli.dara.widget.PermissionLayout;

public class MainActivity extends RxAppCompatActivity implements PermissionLayout.PermissionLayoutListener {
    private FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainer = (FrameLayout) findViewById(R.id.container);
        if (!DaraService.sIsAlive) {
            setupPermission();
        } else {

        }
    }

    private void setupPermission() {
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
}
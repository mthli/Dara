package io.github.mthli.dara.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.github.mthli.dara.R;
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
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }

    @Override
    public void onNegativeClick() {
        finish();
    }

    @Override
    public void onNeutralClick() {
        // TODO
    }
}
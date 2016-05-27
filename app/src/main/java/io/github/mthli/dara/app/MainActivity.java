package io.github.mthli.dara.app;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.github.mthli.dara.R;

public class MainActivity extends RxAppCompatActivity {
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
        View view = getLayoutInflater().inflate(R.layout.layout_permission, null, false);
        mContainer.removeAllViews();
        mContainer.addView(view);
    }
}
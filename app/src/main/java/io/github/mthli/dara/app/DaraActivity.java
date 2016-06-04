package io.github.mthli.dara.app;

import android.app.ActivityManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.github.mthli.dara.R;

public class DaraActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private SwitchCompat mSwitzh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dara);
        setupTaskDescription();

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
        // TODO
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dara, menu);
        return true;
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
}

package io.github.mthli.dara.app;

import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.github.mthli.dara.R;

public class MainActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

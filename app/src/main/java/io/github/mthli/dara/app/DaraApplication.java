package io.github.mthli.dara.app;

import android.app.Application;

import com.orm.SugarContext;

public class DaraApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}

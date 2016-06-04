package io.github.mthli.dara.util;

import android.content.Context;
import android.view.ViewConfiguration;

public class DisplayUtils {
    public static float dp2px(Context context, float dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }

    public static boolean hasNavigationBar(Context context) {
        return !ViewConfiguration.get(context).hasPermanentMenuKey();
    }
}

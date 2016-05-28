package io.github.mthli.dara.util;

import android.content.Context;

public class DisplayUtils {
    public static float dp2px(Context context, float dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }
}

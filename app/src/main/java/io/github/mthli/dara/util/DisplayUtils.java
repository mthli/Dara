package io.github.mthli.dara.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

public class DisplayUtils {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static class ShadowOutline extends ViewOutlineProvider {
        private int width;
        private int height;

        public ShadowOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRect(0, 0, width, height);
        }
    }

    public static float dp2px(Context context, float dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}

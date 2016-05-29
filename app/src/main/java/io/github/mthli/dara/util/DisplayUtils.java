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
        private int mWidth;
        private int mHeight;

        public ShadowOutline(int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRect(0, 0, mWidth, mHeight);
        }
    }

    public static float dp2px(Context context, float dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }
}

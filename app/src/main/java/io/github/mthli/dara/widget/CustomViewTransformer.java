package io.github.mthli.dara.widget;

import android.view.View;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.ViewTransformer;

public class CustomViewTransformer implements ViewTransformer {
    @Override
    public void transformView(float translation, float maxTranslation,
                              float peekedTranslation, BottomSheetLayout parent, View view) {
        // DO NOTHING
    }

    @Override
    public float getDimAlpha(float translation, float maxTranslation,
                             float peekedTranslation, BottomSheetLayout parent, View view) {
        float progress = translation / maxTranslation;
        return progress * 0.54f;
    }
}

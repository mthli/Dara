package io.github.mthli.dara.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import io.github.mthli.dara.R;
import io.github.mthli.dara.util.KeyboardUtils;

public class EditRuleLayout extends LinearLayout implements CompoundButton.OnCheckedChangeListener,
        ButtonBarLayout.ButtonBarLayoutListener {
    private SwitchCompat mSwitchRegular;
    private AppCompatEditText mTitleView;
    private AppCompatEditText mContentView;
    private ButtonBarLayout mButtonBarLayout;

    public EditRuleLayout(Context context) {
        super(context);
    }

    public EditRuleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditRuleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCustomEnable(boolean enabled) {
        mSwitchRegular.setEnabled(enabled);
        mTitleView.setEnabled(enabled);
        mContentView.setEnabled(enabled);

        if (enabled) {
            KeyboardUtils.showKeyboard(getContext(), mTitleView);
        } else {
            KeyboardUtils.hideKeyboard(getContext(), mTitleView);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mSwitchRegular = (SwitchCompat) findViewById(R.id.switch_regular);
        mTitleView = (AppCompatEditText) findViewById(R.id.title);
        mContentView = (AppCompatEditText) findViewById(R.id.content);
        mButtonBarLayout = (ButtonBarLayout) findViewById(R.id.button_bar);

        mSwitchRegular.setOnCheckedChangeListener(this);
        mTitleView.setHint(R.string.hint_title_separator);
        mContentView.setHint(R.string.hint_content_separator);
        mButtonBarLayout.setButtonBarLayoutListener(this);
        setCustomEnable(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mTitleView.setHint(isChecked ? R.string.hint_title_regular
                : R.string.hint_title_separator);
        mContentView.setHint(isChecked ? R.string.hint_content_regular
                : R.string.hint_content_separator);
    }

    @Override
    public void onPositiveButtonClick() {
        // TODO
    }
    
    @Override
    public void onNegativeButtonClick() {
        // TODO
    }
    
    @Override
    public void onNeutralButtonClick() {
        // TODO
    }
}

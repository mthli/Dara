package io.github.mthli.dara.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import io.github.mthli.dara.R;
import io.github.mthli.dara.app.EditActivity;
import io.github.mthli.dara.util.RegExUtils;

public class EditLayout extends LinearLayout implements CompoundButton.OnCheckedChangeListener,
        ButtonBarLayout.ButtonBarLayoutListener {
    private AppCompatEditText mTitleView;
    private AppCompatEditText mContentView;
    private boolean mIsRegExMode;

    public EditLayout(Context context) {
        super(context);
    }

    public EditLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleView = (AppCompatEditText) findViewById(R.id.title);
        mContentView = (AppCompatEditText) findViewById(R.id.content);
        SwitchCompat switchRegular = (SwitchCompat) findViewById(R.id.switch_regular);
        ButtonBarLayout buttonBarLayout = (ButtonBarLayout) findViewById(R.id.button_bar);

        mTitleView.setHint(R.string.hint_title_separator);
        mContentView.setHint(R.string.hint_content_separator);
        switchRegular.setOnCheckedChangeListener(this);
        buttonBarLayout.setButtonBarLayoutListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mTitleView.setHint(isChecked ? R.string.hint_title_regular
                : R.string.hint_title_separator);
        mContentView.setHint(isChecked ? R.string.hint_content_regular
                : R.string.hint_content_separator);
        mIsRegExMode = isChecked;
    }

    @Override
    public void onPositiveButtonClick() {
        if (mIsRegExMode) {
            onRegExMode();
        } else {
            onHashTagsMode();
        }
    }

    private void onHashTagsMode() {
        String title = mTitleView.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && RegExUtils.getHashTags(title).isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_rule_title_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String content = mContentView.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && RegExUtils.getHashTags(content).isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_rule_content_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (RegExUtils.getHashTags(title).isEmpty()
                && RegExUtils.getHashTags(content).isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_rule_title_content_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO
    }

    private void onRegExMode() {
        String title = mTitleView.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && !RegExUtils.isRegEx(title)) {
            Toast.makeText(getContext(), R.string.toast_rule_title_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String content = mContentView.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && !RegExUtils.isRegEx(content)) {
            Toast.makeText(getContext(), R.string.toast_rule_content_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!RegExUtils.isRegEx(title) && !RegExUtils.isRegEx(content)) {
            Toast.makeText(getContext(), R.string.toast_rule_title_content_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO
    }

    @Override
    public void onNegativeButtonClick() {
        ((EditActivity) getContext()).onBackPressed();
    }

    @Override
    public void onNeutralButtonClick() {
        // TODO
    }
}

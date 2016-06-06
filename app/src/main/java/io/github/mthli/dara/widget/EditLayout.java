package io.github.mthli.dara.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.app.EditActivity;
import io.github.mthli.dara.util.RegExUtils;

public class EditLayout extends LinearLayout implements CompoundButton.OnCheckedChangeListener,
        ButtonBarLayout.ButtonBarLayoutListener {
    public interface EditLayoutListener {
        void onHashTagsReady(List<String> titleTags, List<String> contentTags);
        void onRegExReady(String titleRegEx, String contentRegEx);
    }

    private static final int TITLE_COUNT_MAX = 20;
    private static final int TITLE_COUNT_MIN = 0;
    private static final int TITLE_COUNT_LIMIT = 5;
    private static final int CONTENT_COUNT_MAX = 20;
    private static final int CONTENT_COUNT_MIN = 0;
    private static final int CONTENT_COUNT_LIMIT = 5;

    private AppCompatEditText mTitleView;
    private AppCompatTextView mTitleCount;
    private AppCompatEditText mContentView;
    private AppCompatTextView mContentCount;

    private EditLayoutListener mEditLayoutListener;
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

    public void setEditLayoutListener(EditLayoutListener editLayoutListener) {
        mEditLayoutListener = editLayoutListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleView = (AppCompatEditText) findViewById(R.id.title);
        mTitleCount = (AppCompatTextView) findViewById(R.id.title_count);
        mContentView = (AppCompatEditText) findViewById(R.id.content);
        mContentCount = (AppCompatTextView) findViewById(R.id.content_count);
        SwitchCompat switchRegular = (SwitchCompat) findViewById(R.id.switch_regular);
        ButtonBarLayout buttonBarLayout = (ButtonBarLayout) findViewById(R.id.button_bar);

        setupTitleView();
        setupContentView();
        switchRegular.setOnCheckedChangeListener(this);
        buttonBarLayout.setButtonBarLayoutListener(this);
    }

    private void setupTitleView() {
        mTitleView.setHint(R.string.hint_title_separator);
        mTitleCount.setText(String.valueOf(TITLE_COUNT_MAX));
        mTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // DO NOTHING
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // DO NOTHING
            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = TITLE_COUNT_MAX - s.length();
                if (count <= TITLE_COUNT_LIMIT) {
                    mTitleCount.setTextColor(ContextCompat
                            .getColor(getContext(), R.color.red_500));
                } else {
                    mTitleCount.setTextColor(ContextCompat
                            .getColor(getContext(), R.color.text_hint));
                }
                mTitleCount.setText(String.valueOf(count));
            }
        });
    }

    private void setupContentView() {
        mContentView.setHint(R.string.hint_content_separator);
        mContentCount.setText(String.valueOf(CONTENT_COUNT_MAX));
        mContentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // DO NOTHING
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // DO NOTHING
            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = CONTENT_COUNT_MAX - s.length();
                if (count <= CONTENT_COUNT_LIMIT) {
                    mContentCount.setTextColor(ContextCompat
                            .getColor(getContext(), R.color.red_500));
                } else {
                    mContentCount.setTextColor(ContextCompat
                            .getColor(getContext(), R.color.text_hint));
                }
                mContentCount.setText(String.valueOf(count));
            }
        });
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
        } else if (TITLE_COUNT_MAX - title.length() < TITLE_COUNT_MIN) {
            Toast.makeText(getContext(), R.string.toast_rule_title_count,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String content = mContentView.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && RegExUtils.getHashTags(content).isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_rule_content_error,
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (CONTENT_COUNT_MAX - content.length() < CONTENT_COUNT_MIN) {
            Toast.makeText(getContext(), R.string.toast_rule_content_count,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (RegExUtils.getHashTags(title).isEmpty()
                && RegExUtils.getHashTags(content).isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_rule_title_content_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (mEditLayoutListener != null) {
            mEditLayoutListener.onHashTagsReady(RegExUtils.getHashTags(title),
                    RegExUtils.getHashTags(content));
        }
    }

    private void onRegExMode() {
        String title = mTitleView.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && !RegExUtils.isRegEx(title)) {
            Toast.makeText(getContext(), R.string.toast_rule_title_error,
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (TITLE_COUNT_MAX - title.length() < TITLE_COUNT_MIN) {
            Toast.makeText(getContext(), R.string.toast_rule_title_count,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String content = mContentView.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && !RegExUtils.isRegEx(content)) {
            Toast.makeText(getContext(), R.string.toast_rule_content_error,
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (CONTENT_COUNT_MAX - content.length() < CONTENT_COUNT_MIN) {
            Toast.makeText(getContext(), R.string.toast_rule_content_count,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!RegExUtils.isRegEx(title) && !RegExUtils.isRegEx(content)) {
            Toast.makeText(getContext(), R.string.toast_rule_title_content_error,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (mEditLayoutListener != null) {
            mEditLayoutListener.onRegExReady(title, content);
        }
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

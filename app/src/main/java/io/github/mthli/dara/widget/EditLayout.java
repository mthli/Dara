package io.github.mthli.dara.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mthli.dara.R;
import io.github.mthli.dara.app.EditActivity;
import io.github.mthli.dara.util.RegExUtils;

public class EditLayout extends LinearLayout implements CompoundButton.OnCheckedChangeListener,
        ButtonBarLayout.ButtonBarLayoutListener {
    private AppCompatEditText mTitleView;
    private AppCompatEditText mContentView;

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
    }

    @Override
    public void onPositiveButtonClick() {
        Log.e("tag", "-> " + checkTitle());
    }
    
    @Override
    public void onNegativeButtonClick() {
        ((EditActivity) getContext()).onBackPressed();
    }
    
    @Override
    public void onNeutralButtonClick() {
        // TODO
    }

    private boolean checkTitle() {
        String title = mTitleView.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            return false;
        }

        Pattern pattern = RegExUtils.getPattern();
        Matcher matcher = pattern.matcher(title);
        return matcher.matches();
    }

    private boolean checkContent() {
        return false;
    }
}

package io.github.mthli.dara.widget.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import io.github.mthli.dara.R;
import io.github.mthli.dara.widget.item.Filter;

public class FilterHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private View mColorView;
    private AppCompatTextView mTitleView;
    private AppCompatTextView mContentView;

    public FilterHolder(View view) {
        super(view);
        mContext = view.getContext();
        mColorView = view.findViewById(R.id.color);
        mTitleView = (AppCompatTextView) view.findViewById(R.id.title);
        mContentView = (AppCompatTextView) view.findViewById(R.id.content);
    }

    public void setFilter(Filter filter) {
        mColorView.setBackgroundColor(filter.getColor());

        ForegroundColorSpan primary = new ForegroundColorSpan(ContextCompat
                .getColor(mContext, R.color.text_primary));
        ForegroundColorSpan secondary = new ForegroundColorSpan(ContextCompat
                .getColor(mContext, R.color.text_secondary));
        String empty = mContext.getString(R.string.filter_empty);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        String title = mContext.getString(R.string.filter_title);
        int next = title.length();
        if (TextUtils.isEmpty(filter.getTitle())) {
            title += empty;
        } else {
            title += filter.getTitle();
        }
        builder.append(title);
        builder.setSpan(primary, 0, next, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(secondary, next, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTitleView.setText(builder);

        builder = new SpannableStringBuilder();
        String content = mContext.getString(R.string.filter_content);
        next = content.length();
        if (TextUtils.isEmpty(filter.getContent())) {
            content += empty;
        } else {
            content += filter.getContent();
        }
        builder.append(content);
        builder.setSpan(primary, 0, next, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(secondary, next, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mContentView.setText(builder);
    }
}

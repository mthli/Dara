package io.github.mthli.dara.widget.holder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.mthli.dara.R;
import io.github.mthli.dara.widget.item.Filter;

public class FilterHolder extends RecyclerView.ViewHolder {
    private View mColorView;
    private AppCompatTextView mTitleView;
    private AppCompatTextView mContentView;

    public FilterHolder(View view) {
        super(view);
        mColorView = view.findViewById(R.id.color);
        mTitleView = (AppCompatTextView) view.findViewById(R.id.title);
        mContentView = (AppCompatTextView) view.findViewById(R.id.content);
    }

    public void setFilter(Filter filter) {
        mColorView.setBackgroundColor(filter.getColor());
        // TODO
        mTitleView.setText(filter.getTitle());
        mContentView.setText(filter.getContent());
    }
}

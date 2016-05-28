package io.github.mthli.dara.widget.holder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.mthli.dara.R;

public class LabelHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView mLabel;

    public LabelHolder(View view) {
        super(view);
        mLabel = (AppCompatTextView) view.findViewById(R.id.label);
    }

    public void setText(CharSequence label) {
        mLabel.setText(label);
    }
}

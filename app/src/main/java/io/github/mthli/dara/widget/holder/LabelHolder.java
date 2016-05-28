package io.github.mthli.dara.widget.holder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.mthli.dara.R;
import io.github.mthli.dara.widget.item.Label;

public class LabelHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView mLabelView;

    public LabelHolder(View view) {
        super(view);
        mLabelView = (AppCompatTextView) view.findViewById(R.id.label);
    }

    public void setLabel(Label label) {
        mLabelView.setText(label.getText());
    }
}

package io.github.mthli.dara.widget.holder;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.mthli.dara.R;
import io.github.mthli.dara.widget.item.Pkg;

public class PkgHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView mPkgView;
    private AppCompatImageButton mActionView;

    public PkgHolder(View view) {
        super(view);
        mPkgView = (AppCompatTextView) view.findViewById(R.id.pkg);
        mActionView = (AppCompatImageButton) view.findViewById(R.id.action);
    }

    public void setPkg(Pkg pkg) {
        // TODO
    }

    public void setAction(View.OnClickListener listener) {
        mActionView.setOnClickListener(listener);
    }
}

package io.github.mthli.dara.widget.holder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.mthli.dara.R;

public class PackageHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView mPkg;
    private AppCompatImageButton mAction;

    public PackageHolder(View view) {
        super(view);
        mPkg = (AppCompatTextView) view.findViewById(R.id.pkg);
        mAction = (AppCompatImageButton) view.findViewById(R.id.action);
    }

    public void setIcon(Drawable drawable) {
        mPkg.setCompoundDrawables(drawable, null, null, null);
    }

    public void setName(CharSequence name) {
        mPkg.setText(name);
    }

    public void setAction(View.OnClickListener listener) {
        mAction.setOnClickListener(listener);
    }
}

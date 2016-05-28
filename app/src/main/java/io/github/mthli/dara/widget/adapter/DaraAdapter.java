package io.github.mthli.dara.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.widget.holder.LabelHolder;
import io.github.mthli.dara.widget.holder.NotifiHolder;
import io.github.mthli.dara.widget.holder.PkgHolder;
import io.github.mthli.dara.widget.item.Label;
import io.github.mthli.dara.widget.item.Pkg;

public class DaraAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LABEL = 0x100;
    private static final int VIEW_TYPE_PACKAGE = 0x101;
    private static final int VIEW_TYPE_NOTIFICATION = 0x102;

    private Context mContext;
    private List<Object> mList;

    public DaraAdapter(Context context, List<Object> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = mList.get(position);

        if (object instanceof Label) {
            return VIEW_TYPE_LABEL;
        } else if (object instanceof Pkg) {
            return VIEW_TYPE_PACKAGE;
        } else {
            return VIEW_TYPE_NOTIFICATION;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_LABEL:
                return onCreateLabelHolder(parent);
            case VIEW_TYPE_PACKAGE:
                return onCreatePkgHolder(parent);
            default:
                return onCreateNotifiHolder(parent);
        }
    }

    private RecyclerView.ViewHolder onCreateLabelHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_label, parent, false);
        return new LabelHolder(view);
    }

    private RecyclerView.ViewHolder onCreatePkgHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pkg, parent, false);
        return new PkgHolder(view);
    }

    private RecyclerView.ViewHolder onCreateNotifiHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_notification, parent, false);
        return new NotifiHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    private void onBindLabelHolder() {

    }

    private void onBindPkgHolder() {

    }

    private void onBindNotificationHolder() {

    }
}

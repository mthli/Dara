package io.github.mthli.dara.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.widget.holder.LabelHolder;
import io.github.mthli.dara.widget.holder.NotifiHolder;
import io.github.mthli.dara.widget.holder.PkgHolder;
import io.github.mthli.dara.widget.item.Label;
import io.github.mthli.dara.widget.item.Notifi;
import io.github.mthli.dara.widget.item.Pkg;

public class DaraAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LABEL = 0x100;
    private static final int VIEW_TYPE_PKG = 0x101;
    private static final int VIEW_TYPE_NOTIFI = 0x102;

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
            return VIEW_TYPE_PKG;
        } else {
            return VIEW_TYPE_NOTIFI;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_LABEL:
                return new LabelHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_label, parent, false));
            case VIEW_TYPE_PKG:
                return new PkgHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_pkg, parent, false));
            default:
                return new NotifiHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_notifi, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object object = mList.get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_LABEL:
                ((LabelHolder) holder).setLabel((Label) object);
                break;
            case VIEW_TYPE_PKG:
                ((PkgHolder) holder).setPkg((Pkg) object);
                break;
            default:
                ((NotifiHolder) holder).setNotifi((Notifi) object);
                break;
        }
    }
}

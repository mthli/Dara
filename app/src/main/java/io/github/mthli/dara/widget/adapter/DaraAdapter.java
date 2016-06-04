package io.github.mthli.dara.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.github.mthli.dara.R;
import io.github.mthli.dara.widget.holder.FilterHolder;
import io.github.mthli.dara.widget.holder.LabelHolder;
import io.github.mthli.dara.widget.holder.NoticeHolder;
import io.github.mthli.dara.widget.item.Filter;
import io.github.mthli.dara.widget.item.Label;
import io.github.mthli.dara.widget.item.Notice;

public class DaraAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_FILTER = 0x100;
    private static final int VIEW_TYPE_LABEL = 0x101;
    private static final int VIEW_TYPE_NOTICE = 0x102;

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

        if (object instanceof Filter) {
            return VIEW_TYPE_FILTER;
        } else if (object instanceof Label) {
            return VIEW_TYPE_LABEL;
        } else {
            return VIEW_TYPE_NOTICE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_FILTER:
                return new FilterHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.recycler_item_filter, parent, false));
            case VIEW_TYPE_LABEL:
                return new LabelHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.recycler_item_label, parent, false));
            default:
                return new NoticeHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.reccyler_item_notice, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object object = mList.get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_FILTER:
                // TODO
                break;
            case VIEW_TYPE_LABEL:
                ((LabelHolder) holder).setLabel((Label) object);
                break;
            default:
                ((NoticeHolder) holder).setNotice((Notice) object);
                break;
        }
    }
}

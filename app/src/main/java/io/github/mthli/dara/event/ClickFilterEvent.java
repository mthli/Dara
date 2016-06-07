package io.github.mthli.dara.event;

import io.github.mthli.dara.widget.item.Filter;

public class ClickFilterEvent {
    private Filter mFilter;

    public ClickFilterEvent(Filter filter) {
        mFilter = filter;
    }

    public Filter getFilter() {
        return mFilter;
    }
}

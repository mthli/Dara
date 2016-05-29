package io.github.mthli.dara.event;

import io.github.mthli.dara.widget.item.Notice;

public class ClickNoticeEvent {
    private Notice mNotice;

    public ClickNoticeEvent(Notice notice) {
        mNotice = notice;
    }

    public Notice getNotifi() {
        return mNotice;
    }
}

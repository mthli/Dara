package io.github.mthli.dara.event;

import io.github.mthli.dara.widget.item.Notifi;

public class ClickNotifiHolderEvent {
    private Notifi mNotifi;

    public ClickNotifiHolderEvent(Notifi notifi) {
        mNotifi = notifi;
    }

    public Notifi getNotifi() {
        return mNotifi;
    }
}

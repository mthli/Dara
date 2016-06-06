package io.github.mthli.dara.widget.item;

public class Filter {
    private int mColor;
    private String mTitle;
    private String mContent;

    public Filter() {
        mColor = 0;
        mTitle = null;
        mContent = null;
    }

    public Filter(int color, String title, String content) {
        mColor = color;
        mTitle = title;
        mContent = content;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}

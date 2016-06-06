package io.github.mthli.dara.widget.item;

public class Filter {
    private int mColor;
    private boolean mIsRegEx;
    private String mTitle;
    private String mContent;

    public Filter() {
        mColor = 0;
        mIsRegEx = false;
        mTitle = null;
        mContent = null;
    }

    public Filter(int color, boolean isRegEx, String title, String content) {
        mColor = color;
        mIsRegEx = isRegEx;
        mTitle = title;
        mContent = content;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public boolean isRegEx() {
        return mIsRegEx;
    }

    public void setRegEx(boolean regEx) {
        mIsRegEx = regEx;
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

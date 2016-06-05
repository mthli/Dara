package io.github.mthli.dara.record;

import android.graphics.Bitmap;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table
public class Record extends SugarRecord {
    private String mPackageName;
    private Bitmap mSmallIcon;
    private Boolean mIsRegularExpression;
    private String mTitle;
    private String mContent;

    public Record() {}

    public Record(String packageName, Bitmap smallIcon, Boolean isRegularExpression,
                  String title, String content) {
        mPackageName = packageName;
        mSmallIcon = smallIcon;
        mIsRegularExpression = isRegularExpression;
        mTitle = title;
        mContent = content;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    public Bitmap getSmallIcon() {
        return mSmallIcon;
    }

    public void setSmallIcon(Bitmap smallIcon) {
        mSmallIcon = smallIcon;
    }

    public Boolean getRegularExpression() {
        return mIsRegularExpression;
    }

    public void setRegularExpression(Boolean regularExpression) {
        mIsRegularExpression = regularExpression;
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

package io.github.mthli.dara.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table
public class Record extends SugarRecord {
    private String mPackageName;
    private Boolean mIsRegularExpression;
    private String mTitle;
    private String mContent;

    public Record() {}

    public Record(String packageName, Boolean isRegularExpression, String title, String content) {
        mPackageName = packageName;
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

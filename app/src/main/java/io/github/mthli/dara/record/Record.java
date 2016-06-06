package io.github.mthli.dara.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table
public class Record extends SugarRecord {
    private String mPackageName;
    private Boolean mIsRegEx;
    private String mTitle;
    private String mContent;

    public Record() {}

    public Record(String packageName, Boolean isRegEx,
                  String title, String content) {
        mPackageName = packageName;
        mIsRegEx = isRegEx;
        mTitle = title;
        mContent = content;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    public Boolean getRegEx() {
        return mIsRegEx;
    }

    public void setRegEx(Boolean regEx) {
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

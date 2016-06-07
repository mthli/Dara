package io.github.mthli.dara.record;

import com.orm.SugarRecord;

public class Record extends SugarRecord {
    public String packageName;
    public Boolean isRegEx;
    public String title;
    public String content;

    public Record() {}
}

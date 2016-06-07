package io.github.mthli.dara.widget.item;

import io.github.mthli.dara.record.Record;

public class Filter {
    private int mColor;
    private Record mRecord;

    public Filter() {
        mColor = 0;
        mRecord = null;
    }

    public Filter(int color, Record record) {
        mColor = color;
        mRecord = record;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public Record getRecord() {
        return mRecord;
    }

    public void setRecord(Record record) {
        mRecord = record;
    }
}

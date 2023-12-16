package com.bergamo.leo.wuake;

public class OptionSelectItem {
    private final String msTitle;
    private final String msVerb;



    public OptionSelectItem(String sTitle, String sVerb) {
        super();
        this.msTitle = sTitle;
        this.msVerb = sVerb;
    }

    public String getTitle() {
        return msTitle;
    }

    public String getVerb() {
        return msVerb;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}

package com.bergamo.leo.wuake;

public class MacroSelectItem {
    private final String msTitle;
    private final String msCommand;



    public MacroSelectItem(String sTitle, String sCommand) {
        super();
        this.msTitle = sTitle;
        this.msCommand = sCommand;
    }

    public String getTitle() {
        return msTitle;
    }

    public String getCommand() {
        return msCommand;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}

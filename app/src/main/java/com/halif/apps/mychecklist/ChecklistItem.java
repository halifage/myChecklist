package com.halif.apps.mychecklist;

public class ChecklistItem {

    private Boolean checkBox;
    private int deleteIcon;
    private String itemText;


    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }

    public int getDeleteIcon() {
        return deleteIcon;
    }

    public String getItemText() {
        return itemText;
    }

    public ChecklistItem(Boolean checkBox, String itemText) {

        this.checkBox = checkBox;
        this.itemText = itemText;
    }
}
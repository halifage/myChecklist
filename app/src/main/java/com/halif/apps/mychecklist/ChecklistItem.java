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

    public String getItemText() {
        return itemText;
    }

    public ChecklistItem(String itemText, Boolean isChecked) {
        this.itemText = itemText;
        checkBox = isChecked;
    }
}

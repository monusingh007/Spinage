package com.spinages.slider.model;


import android.widget.ImageView;

public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int icon_id;


    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title,int icon_id) {
        this.showNotify = showNotify;
        this.title = title;
        this.icon_id = icon_id;

    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon (){ return icon_id; }
    public  void setIcon(int icon_id){this.icon_id = icon_id;}
}

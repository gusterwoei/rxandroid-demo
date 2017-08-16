package com.guster.rxandroiddemo.chat;

/**
 * Created by Gusterwoei on 7/21/17.
 */

public class ListItem {
    public static final int TYPE_SYSTEM = 0;
    public static final int TYPE_USER = 1;

    private int type;
    private String text;

    public ListItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}

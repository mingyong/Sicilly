package xyz.shaohui.sicilly.views.share;

import android.graphics.drawable.Drawable;

/**
 * Created by shaohui on 2016/11/1.
 */

public class ShareIconItem {

    private Drawable icon;

    private String title;

    private String className;

    ShareIconItem(Drawable icon, String title, String className) {
        this.icon = icon;
        this.title = title;
        this.className = className;
    }

}

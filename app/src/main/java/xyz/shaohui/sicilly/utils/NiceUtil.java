package xyz.shaohui.sicilly.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import xyz.shaohui.sicilly.SicillyApplication;

/**
 * Created by kpt on 16/4/7.
 */
public class NiceUtil {

    public static void copyText(String text) {
        ClipboardManager cmb = (ClipboardManager) SicillyApplication
                .getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("label", text);
        MyToast.showToast(SicillyApplication.getContext(), "已复制至剪切版");
        cmb.setPrimaryClip(data);
    }

}

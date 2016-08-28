package xyz.shaohui.sicilly.utils;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by shaohui on 16/8/25.
 */
public class NoUnderlineSpan extends UnderlineSpan {

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }
}

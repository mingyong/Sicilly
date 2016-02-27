package xyz.shaohui.sicilly.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Created by kpt on 16/2/24.
 */
public class HtmlParse {

    public static String cleanAllTag(String html) {
        return Jsoup.clean(html, Whitelist.none());
    }

}

package xyz.shaohui.sicilly.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by shaohui on 16/8/19.
 */
public class HtmlUtils {

    public static String cleanAllTag(String html) {
        Document doc = Jsoup.parse(html);
        return doc.text();
    }

}

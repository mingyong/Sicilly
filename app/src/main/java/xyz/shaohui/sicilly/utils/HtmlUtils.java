package xyz.shaohui.sicilly.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by shaohui on 16/8/19.
 */
public class HtmlUtils {

    public static String cleanAllTag(String html) {
        Document doc = Jsoup.parse(html);
        return doc.text();
    }

    public static String switchTag(String html) {
        Document doc = Jsoup.parse(html);
        Elements links = doc.getElementsByTag("a");
        for (Element link:links) {
            String href = link.attr("href");
            if (href.contains("http://")) {
                link.attr("href", href.replace("http://", "me.shaohui.sicilly.user://"));
            } else {
                link.attr("href", "me.shaohui.sicilly.catalog://" + href);
            }
        }
        return doc.html();
    }

}

package xyz.shaohui.sicilly.utils;

import android.net.Uri;

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
            if (href.contains("http://fanfou.com")) {
                link.attr("href", href.replace("http://", "me.shaohui.sicilly.user://"));
            } else if (href.contains("http://")){
                link.attr("href", href.replace("http://", "me.shaohui.sicilly.http://"));
            } else if (href.startsWith("/")) {
                link.attr("href", "me.shaohui.sicilly.catalog:/" + href);
            }
        }
        return doc.html();
    }

    public static String cleanUserScheme(Uri uri) {
        String uriString = uri.toString();
        return uriString.replace("me.shaohui.sicilly.user://fanfou.com/", "");
    }

    public static String revertHttpScheme(Uri uri) {
        String uriString = uri.toString();
        return uriString.replace("me.shaohui.sicilly.http://", "http://");
    }

}

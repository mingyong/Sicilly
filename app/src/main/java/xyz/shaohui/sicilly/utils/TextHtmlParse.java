package xyz.shaohui.sicilly.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xyz.shaohui.sicilly.SicillyFactory;

/**
 * Created by kpt on 16/3/7.
 */
public class TextHtmlParse {

    public static void setTextView(TextView textView, String str) {
        textView.setText(Html.fromHtml(str));
        Spannable s = new SpannableString(textView.getText());
        s.setSpan(new NoLineClickSpan(), 0, s.length(), Spanned.SPAN_MARK_MARK);
        textView.setText(s);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static class NoLineClickSpan extends ClickableSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            String str = ((TextView) widget).getText().toString();
            MyToast.showToast(widget.getContext(), str);
        }
    }

    public static Spanned updateMainText(String str) {

        Document doc = Jsoup.parse(str);
        Elements urls = doc.select("a");

        for (Element url:urls) {
            String urlHref = url.attr("href");
            if (Validate.isFanFouUrl(urlHref)) {
                String userId = urlToUserId(urlHref);
                url.attr("href", SicillyFactory.PREFIX_USER + userId);
            } else if (Validate.isFanTrendUrl(urlHref)) {
                String trend = urlToTrend(urlHref);
                url.attr("href", SicillyFactory.PREFIX_TREND + trend);
            } else if (Validate.isFanWebUrl(urlHref)){
                String uri = urlToUrl(urlHref);
                url.attr("href", SicillyFactory.PREFIX_WEB + uri);
            }
        }
        return Html.fromHtml(doc.html());
    }

    public static String urlToUserId(String url) {
        String userId = url.trim().substring(SicillyFactory.PREFIX_FAN_INDEX.length());
        return userId;
    }

    public static String urlToTrend(String url) {
        String trend = url.trim().substring(SicillyFactory.PREFIX_FAN_TREND.length());
        return trend;
    }

    public static String urlToUrl(String url) {
        String uri = url.trim().substring(SicillyFactory.PREFIX_FAN_WEB.length());
        return uri;
    }
}

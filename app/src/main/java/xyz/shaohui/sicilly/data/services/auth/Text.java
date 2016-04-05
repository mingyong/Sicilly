package xyz.shaohui.sicilly.data.services.auth;

/**
 * Created by kpt on 16/4/1.
 */
public class Text {

    public static void update(String str) {
        String newStr = "";
        for(int i = 0; i< str.length(); i++) {
            if (str.charAt(i) == ')') {

            }
            newStr = newStr + str.charAt(i);
        }
    }

    public static void format(String s) {
        for (int i=s.length() - 1; i>=0; i--) {
        }
    }

}

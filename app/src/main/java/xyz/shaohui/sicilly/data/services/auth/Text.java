package xyz.shaohui.sicilly.data.services.auth;

import java.util.Stack;

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
        boolean add = false;
        boolean canAdd = false;
        for (int i = 0; i< s.length(); i++) {
            if (s.charAt(i) == '(') {
                add = true;
                canAdd = false;
            }
            if (add && canAdd) {
                char[] chars = s.toCharArray();

            }
        }
    }

    public static void formate(String s) {
        Stack signStack = new Stack() ;
        Stack bracketStack = new Stack();
        for (int i = 0; i< s.length();i++) {
            if (s.charAt(i) == '(') {
                bracketStack.push('(');
            } else if (s.charAt(i) == '+') {
                signStack.push('+');
            }

        }
    }

    public static void add(String s, int index) {

    }

}

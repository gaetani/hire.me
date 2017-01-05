package br.org.hireme.helper;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
/**
 *
 * Given a input string create a random
 * string
 *
 * @author
 * @creation Mar 23, 2015
 *
 */
public class URLTinyMe {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int    BASE     = ALPHABET.length();
    private static final Random DIFF = new Random();

    public static String encode(int num) {
        StringBuilder sb = new StringBuilder();
        while ( num > 0 ) {
            sb.append( ALPHABET.charAt( num % BASE ) );
            num /= BASE;
        }
        return sb.reverse().toString();
    }

    public static String encode(String url) {
       return encode(fromBase26(url));
    }

    public static int decode(String str) {
        int num = 0;
        for ( int i = 0; i < str.length(); i++ )
            num = num * BASE + ALPHABET.indexOf(str.charAt(i));
        return num;
    }

    public static int fromBase26(String s) {
        int res = 0;
        for (Character c : s.toCharArray()) {
            int d = c - 'A' + 1;
            res += DIFF.nextInt(9) * d;
        }
        return res;
    }

}
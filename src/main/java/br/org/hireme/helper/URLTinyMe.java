package br.org.hireme.helper;
import net.openhft.lang.io.DirectBytes;
import net.openhft.lang.io.MappedStore;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);

    public static String encode(int num) {
        StringBuilder sb = new StringBuilder();
        while ( num > 0 ) {
            sb.append( ALPHABET.charAt( num % BASE ) );
            num /= BASE;
        }
        return sb.reverse().toString();
    }

    public static String encode() {
       return encode(nextSequence());
    }

    public static int decode(String str) {
        int num = 0;
        for ( int i = 0; i < str.length(); i++ )
            num = num * BASE + ALPHABET.indexOf(str.charAt(i));
        return num;
    }
    public static int shortKeytoID(String shortKey) {
        int id = 0;
        for (int i = 0; i < shortKey.length(); ++i) {
            if ('a' <= shortKey.charAt(i) && shortKey.charAt(i) <= 'z')
                id = id * 62 + (shortKey.charAt(i) - 'a');
            if ('A' <= shortKey.charAt(i) && shortKey.charAt(i) <= 'Z')
                id = id * 62 + (shortKey.charAt(i) - 'A' + 26);
            if ('0' <= shortKey.charAt(i) && shortKey.charAt(i) <= '9')
                id = id * 62 + (shortKey.charAt(i) - '0' + 52);
        }
        return id;
    }

    public static int nextSequence() {

        return SEQUENCE.getAndIncrement();
    }

    public static int fromBase26(String s) {
        int res = 0;
        for (Character c : s.toCharArray()) {
            int d = c - 'A' + 1;
            res += 3 * d;
        }
        return res;
    }

}
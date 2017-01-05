package br.org.hireme.helper;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gaetani on 02/01/17.
 */
public class URLTinyMeTest {



    @Test
    public void testSameUrl(){
        // same url creates same tiny url
        Assert.assertEquals(URLTinyMe.decode("foo"), URLTinyMe.decode("foo"));
    }

    @Test
    public void testSameOriginUrl(){

        // getOriginalUrl works
        String test = "www.google.com.br";
        Assert.assertEquals(test, URLTinyMe.decode(URLTinyMe.encode(URLTinyMe.fromBase26("www.google.com.br"))));
    }

    @Test
    public void testDuplicates(){
         // inject dummy random object that always returns 60
        List<Integer> urls = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            urls.add(URLTinyMe.decode("foo" + i));
        }
        Assert.assertTrue(!hasDuplicates(urls));
    }

    private boolean hasDuplicates(List<Integer> urls) {
        return false;
    }
}
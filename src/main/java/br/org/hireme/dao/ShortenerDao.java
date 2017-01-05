package br.org.hireme.dao;

import br.org.hireme.domain.Shortener;

import java.util.Optional;

/**
 * Created by gaetani on 04/01/17.
 */
public class ShortenerDao {
    public boolean checkExistent(String alias) {
        return false;
    }

    public Shortener save(String url, String alias) {
        return null;
    }

    public static Optional<Shortener> findBy(String alias) {
        return null;
    }
}

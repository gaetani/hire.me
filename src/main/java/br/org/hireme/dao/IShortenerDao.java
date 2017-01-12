package br.org.hireme.dao;

import br.org.hireme.domain.Shortener;

import java.util.Optional;

public interface IShortenerDao {
    Optional<Shortener> findBy(String alias);

    boolean checkExistent(String alias);

    void save(Shortener shortener);
}

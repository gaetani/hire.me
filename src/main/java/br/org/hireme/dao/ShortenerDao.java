package br.org.hireme.dao;

import br.org.hireme.domain.Shortener;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;

import java.util.Optional;

@Singleton
public class ShortenerDao implements IShortenerDao {
    private Datastore datastore;

    @Inject
    public ShortenerDao(Datastore datastore){
        this.datastore = datastore;
    }

    @Override
    public Optional<Shortener> findBy(String alias) {
        return Optional.ofNullable(datastore.createQuery(Shortener.class).field("alias").equal(alias).get());
    }

    @Override
    public boolean checkExistent(String alias) {
        return datastore.createQuery(Shortener.class).field("alias").equal(alias).countAll() > 0;
    }

    @Override
    public void save(Shortener shortener) {
        datastore.save(shortener);
    }
}

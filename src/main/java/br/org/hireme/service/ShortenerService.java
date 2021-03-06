package br.org.hireme.service;

import br.org.hireme.dao.ISequenceDao;
import br.org.hireme.dao.IShortenerDao;
import br.org.hireme.dao.ShortenerDao;
import br.org.hireme.domain.Shortener;
import br.org.hireme.exception.BusinessException;
import br.org.hireme.exception.domain.CodeError;
import br.org.hireme.helper.URLTinyMe;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.utils.StringUtils;

import java.util.Optional;

public class ShortenerService implements IShortenerService {

    private static final String EMPTY = "";

    private IShortenerDao shortenerDao;
    private ISequenceDao sequenceDao;

    @Inject
    public ShortenerService(IShortenerDao shortenerDao, ISequenceDao sequenceDao){
        this.shortenerDao = shortenerDao;
        this.sequenceDao = sequenceDao;
    }

    @Override
    public Shortener shortIt(String url, Optional<String> optionalAlias){
        optionalAlias.ifPresent(this::validAlias);
        String alias = optionalAlias.orElse(getUniqueAlias(url));
        Shortener shortener = new Shortener();
        shortener.setAlias(alias);
        shortener.setUrl(url);
        shortenerDao.save(shortener);

        return shortener;
    }

    private String getUniqueAlias(String url) {
        String alias = EMPTY;
        alias = URLTinyMe.encode(sequenceDao.nextSequence("shortener").getValue());

        return alias;
    }

    private void validAlias(String alias){
        shortenerDao.findBy(alias).ifPresent(shortener -> {
            throw new BusinessException(CodeError.CUSTOM_ALIAS_ALREADY_EXISTS, alias);
        });
    }

    @Override
    public Shortener getUrl(String alias) {

        Optional<Shortener> shortenerOptional = shortenerDao.findBy(alias);
        return shortenerOptional.orElseThrow(() -> new BusinessException(CodeError.SHORTENED_URL_NOT_FOUND, alias));
    }
}

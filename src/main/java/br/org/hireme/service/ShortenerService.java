package br.org.hireme.service;

import br.org.hireme.dao.ShortenerDao;
import br.org.hireme.domain.Shortener;
import br.org.hireme.exception.BusinessException;
import br.org.hireme.exception.domain.CodeError;
import br.org.hireme.helper.URLTinyMe;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.utils.StringUtils;

import java.util.Optional;

public class ShortenerService {

    private static final String EMPTY = "";

    private ShortenerDao shortenerDao;

    public Shortener shortIt(String url, Optional<String> optionalAlias){
        optionalAlias.ifPresent(this::validAlias);
        String alias = optionalAlias.orElse(getUniqueAlias(url));
        return shortenerDao.save(url, alias);
    }

    private String getUniqueAlias(String url) {
        String alias = EMPTY;
        do{
            alias = URLTinyMe.encode(url);
        } while (shortenerDao.checkExistent(alias));

        return alias;
    }

    private void validAlias(String alias){
        if(alias.equals("invalid")){
            throw new BusinessException(CodeError.CUSTOM_ALIAS_ALREADY_EXISTS);
        }
    }

    public Shortener getUrl(String alias) {

        Optional<Shortener> shortenerOptional = ShortenerDao.findBy(alias);
        return shortenerOptional.orElseThrow(() -> new BusinessException(CodeError.SHORTENED_ALIAS_ALREADY_EXISTS));
    }
}

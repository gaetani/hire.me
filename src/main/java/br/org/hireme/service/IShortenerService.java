package br.org.hireme.service;

import br.org.hireme.domain.Shortener;

import java.util.Optional;

public interface IShortenerService {
    Shortener shortIt(String url, Optional<String> optionalAlias);

    Shortener getUrl(String alias);
}

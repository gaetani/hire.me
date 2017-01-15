package br.org.hireme.dao;

import br.org.hireme.domain.Sequence;

/**
 * Created by gaetani on 15/01/17.
 */
public interface ISequenceDao {
    Sequence nextSequence(String sequenceName);
}

package br.org.hireme.dao;

import br.org.hireme.domain.Sequence;
import com.google.inject.Inject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

/**
 * Created by gaetani on 15/01/17.
 */
public class SequenceDao implements ISequenceDao {
    private Datastore datastore;

    @Inject
    public SequenceDao(Datastore datastore){
        this.datastore = datastore;
    }

    @Override
    public Sequence nextSequence(String sequenceName) {

        Query<Sequence> sequenceQuery = datastore.createQuery(Sequence.class).field("sequenceName").equal(sequenceName);
        UpdateOperations<Sequence> ops = datastore
                .createUpdateOperations(Sequence.class)
                .inc("value", 1);


        return datastore.findAndModify(sequenceQuery, ops, false, true);
    }
}

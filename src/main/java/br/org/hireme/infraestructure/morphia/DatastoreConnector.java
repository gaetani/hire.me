package br.org.hireme.infraestructure.morphia;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.annotation.PostConstruct;

@Singleton
public class DatastoreConnector {

    private final Morphia morphia = new Morphia();
    private Datastore datastore;

    public Datastore getDatastore(){
        return datastore;
    }

    @PostConstruct
    public void connect(){

// tell Morphia where to find your classes
// can be called multiple times with different packages or classes
        morphia.mapPackage("br.org.domain");

// create the Datastore connecting to the default port on the local host
        datastore = morphia.createDatastore(new MongoClient(), "hireme");

        datastore.ensureIndexes();
    }


}

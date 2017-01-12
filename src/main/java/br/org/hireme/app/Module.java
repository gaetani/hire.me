package br.org.hireme.app;

import br.org.hireme.controller.IShortenerController;
import br.org.hireme.controller.ShortenerController;
import br.org.hireme.dao.IShortenerDao;
import br.org.hireme.dao.ShortenerDao;
import br.org.hireme.infraestructure.morphia.DatastoreConnector;
import br.org.hireme.routes.IRoutesDefinition;
import br.org.hireme.routes.RoutesDefinition;
import br.org.hireme.service.IShortenerService;
import br.org.hireme.service.ShortenerService;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.mongodb.morphia.Datastore;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by gaetani on 11/01/17.
 */
public class Module extends AbstractModule implements TypeListener {

    /**
     * Call postconstruct method (if annotation exists).
     */
    @Override
    public <I> void hear(final TypeLiteral<I> type, final TypeEncounter<I> encounter) {
        encounter.register(new InjectionListener<I>() {

            @Override
            public void afterInjection(final I injectee) {
                Arrays.stream(injectee.getClass().getMethods()).filter(method -> method.isAnnotationPresent(PostConstruct.class)).forEach(method -> {
                    try {
                        method.invoke(injectee);
                    } catch (final Exception e) {
                        throw new RuntimeException(String.format("@PostConstruct %s", method), e);
                    }
                });
            }
        });
    }

    @Override
    protected void configure() {


        bind(IShortenerDao.class).to(ShortenerDao.class);
        bind(IShortenerService.class).to(ShortenerService.class);
        bind(IShortenerController.class).to(ShortenerController.class);
        bind(IRoutesDefinition.class).to(RoutesDefinition.class);


        bindListener(Matchers.any(), this);
    }


    @Provides
    @Inject
    public Datastore datastore(DatastoreConnector datastoreConnector){
        return datastoreConnector.getDatastore();
    }
}

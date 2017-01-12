package br.org.hireme.domain;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("shortener")
@Indexes(
        @Index(value = "alias", fields = @Field("alias"))
)
public class Shortener {

    @Id
    private ObjectId id;
    private String url;
    private String alias;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}

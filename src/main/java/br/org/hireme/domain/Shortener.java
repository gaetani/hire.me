package br.org.hireme.domain;


import com.google.gson.annotations.Expose;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexType;

@Entity("shortener")
@Indexes(
        @Index(value = "alias", fields = @Field(value = "alias", type = IndexType.TEXT))
)
public class Shortener {

    @Id
    private ObjectId id;

    @Expose
    private String url;

    @Expose
    @Indexed(options = @IndexOptions(unique = true))
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

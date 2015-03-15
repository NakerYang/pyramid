package edu.neu.ccs.pyramid.feature;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengli on 3/4/15.
 */
@JsonSerialize(using = Feature.Serializer.class)
public class Feature implements Serializable{
    private static final long serialVersionUID = 1L;
    protected int index;
    protected String name = "unknown";
    protected Map<String,String> settings;

    public Feature() {
        this.settings = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Feature{");
        sb.append("index=").append(index);
        sb.append(", name=").append(name);
        sb.append(", settings=").append(settings);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature feature = (Feature) o;

        if (index != feature.index) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return index;
    }

    public static class Serializer extends JsonSerializer<Feature>{
        @Override
        public void serialize(Feature feature, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("index",feature.index);
            jsonGenerator.writeStringField("name",feature.name);
            jsonGenerator.writeEndObject();
        }
    }
}

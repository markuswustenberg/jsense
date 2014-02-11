package org.jsense.serialize.json;

import com.google.gson.*;
import org.joda.time.Instant;

import java.lang.reflect.Type;

/**
 * For converting to and from an {@link org.joda.time.Instant}.
 *
 * @author Markus Wüstenberg
 */
class InstantTypeConverter implements com.google.gson.JsonSerializer<Instant>, com.google.gson.JsonDeserializer<Instant> {

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getMillis());
    }

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Instant(json.getAsLong());
    }
}

package org.jsense.serialize;

import com.google.gson.*;
import org.joda.time.Duration;

import java.lang.reflect.Type;

/**
 * For converting to and from a {@link org.joda.time.Duration}.
 *
 * @author Markus Wüstenberg
 */
class DurationTypeConverter implements com.google.gson.JsonSerializer<Duration>, com.google.gson.JsonDeserializer<Duration> {

    @Override
    public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getMillis());
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Duration(json.getAsLong());
    }
}

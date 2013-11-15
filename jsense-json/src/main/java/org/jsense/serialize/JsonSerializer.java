package org.jsense.serialize;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.*;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * A {@link org.jsense.serialize.Serializer} that serializes to JSON.
 *
 * @param <T> The type of data to be serialized.
 * @author Markus Wüstenberg
 */
public final class JsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeConverter())
            .registerTypeAdapter(Duration.class, new DurationTypeConverter())
            .create();

    private Iterable<T> value;
    private boolean hasData;

    @Override
    public Serializer<T> serialize(Iterable<T> value) {
        this.value = Preconditions.checkNotNull(value);
        hasData = true;
        return this;
    }

    @Override
    public void to(OutputStream out) throws IOException {
        if (!hasData) {
            throw new IllegalStateException("Nothing to serialize.");
        }
        OutputStreamWriter writer = new OutputStreamWriter(Preconditions.checkNotNull(out), Charsets.UTF_8);
        gson.toJson(value, writer);
        writer.flush();
    }

    /**
     * For converting to and from an {@link org.joda.time.Instant}.
     */
    private static class InstantTypeConverter implements com.google.gson.JsonSerializer<Instant>, JsonDeserializer<Instant> {

        @Override
        public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getMillis());
        }

        @Override
        public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            return new Instant(json.getAsLong());
        }
    }

    /**
     * For converting to and from a {@link org.joda.time.Duration}.
     */
    private static class DurationTypeConverter implements com.google.gson.JsonSerializer<Duration>, JsonDeserializer<Duration> {

        @Override
        public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getMillis());
        }

        @Override
        public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            return new Duration(json.getAsLong());
        }
    }
}

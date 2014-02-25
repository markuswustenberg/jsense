package org.jsense.serialize.json;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.jsense.serialize.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * A {@link org.jsense.serialize.Serializer} that serializes to JSON.
 *
 * @param <T> The type of data to be serialized.
 * @author Markus Wüstenberg
 */
@Beta
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
}

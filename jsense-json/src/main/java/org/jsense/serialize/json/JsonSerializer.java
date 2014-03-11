package org.jsense.serialize.json;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
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

    private Iterable<T> values;

    @Override
    public Serializer<T> serialize(Iterable<T> values) {
        this.values = Preconditions.checkNotNull(values);
        return this;
    }

    @Override
    public void to(OutputStream out) throws IOException {
        Preconditions.checkState(values != null, "Nothing to serialize.");
        Preconditions.checkState(!Iterables.isEmpty(values));
        OutputStreamWriter writer = new OutputStreamWriter(Preconditions.checkNotNull(out), Charsets.UTF_8);
        gson.toJson(values, writer);
        writer.flush();
    }
}

package org.jsense.serialize.json;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.Instant;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Deserializes a JSON representation to an {@link org.jsense.AccelerometerEvent}.
 * <p/>
 * It loads the whole stream into memory.
 *
 * @author Markus Wüstenberg
 */
@Beta
public final class AccelerometerEventDeserializer implements Deserializer<AccelerometerEvent> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeConverter())
            .create();

    @Override
    public Iterable<AccelerometerEvent> from(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(Preconditions.checkNotNull(in), Charsets.UTF_8);
        return Arrays.asList(gson.fromJson(reader, AccelerometerEvent[].class));
    }
}

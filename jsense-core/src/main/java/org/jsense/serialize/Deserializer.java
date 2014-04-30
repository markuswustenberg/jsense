package org.jsense.serialize;

import com.google.common.annotations.Beta;

import java.io.Closeable;
import java.io.IOException;

/**
 * A {@code Deserializer} can deserialize data. Its interface draws inspiration from {@link java.io.InputStream}.
 * <p/>
 * A {@code Deserializer} must be closed after use.
 * <p/>
 * The implementing classes are usually not thread-safe.
 *
 * @see org.jsense.serialize.Serializer
 * @param <T> The type of data to be deserialized.
 * @author Markus Wüstenberg
 */
@Beta
public interface Deserializer<T> extends Closeable {

    /**
     * Deserializes data, preferably lazily, while minimizing loading data into memory. This is implementation-specific.
     *
     * @return An {@link java.lang.Iterable} over {@code T}.
     * @throws IOException If there is a problem deserializing. In particular, an {@link java.io.IOException} is thrown if the {@code Deserializer} is closed.
     */
    Iterable<T> deserialize() throws IOException;
}

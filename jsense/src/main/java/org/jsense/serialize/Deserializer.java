package org.jsense.serialize;

import com.google.common.annotations.Beta;

import java.io.IOException;
import java.io.InputStream;

/**
 * A {@code Deserializer} can deserialize data from an {@link java.io.InputStream}.
 * <p/>
 * The implementing classes may or may not be thread-safe.
 *
 * @param <T> The type of data to be deserialized.
 * @author Markus Wüstenberg
 */
@Beta
public interface Deserializer<T> {

    /**
     * Reads serialized data from the {@link java.io.InputStream}.
     *
     * @param in The {@link java.io.InputStream} to read from.
     * @return An {@link java.lang.Iterable} over {@code T}.
     * @throws IOException If there is a problem reading from {@code in}.
     */
    Iterable<T> from(InputStream in) throws IOException;
}

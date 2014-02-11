package org.jsense.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A {@code Deserializer} can deserialize data from an {@link java.io.InputStream}.
 * <p/>
 * The implementing classes may or may not be thread-safe.
 *
 * @param <T> The type of data to be deserialized.
 * @author Markus Wüstenberg
 */
public interface Deserializer<T> {

    /**
     * Reads serialized data from the {@link java.io.InputStream}.
     *
     * @param in The {@link java.io.InputStream} to read from.
     * @throws {@link java.io.IOException} If there is a problem reading from {@code in}.
     */
    Iterable<T> from(InputStream in) throws IOException;
}

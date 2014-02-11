package org.jsense.serialize;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@code Serializer} can serialize data to an {@link java.io.OutputStream}.
 * <p/>
 * The implementing classes may or may not be thread-safe.
 *
 * @param <T> The type of data to be serialized.
 * @author Markus Wüstenberg
 */
public interface Serializer<T> {

    /**
     * Set the data to be serialized.
     *
     * @param value The data.
     * @return The {@code Serializer}, for method chaining.
     */
    Serializer<T> serialize(Iterable<T> value);

    /**
     * Writes serialized data to the {@link java.io.OutputStream} and flushes.
     *
     * @param out The {@link java.io.OutputStream} to write to.
     * @throws {@link java.io.IOException} If there is a problem writing to {@code out}.
     */
    void to(OutputStream out) throws IOException;

}

package org.jsense.serialize;

import com.google.common.annotations.Beta;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * A {@code Serializer} can serialize data. Its interface draws inspiration from {@link java.io.OutputStream}.
 * <p>
 * Depending on the implementation, the data may be buffered before writing, but may always be flushed explicitly. A {@code Serializer} must be closed after use.
 * <p>
 * The implementing classes are usually thread-safe.
 *
 * @see org.jsense.serialize.Deserializer
 * @param <T> The type of data to be serialized.
 * @author Markus Wüstenberg
 */
@Beta
public interface Serializer<T> extends Flushable, Closeable {

    /**
     * Serialize one value.
     *
     * @param value The datum to serialize.
     * @return The {@code Serializer}, for method chaining.
     * @throws IOException If there is a problem serializing. In particular, an {@link java.io.IOException} is thrown if the {@code Serializer} is closed.
     */
    Serializer<T> serialize(T value) throws IOException;

    /**
     * Serialize multiple values.
     *
     * @param values The data to serialize.
     * @return The {@code Serializer}, for method chaining.
     * @throws IOException If there is a problem serializing. In particular, an {@link java.io.IOException} is thrown if the {@code Serializer} is closed.
     */
    Serializer<T> serialize(Iterable<T> values) throws IOException;
}

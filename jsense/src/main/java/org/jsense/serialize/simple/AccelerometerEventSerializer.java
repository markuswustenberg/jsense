package org.jsense.serialize.simple;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Serializer;

import java.io.*;
import java.util.Arrays;

/**
 * A simple {@link org.jsense.serialize.Serializer} that creates a simple delimited representation of
 * {@link org.jsense.AccelerometerEvent}s.
 * <p/>
 * A newline character (\n) is always used between multiple events, so the line separator as defined by the
 * system property <tt>line.separator</tt> is NOT used.
 * <p/>
 * This class is not thread-safe.
 *
 * @author Markus Wüstenberg
 */
@Beta
public final class AccelerometerEventSerializer implements Serializer<AccelerometerEvent> {

    private static final String STANDARD_DELIMITER = ",";

    private static final AccelerometerEventSerializer singleton = new AccelerometerEventSerializer();

    private Iterable<AccelerometerEvent> events;

    @Override
    public Serializer<AccelerometerEvent> serialize(Iterable<AccelerometerEvent> events) {
        this.events = Preconditions.checkNotNull(events);
        return this;
    }

    @Override
    public void to(OutputStream out) throws IOException {
        Preconditions.checkNotNull(out);
        Preconditions.checkState(events != null);
        Preconditions.checkState(!Iterables.isEmpty(events));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8));
        for (AccelerometerEvent event : events) {
            writer.write(event.getAbsoluteTimestamp().getMillis() + STANDARD_DELIMITER);
            writer.write(event.hasRelativeTimestamp() + STANDARD_DELIMITER);
            writer.write(event.getRelativeTimestamp() + STANDARD_DELIMITER);
            writer.write(event.getX() + STANDARD_DELIMITER);
            writer.write(event.getY() + STANDARD_DELIMITER);
            writer.write(event.getZ() + "\n");
        }
        writer.flush();
    }

    /**
     * Serialises an {@link org.jsense.AccelerometerEvent} directly to a {@code String}, using a singleton {@code AccelerometerEventSerializer} internally.
     *
     * @param event The event to serialise.
     * @return The serialised event as a {@code String}.
     */
    public static String serializeToString(AccelerometerEvent event) {
        return serializeToString(Lists.newArrayList(event));
    }

    /**
     * Serialises {@link org.jsense.AccelerometerEvent}s directly to a {@code String}, using a singleton {@code AccelerometerEventSerializer} internally.
     *
     * @param events The events to serialise.
     * @return The serialised events as a {@code String}.
     */
    public static String serializeToString(AccelerometerEvent... events) {
        return serializeToString(Arrays.asList(events));
    }

    /**
     * Serialises {@link org.jsense.AccelerometerEvent}s directly to a {@code String}, using a singleton {@code AccelerometerEventSerializer} internally.
     *
     * @param events The events to serialise.
     * @return The serialised events as a {@code String}.
     */
    public static String serializeToString(Iterable<AccelerometerEvent> events) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            singleton.serialize(events).to(out);
            return out.toString(Charsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException("Error writing to ByteArrayOutputStream.");
        }
    }
}

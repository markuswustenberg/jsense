package org.jsense.serialize;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import org.joda.time.Instant;
import org.jsense.AccelerometerEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * A simple {@link org.jsense.serialize.Deserializer} that parses a simple delimited representation of {@link org.jsense.AccelerometerEvent}s.
 * <p>
 * Currently loads all events into memory.
 * <p>
 * This class is not thread-safe.
 *
 * @see org.jsense.serialize.SimpleAccelerometerEventSerializer
 * @author Markus Wüstenberg
 */
public final class SimpleAccelerometerEventDeserializer implements Deserializer<AccelerometerEvent> {

    private static final String STANDARD_DELIMITER = ",";

    private static final int ABSOLUTE_TIMESTAMP_INDEX = 0;
    private static final int HAS_RELATIVE_TIMESTAMP_INDEX = 1;
    private static final int RELATIVE_TIMESTAMP_INDEX = 2;
    private static final int X_INDEX = 3;
    private static final int Y_INDEX = 4;
    private static final int Z_INDEX = 5;

    private static final String CLOSED_EXCEPTION_MESSAGE = "The Deserializer is closed, no deserializing possible.";

    private final CharSource source;
    private BufferedReader reader;
    private boolean closed;

    public SimpleAccelerometerEventDeserializer(CharSource source) {
        this.source = Preconditions.checkNotNull(source);
    }

    @Override
    public Iterable<AccelerometerEvent> deserialize() throws IOException {
        if (closed) {
            throw new IOException(CLOSED_EXCEPTION_MESSAGE);
        }

        if (reader == null) {
            openSource();
        }

        String line;
        List<AccelerometerEvent> events = Lists.newArrayList();
        while ((line = reader.readLine()) != null) {
            List<String> parts = Splitter.on(STANDARD_DELIMITER).splitToList(line);
            AccelerometerEvent.Builder builder = AccelerometerEvent.newBuilder();
            builder
                    .setAbsoluteTimestamp(new Instant(Long.parseLong(parts.get(ABSOLUTE_TIMESTAMP_INDEX))))
                    .setX(Float.parseFloat(parts.get(X_INDEX)))
                    .setY(Float.parseFloat(parts.get(Y_INDEX)))
                    .setZ(Float.parseFloat(parts.get(Z_INDEX)));
            if (Boolean.parseBoolean(parts.get(HAS_RELATIVE_TIMESTAMP_INDEX))) {
                builder.setRelativeTimestamp(Long.parseLong(parts.get(RELATIVE_TIMESTAMP_INDEX)));
            }
            events.add(builder.build());
        }

        return events;
    }

    @Override
    public void close() throws IOException {
        closed = true;
        if (reader != null) {
            reader.close();
        }
    }

    private void openSource() throws IOException {
        reader = source.openBufferedStream();
    }
}

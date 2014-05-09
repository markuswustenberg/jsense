package org.jsense.serialize;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.joda.time.Instant;
import org.jsense.AccelerometerEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * A simple {@link org.jsense.serialize.Deserializer} that parses a simple delimited representation of {@link org.jsense.AccelerometerEvent}s.
 * <p>
 * Input charset is UTF-8.
 * <p>
 * Input is read with a {@link java.io.BufferedReader} for buffering.
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

    private final InputStream source;
    private BufferedReader reader;
    private boolean closed;

    public SimpleAccelerometerEventDeserializer(InputStream source) {
        this.source = Preconditions.checkNotNull(source);
    }

    @Override
    public Iterable<AccelerometerEvent> deserialize() throws IOException {
        if (closed) {
            throw new IOException(Constants.DESERIALIZER_CLOSED_EXCEPTION_MESSAGE);
        }

        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(source, Charsets.UTF_8));
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
        if (reader != null) {
            reader.close();
        }
        closed = true;
    }
}

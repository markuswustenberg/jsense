package org.jsense.serialize.simple;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.io.CharSource;
import org.joda.time.Instant;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Deserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link org.jsense.serialize.Deserializer} that parses a simple delimited representation of {@link org.jsense.AccelerometerEvent}s.
 * <p/>
 * This class is not thread-safe.
 *
 * @see org.jsense.serialize.simple.AccelerometerEventSerializer
 * @author Markus Wüstenberg
 */
public final class AccelerometerEventDeserializer implements Deserializer<AccelerometerEvent> {

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

    public AccelerometerEventDeserializer(CharSource source) {
        this.source = Preconditions.checkNotNull(source);
    }

    @Override
    public synchronized Iterable<AccelerometerEvent> deserialize() throws IOException {
        if (closed) {
            throw new IOException(CLOSED_EXCEPTION_MESSAGE);
        }

        if (reader == null) {
            openSource();
        }

        // This is dirty: exception tunneling. Maybe find a better way at some point.
        try {
            return new AccelerometerEventIterable(reader);
        } catch (IOExceptionWrapper e) {
            throw e.getIoException();
        }
    }

    @Override
    public synchronized void close() throws IOException {
        closed = true;
        if (reader != null) {
            reader.close();
        }
    }

    private void openSource() throws IOException {
        reader = source.openBufferedStream();
    }

    /**
     * A simple wrapper around {@link java.io.IOException}s that enables tunneling.
     */
    private static class IOExceptionWrapper extends RuntimeException {

        private final IOException e;

        public IOExceptionWrapper(IOException e) {
            this.e = e;
        }

        public IOException getIoException() {
            return e;
        }
    }

    /**
     * An {@link java.lang.Iterable} that returns an {@link org.jsense.serialize.simple.AccelerometerEventDeserializer.AccelerometerEventIterator}.
     */
    private static class AccelerometerEventIterable implements Iterable<AccelerometerEvent> {

        private final BufferedReader reader;

        public AccelerometerEventIterable(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public Iterator<AccelerometerEvent> iterator() {
            return new AccelerometerEventIterator(reader);
        }
    }

    /**
     * An {@code AccelerometerEventIterator} reads the underlying stream without loading everything into memory.
     */
    private static class AccelerometerEventIterator extends AbstractIterator<AccelerometerEvent> {

        private final AccelerometerEvent.Builder builder = AccelerometerEvent.newBuilder();
        private final BufferedReader reader;

        public AccelerometerEventIterator(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        protected AccelerometerEvent computeNext() {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    List<String> parts = Splitter.on(STANDARD_DELIMITER).splitToList(line);
                    builder
                            .setAbsoluteTimestamp(new Instant(Long.parseLong(parts.get(ABSOLUTE_TIMESTAMP_INDEX))))
                            .setX(Float.parseFloat(parts.get(X_INDEX)))
                            .setY(Float.parseFloat(parts.get(Y_INDEX)))
                            .setZ(Float.parseFloat(parts.get(Z_INDEX)));
                    if (Boolean.parseBoolean(parts.get(HAS_RELATIVE_TIMESTAMP_INDEX))) {
                        builder.setRelativeTimestamp(Long.parseLong(parts.get(RELATIVE_TIMESTAMP_INDEX)));
                    }
                    AccelerometerEvent event = builder.build();
                    builder.reset();
                    return event;
                }

                return endOfData();
            } catch (IOException e) {
                throw new IOExceptionWrapper(e);
            }
        }
    }
}

package org.jsense.serialize.simple;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.io.CharSink;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Serializer;

import java.io.IOException;
import java.io.Writer;

/**
 * A simple {@link org.jsense.serialize.Serializer} that creates a simple delimited representation of {@link org.jsense.AccelerometerEvent}s.
 * <p/>
 * A newline character (\n) is always used between multiple events, so the line separator as defined by the system property <tt>line.separator</tt> is NOT used.
 * <p/>
 * This class is thread-safe.
 *
 * @see org.jsense.serialize.simple.AccelerometerEventDeserializer
 * @author Markus Wüstenberg
 */
@Beta
public final class AccelerometerEventSerializer implements Serializer<AccelerometerEvent> {

    private static final String STANDARD_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";

    private static final String CLOSED_EXCEPTION_MESSAGE = "The Serializer is closed, no serializing possible.";

    private final CharSink sink;
    private Writer writer;
    private boolean closed;

    public AccelerometerEventSerializer(CharSink sink) {
        this.sink = Preconditions.checkNotNull(sink);
    }

    @Override
    public synchronized Serializer<AccelerometerEvent> serialize(AccelerometerEvent event) throws IOException {
        Preconditions.checkNotNull(event);

        if (closed) {
            throw new IOException(CLOSED_EXCEPTION_MESSAGE);
        }

        if (writer == null) {
            openSink();
        }

        writeEvent(event);

        return this;
    }

    @Override
    public synchronized Serializer<AccelerometerEvent> serialize(Iterable<AccelerometerEvent> events) throws IOException {
        Preconditions.checkNotNull(events);
        Preconditions.checkState(!Iterables.isEmpty(events));

        if (closed) {
            throw new IOException(CLOSED_EXCEPTION_MESSAGE);
        }

        if (writer == null) {
            openSink();
        }

        for (AccelerometerEvent event : events) {
            writeEvent(event);
        }

        return this;
    }

    @Override
    public synchronized void flush() throws IOException {
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public synchronized void close() throws IOException {
        closed = true;
        if (writer != null) {
            writer.close();
        }
    }

    private void openSink() throws IOException {
        writer = sink.openBufferedStream();
    }

    private void writeEvent(AccelerometerEvent event) throws IOException {
        writer.write(event.getAbsoluteTimestamp().getMillis() + STANDARD_DELIMITER);
        writer.write(event.hasRelativeTimestamp() + STANDARD_DELIMITER);
        writer.write(event.getRelativeTimestamp() + STANDARD_DELIMITER);
        writer.write(event.getX() + STANDARD_DELIMITER);
        writer.write(event.getY() + STANDARD_DELIMITER);
        writer.write(event.getZ() + LINE_SEPARATOR);
    }
}

package org.jsense.serialize;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.jsense.AccelerometerEvent;

import java.io.*;

/**
 * A simple {@link org.jsense.serialize.Serializer} that creates a simple delimited representation of {@link org.jsense.AccelerometerEvent}s.
 * <p>
 * Output charset is UTF-8. A newline character (\n) is always used between multiple events, so the line separator as defined by the system property <tt>line.separator</tt> is NOT
 * used.
 * <p>
 * Output is written with a {@link java.io.BufferedWriter} for buffering.
 * <p>
 * This class is thread-safe.
 *
 * @see org.jsense.serialize.SimpleAccelerometerEventDeserializer
 * @author Markus Wüstenberg
 */
@Beta
public final class SimpleAccelerometerEventSerializer implements Serializer<AccelerometerEvent> {

    private static final String STANDARD_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";

    private final OutputStream sink;
    private Writer writer;
    private boolean closed;

    public SimpleAccelerometerEventSerializer(OutputStream sink) {
        this.sink = Preconditions.checkNotNull(sink);
    }

    @Override
    public synchronized Serializer<AccelerometerEvent> serialize(AccelerometerEvent event) throws IOException {
        Preconditions.checkNotNull(event);

        checkClosed();
        checkAndOpenWriter();

        writeEvent(event);

        return this;
    }

    @Override
    public synchronized Serializer<AccelerometerEvent> serialize(Iterable<AccelerometerEvent> events) throws IOException {
        Preconditions.checkNotNull(events);
        Preconditions.checkState(!Iterables.isEmpty(events));

        checkClosed();
        checkAndOpenWriter();

        for (AccelerometerEvent event : events) {
            writeEvent(event);
        }

        return this;
    }

    @Override
    public synchronized void flush() throws IOException {
        checkClosed();
        checkAndOpenWriter();

        writer.flush();
    }

    @Override
    public synchronized void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
        closed = true;
    }

    private void checkAndOpenWriter() throws IOException {
        if (writer == null) {
            writer = new BufferedWriter(new OutputStreamWriter(sink, Charsets.UTF_8));
        }
    }

    private void writeEvent(AccelerometerEvent event) throws IOException {
        writer.write(event.getAbsoluteTimestamp().getMillis() + STANDARD_DELIMITER);
        writer.write(event.hasRelativeTimestamp() + STANDARD_DELIMITER);
        writer.write(event.getRelativeTimestamp() + STANDARD_DELIMITER);
        writer.write(event.getX() + STANDARD_DELIMITER);
        writer.write(event.getY() + STANDARD_DELIMITER);
        writer.write(event.getZ() + LINE_SEPARATOR);
    }

    private void checkClosed() throws IOException {
        if (closed) {
            throw new IOException(Constants.SERIALIZER_CLOSED_EXCEPTION_MESSAGE);
        }
    }
}

package org.jsense.serialize.protobuf;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteSink;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Serializer;
import org.jsense.serialize.protobuf.gen.ProtoModel;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link org.jsense.serialize.Serializer} that serializes {@link org.jsense.AccelerometerEvent}s into Protocol Buffers format.
 * <p/>
 * This class is thread-safe.
 *
 * @see org.jsense.serialize.protobuf.AccelerometerEventDeserializer
 * @author Markus Wüstenberg
 */
@Beta
public final class AccelerometerEventSerializer implements Serializer<AccelerometerEvent> {

    private static final String CLOSED_EXCEPTION_MESSAGE = "The Serializer is closed, no serializing possible.";

    private final ByteSink sink;
    private OutputStream out;
    private boolean closed;

    private ProtoModel.ThreeAxisSensorEvent.Builder builder = ProtoModel.ThreeAxisSensorEvent.newBuilder();

    public AccelerometerEventSerializer(ByteSink sink) {
        this.sink = Preconditions.checkNotNull(sink);
    }

    @Override
    public synchronized Serializer<AccelerometerEvent> serialize(AccelerometerEvent event) throws IOException {
        Preconditions.checkNotNull(event);

        if (closed) {
            throw new IOException(CLOSED_EXCEPTION_MESSAGE);
        }

        if (out == null) {
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

        if (out == null) {
            openSink();
        }

        for (AccelerometerEvent event : events) {
            writeEvent(event);
        }

        return this;
    }

    @Override
    public synchronized void flush() throws IOException {
        if (closed) {
            throw new IOException(CLOSED_EXCEPTION_MESSAGE);
        }

        if (out != null) {
            out.flush();
        }
    }

    @Override
    public synchronized void close() throws IOException {
        if (!closed && out != null) {
            out.close();
        }
        closed = true;
    }

    private void openSink() throws IOException {
        out = sink.openBufferedStream();
    }

    private void writeEvent(AccelerometerEvent event) throws IOException {
        builder.setAbsoluteTimestamp(event.getAbsoluteTimestamp().getMillis())
                .setX(event.getX())
                .setY(event.getY())
                .setZ(event.getZ());
        if (event.hasRelativeTimestamp()) {
            builder.setRelativeTimestamp(event.getRelativeTimestamp());
        }
        ProtoModel.ThreeAxisSensorEvent proto = builder.build();
        builder.clear();
        proto.writeDelimitedTo(out);
    }
}

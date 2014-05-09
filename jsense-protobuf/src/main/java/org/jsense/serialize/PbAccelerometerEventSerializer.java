package org.jsense.serialize;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.gen.ProtoModel;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link org.jsense.serialize.Serializer} that serializes {@link org.jsense.AccelerometerEvent}s into Protocol Buffers format.
 * <p>
 * This class is thread-safe.
 *
 * @see org.jsense.serialize.PbAccelerometerEventDeserializer
 * @author Markus Wüstenberg
 */
@Beta
public final class PbAccelerometerEventSerializer implements Serializer<AccelerometerEvent> {

    private final OutputStream sink;
    private boolean closed;

    private final ProtoModel.ThreeAxisSensorEvent.Builder builder = ProtoModel.ThreeAxisSensorEvent.newBuilder();

    public PbAccelerometerEventSerializer(OutputStream sink) {
        this.sink = Preconditions.checkNotNull(sink);
    }

    @Override
    public synchronized Serializer<AccelerometerEvent> serialize(AccelerometerEvent event) throws IOException {
        Preconditions.checkNotNull(event);

        checkClosed();

        writeEvent(event);

        return this;
    }

    @Override
    public synchronized Serializer<AccelerometerEvent> serialize(Iterable<AccelerometerEvent> events) throws IOException {
        Preconditions.checkNotNull(events);
        Preconditions.checkState(!Iterables.isEmpty(events));

        checkClosed();

        for (AccelerometerEvent event : events) {
            writeEvent(event);
        }

        return this;
    }

    @Override
    public synchronized void flush() throws IOException {
        checkClosed();

        sink.flush();
    }

    @Override
    public synchronized void close() throws IOException {
        sink.close();
        closed = true;
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
        proto.writeDelimitedTo(sink);
    }

    private void checkClosed() throws IOException {
        if (closed) {
            throw new IOException(Constants.SERIALIZER_CLOSED_EXCEPTION_MESSAGE);
        }
    }
}

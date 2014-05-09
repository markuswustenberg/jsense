package org.jsense.serialize;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.joda.time.Instant;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.gen.ProtoModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A {@link org.jsense.serialize.Deserializer} that deserializes {@link org.jsense.AccelerometerEvent}s from Protocol Buffers format.
 * <p>
 * Currently loads all data into memory.
 * <p>
 * This class is not thread-safe.
 *
 * @see org.jsense.serialize.PbAccelerometerEventSerializer
 * @author Markus Wüstenberg
 */
@Beta
public final class PbAccelerometerEventDeserializer implements Deserializer<AccelerometerEvent> {

    private final InputStream source;
    private boolean closed;

    public PbAccelerometerEventDeserializer(InputStream source) {
        this.source = Preconditions.checkNotNull(source);
    }

    @Override
    public Iterable<AccelerometerEvent> deserialize() throws IOException {
        if (closed) {
            throw new IOException(Constants.DESERIALIZER_CLOSED_EXCEPTION_MESSAGE);
        }

        ProtoModel.ThreeAxisSensorEvent proto;
        AccelerometerEvent.Builder builder = AccelerometerEvent.newBuilder();
        List<AccelerometerEvent> events = Lists.newArrayList();
        while ((proto = ProtoModel.ThreeAxisSensorEvent.parseDelimitedFrom(source)) != null) {
            builder.setAbsoluteTimestamp(new Instant(proto.getAbsoluteTimestamp()))
                    .setX(proto.getX())
                    .setY(proto.getY())
                    .setZ(proto.getZ());
            if (proto.hasRelativeTimestamp()) {
                builder.setRelativeTimestamp(proto.getRelativeTimestamp());
            }
            events.add(builder.build());
            builder.reset();
        }
        return events;
    }

    @Override
    public void close() throws IOException {
        source.close();
        closed = true;
    }
}

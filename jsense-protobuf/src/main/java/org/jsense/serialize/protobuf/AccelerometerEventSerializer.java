package org.jsense.serialize.protobuf;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Serializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link org.jsense.serialize.Serializer} that serializes {@link org.jsense.AccelerometerEvent}s into
 * Protocol Buffers format.
 *
 * This class is not thread-safe.
 *
 * @author Markus Wüstenberg
 */
@Beta
public final class AccelerometerEventSerializer implements Serializer<AccelerometerEvent> {

    private Iterable<AccelerometerEvent> events;
    private boolean hasEvents;

    @Override
    public Serializer<AccelerometerEvent> serialize(Iterable<AccelerometerEvent> values) {
        this.events = Preconditions.checkNotNull(values);
        hasEvents = true;
        return this;
    }

    @Override
    public void to(OutputStream out) throws IOException {
        Preconditions.checkState(hasEvents, "Nothing to serialize.");
        Preconditions.checkNotNull(out);

        ProtoModel.ThreeAxisSensorEvent.Builder builder = ProtoModel.ThreeAxisSensorEvent.newBuilder();
        // Go through the events and write them one at a time, so we don't have to load everything into memory
        for (AccelerometerEvent event : events) {
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
}

package org.jsense.serialize.protobuf;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.joda.time.Instant;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Deserializer;
import org.jsense.serialize.protobuf.gen.ProtoModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A {@link org.jsense.serialize.Deserializer} that deserializes {@link org.jsense.AccelerometerEvent}s from
 * Protocol Buffers format.
 *
 * Currently loads all data into memory.
 *
 * @author Markus Wüstenberg
 */
@Beta
public final class AccelerometerEventDeserializer implements Deserializer<AccelerometerEvent> {
    @Override
    public Iterable<AccelerometerEvent> from(InputStream in) throws IOException {
        Preconditions.checkNotNull(in);

        ProtoModel.ThreeAxisSensorEvent proto;
        AccelerometerEvent.Builder builder = AccelerometerEvent.newBuilder();
        List<AccelerometerEvent> events = Lists.newLinkedList();
        while ((proto = ProtoModel.ThreeAxisSensorEvent.parseDelimitedFrom(in)) != null) {
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
}

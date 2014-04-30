package org.jsense.serialize.protobuf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSink;
import org.jsense.AccelerometerEvent;
import org.jsense.ModelFactory;
import org.jsense.serialize.Serializer;
import org.jsense.serialize.protobuf.gen.ProtoModel;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link org.jsense.serialize.protobuf.AccelerometerEventSerializer}.
 *
 * @author Markus Wüstenberg
 */
public class TestProtocolBuffersSerializers {

    private static final int SEED = 489330;

    private static final float DELTA = 0.0001f;

    private ByteArrayOutputStream out;
    private ByteArrayInputStream in;

    private Serializer<AccelerometerEvent> serializer;

    private AccelerometerEvent event1, event2;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        serializer = new AccelerometerEventSerializer(new ByteSink() {
            @Override
            public OutputStream openStream() throws IOException {
                return out;
            }
        });

        ModelFactory.setSeed(SEED);
        event1 = ModelFactory.newRandomAccelerometerEvent();
        event2 = ModelFactory.newRandomAccelerometerEvent();
    }

    @Test
    public void serializeSingleAccelerometerEvent() throws IOException {
        serializer.serialize(ImmutableList.of(event1));
        serializer.flush();
        in = new ByteArrayInputStream(out.toByteArray());
        ProtoModel.ThreeAxisSensorEvent parsedEvent = ProtoModel.ThreeAxisSensorEvent.parseDelimitedFrom(in);
        validateEvent(event1, parsedEvent);
    }

    @Test
    public void serializeTwoAccelerometerEvents() throws IOException {
        serializer.serialize(ImmutableList.of(event1, event2));
        serializer.flush();
        in = new ByteArrayInputStream(out.toByteArray());
        ProtoModel.ThreeAxisSensorEvent parsedEvent = ProtoModel.ThreeAxisSensorEvent.parseDelimitedFrom(in);
        validateEvent(event1, parsedEvent);
        parsedEvent = ProtoModel.ThreeAxisSensorEvent.parseDelimitedFrom(in);
        validateEvent(event2, parsedEvent);
    }

    private void validateEvent(AccelerometerEvent event, ProtoModel.ThreeAxisSensorEvent parsedEvent) {
        assertNotNull(parsedEvent);
        assertEquals(event.getAbsoluteTimestamp().getMillis(), parsedEvent.getAbsoluteTimestamp());
        assertEquals(event.hasRelativeTimestamp(), parsedEvent.hasRelativeTimestamp());
        if (event.hasRelativeTimestamp()) {
            assertEquals(event.getRelativeTimestamp(), parsedEvent.getRelativeTimestamp());
        }
        assertEquals(event.getX(), parsedEvent.getX(), DELTA);
        assertEquals(event.getY(), parsedEvent.getY(), DELTA);
        assertEquals(event.getZ(), parsedEvent.getZ(), DELTA);
    }

    @Test(expected = NullPointerException.class)
    public void serializeValueCantBeNull() throws IOException {
        serializer.serialize((AccelerometerEvent) null);
    }

    @Test(expected = NullPointerException.class)
    public void serializeValuesCantBeNull() throws IOException {
        serializer.serialize((Iterable<AccelerometerEvent>) null);
    }

    @Test(expected = NullPointerException.class)
    public void sinkCantBeNull() throws IOException {
        new AccelerometerEventSerializer(null);
    }

    @Test(expected = IllegalStateException.class)
    public void serializerMustHaveNonEmptyData() throws IOException {
        serializer.serialize(Lists.<AccelerometerEvent>newArrayList());
    }

    @Test(expected = IOException.class)
    public void cantWriteAfterClose() throws IOException {
        serializer.close();
        serializer.serialize(event1);
    }

    @Test(expected = IOException.class)
    public void cantWriteAfterClose2() throws IOException {
        serializer.close();
        serializer.serialize(Lists.newArrayList(event1));
    }

    @Test(expected = IOException.class)
    public void cantFlushAfterClose() throws IOException {
        serializer.close();
        serializer.flush();
    }
}

package org.jsense.serialize.protobuf;

import com.google.common.collect.ImmutableList;
import org.jsense.AccelerometerEvent;
import org.jsense.ModelFactory;
import org.jsense.serialize.Serializer;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the AccelerometerEventProtocolBuffersSerializer.
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

        serializer = new AccelerometerEventSerializer();

        ModelFactory.setSeed(SEED);
        event1 = ModelFactory.newRandomAccelerometerEvent();
        event2 = ModelFactory.newRandomAccelerometerEvent();
    }

    @Test
    public void serializeSingleAccelerometerEvent() throws IOException {
        serializer.serialize(ImmutableList.of(event1))
                .to(out);
        in = new ByteArrayInputStream(out.toByteArray());
        ProtoModel.ThreeAxisSensorEvent parsedEvent = ProtoModel.ThreeAxisSensorEvent.parseDelimitedFrom(in);
        validateEvent(event1, parsedEvent);
    }

    @Test
    public void serializeTwoAccelerometerEvents() throws IOException {
        serializer.serialize(ImmutableList.of(event1, event2))
                .to(out);
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
    public void serializeValueCantBeNull() {
        serializer.serialize(null);
    }

    @Test(expected = NullPointerException.class)
    public void outputStreamCantBeNull() throws IOException {
        serializer.serialize(ImmutableList.of(event1))
                .to(null);
    }

    @Test(expected = IllegalStateException.class)
    public void serializerMustHaveData() throws IOException {
        serializer.to(new ByteArrayOutputStream());
    }
}

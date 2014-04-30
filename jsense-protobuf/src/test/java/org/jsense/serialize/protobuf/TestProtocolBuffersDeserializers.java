package org.jsense.serialize.protobuf;

import com.google.common.collect.ImmutableList;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.jsense.AccelerometerEvent;
import org.jsense.ModelFactory;
import org.jsense.serialize.Deserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the Protocol Buffers deserializers.
 *
 * @author Markus Wüstenberg
 */
public class TestProtocolBuffersDeserializers {

    private static final int SEED = 88951;
    private static final ReadableInstant ABSOLUTE_TIMESTAMP = new Instant(123L);
    private static final long RELATIVE_TIMESTAMP = 124L;
    private static final float X = 0.1f;
    private static final float Y = 0.2f;
    private static final float Z = 0.3f;

    private AccelerometerEvent event1, event2;
    private Deserializer<AccelerometerEvent> deserializer;

    @Before
    public void setUp() throws IOException {
        ModelFactory.setSeed(SEED);
        event1 = ModelFactory.newRandomAccelerometerEvent();
        event2 = ModelFactory.newRandomAccelerometerEvent();

        deserializer = new AccelerometerEventDeserializer();
    }

    @Test
    public void deserializeSingleAccelerometerEvent() throws IOException {
        InputStream in = getInputStreamFrom(ImmutableList.of(event1));
        Iterable<AccelerometerEvent> events = deserializer.from(in);

        Iterator<AccelerometerEvent> eventsIterator = events.iterator();
        assertTrue(eventsIterator.hasNext());
        assertEquals(event1, eventsIterator.next());
    }

    @Test
    public void deserializeMultipleAccelerometerEvents() throws IOException {
        InputStream in = getInputStreamFrom(ImmutableList.of(event1, event2));
        Iterable<AccelerometerEvent> events = deserializer.from(in);

        Iterator<AccelerometerEvent> eventsIterator = events.iterator();
        assertTrue(eventsIterator.hasNext());
        assertEquals(event1, eventsIterator.next());
        assertTrue(eventsIterator.hasNext());
        assertEquals(event2, eventsIterator.next());
    }

    @Test(expected = NullPointerException.class)
    public void inputStreamCantBeNull() throws IOException {
        deserializer.from(null);
    }

    @Test
    public void deserializeMultipleAccelerometerEventsAndDontKeepState() throws IOException {
        AccelerometerEvent eventWithRelativeTimestamp = AccelerometerEvent.newBuilder()
                .setAbsoluteTimestamp(ABSOLUTE_TIMESTAMP)
                .setRelativeTimestamp(RELATIVE_TIMESTAMP)
                .setX(X)
                .setY(Y)
                .setZ(Z)
                .build();

        AccelerometerEvent eventNoRelativeTimestamp = AccelerometerEvent.newBuilder()
                .setAbsoluteTimestamp(ABSOLUTE_TIMESTAMP)
                .setX(X)
                .setY(Y)
                .setZ(Z)
                .build();

        InputStream in = getInputStreamFrom(ImmutableList.of(eventWithRelativeTimestamp, eventNoRelativeTimestamp));
        Iterator<AccelerometerEvent> events = deserializer.from(in).iterator();
        assertTrue(events.next().hasRelativeTimestamp());
        assertFalse(events.next().hasRelativeTimestamp());
    }

    private InputStream getInputStreamFrom(Iterable<AccelerometerEvent> events) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new AccelerometerEventSerializer()
                .serialize(events)
                .to(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}

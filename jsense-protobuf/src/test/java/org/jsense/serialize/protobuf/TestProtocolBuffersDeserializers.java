package org.jsense.serialize.protobuf;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.jsense.AccelerometerEvent;
import org.jsense.ModelFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Tests the {@link org.jsense.serialize.protobuf.AccelerometerEventDeserializer}.
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

    @Before
    public void setUp() throws IOException {
        ModelFactory.setSeed(SEED);
        event1 = ModelFactory.newRandomAccelerometerEvent();
        event2 = ModelFactory.newRandomAccelerometerEvent();
    }

    @Test
    public void deserializeSingleAccelerometerEvent() throws IOException {
        AccelerometerEventDeserializer deserializer = new AccelerometerEventDeserializer(ByteSource.wrap(getByteArrayFrom(ImmutableList.of(event1))));
        Iterable<AccelerometerEvent> events = deserializer.deserialize();

        Iterator<AccelerometerEvent> eventsIterator = events.iterator();
        assertTrue(eventsIterator.hasNext());
        assertEquals(event1, eventsIterator.next());
    }

    @Test
    public void deserializeMultipleAccelerometerEvents() throws IOException {
        AccelerometerEventDeserializer deserializer = new AccelerometerEventDeserializer(ByteSource.wrap(getByteArrayFrom(ImmutableList.of(event1, event2))));
        Iterable<AccelerometerEvent> events = deserializer.deserialize();

        Iterator<AccelerometerEvent> eventsIterator = events.iterator();
        assertTrue(eventsIterator.hasNext());
        assertEquals(event1, eventsIterator.next());
        assertTrue(eventsIterator.hasNext());
        assertEquals(event2, eventsIterator.next());
    }

    @Test(expected = NullPointerException.class)
    public void sourceCantBeNull() throws IOException {
        new AccelerometerEventDeserializer(null);
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

        ByteSource serialized = ByteSource.wrap(getByteArrayFrom(ImmutableList.of(eventWithRelativeTimestamp, eventNoRelativeTimestamp)));
        AccelerometerEventDeserializer deserializer = new AccelerometerEventDeserializer(serialized);
        Iterable<AccelerometerEvent> events = deserializer.deserialize();
        Iterator<AccelerometerEvent> eventsIterator = events.iterator();

        assertTrue(eventsIterator.next().hasRelativeTimestamp());
        assertFalse(eventsIterator.next().hasRelativeTimestamp());
    }

    private byte[] getByteArrayFrom(Iterable<AccelerometerEvent> events) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        new AccelerometerEventSerializer(new ByteSink() {
            @Override
            public OutputStream openStream() throws IOException {
                return out;
            }
        }).serialize(events).flush();
        return out.toByteArray();
    }
}

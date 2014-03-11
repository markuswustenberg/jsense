package org.jsense;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jsense.serialize.Deserializer;
import org.jsense.serialize.Serializer;
import org.jsense.serialize.simple.AccelerometerEventDeserializer;
import org.jsense.serialize.simple.AccelerometerEventSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link org.jsense.serialize.simple.AccelerometerEventSerializer} and {@link org.jsense.serialize.simple.AccelerometerEventDeserializer}.
 *
 * @author Markus Wüstenberg
 */
public class TestSimpleSerialization {

    private static final String ACCELEROMETER_EVENT_SIMPLE = "3861051329808178389,true,1279970208094231118,0.8522093,0.115404725,0.5891033\n";
    private static final String ACCELEROMETER_EVENTS_SIMPLE = "4100716953860509315,true,4810477348898871349,0.4926042,0.08841491,0.59639716\n"
            + ACCELEROMETER_EVENT_SIMPLE;

    private static final int SEED = 349246;

    private ByteArrayOutputStream out;

    private Serializer<AccelerometerEvent> serializer;
    private Deserializer<AccelerometerEvent> deserializer;

    private AccelerometerEvent event1, event2;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();

        serializer = new AccelerometerEventSerializer();
        deserializer = new AccelerometerEventDeserializer();

        ModelFactory.setSeed(SEED);
        event1 = ModelFactory.newRandomAccelerometerEvent();
        event2 = ModelFactory.newRandomAccelerometerEvent();
    }

    @Test
    public void serializeSingleAccelerometerEvent() throws IOException {
        serializer.serialize(ImmutableList.of(event1))
                .to(out);
        assertEquals(ACCELEROMETER_EVENT_SIMPLE, out.toString(Charsets.UTF_8.name()));
    }

    @Test
    public void serializeTwoAccelerometerEvents() throws IOException {
        serializer.serialize(ImmutableList.of(event2, event1))
                .to(out);
        assertEquals(ACCELEROMETER_EVENTS_SIMPLE, out.toString(Charsets.UTF_8.name()));
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
        serializer.to(out);
    }

    @Test(expected = IllegalStateException.class)
    public void serializerMustHaveNonEmptyData() throws IOException {
        serializer.serialize(Lists.<AccelerometerEvent>newArrayList())
                .to(out);
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

    private InputStream getInputStreamFrom(Iterable<AccelerometerEvent> events) throws IOException {
        serializer
                .serialize(events)
                .to(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}

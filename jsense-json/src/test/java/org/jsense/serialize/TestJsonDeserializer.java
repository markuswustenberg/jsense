package org.jsense.serialize;

import com.google.common.collect.ImmutableList;
import org.jsense.AccelerometerEvent;
import org.jsense.ModelFactory;
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
 * Tests the JSON deserializer.
 *
 * @author Markus Wüstenberg
 */
public class TestJsonDeserializer {

    private static final int SEED = 88951;

    private AccelerometerEvent accelerometerEvent, accelerometerEvent2;
    private AccelerometerEventJsonDeserializer deserializer;

    @Before
    public void setUp() throws IOException {
        ModelFactory.setSeed(SEED);
        accelerometerEvent = ModelFactory.newRandomAccelerometerEvent();
        accelerometerEvent2 = ModelFactory.newRandomAccelerometerEvent();

        deserializer = new AccelerometerEventJsonDeserializer();
    }

    @Test
    public void deserializeSingleAccelerometerEvent() throws IOException {
        InputStream in = getInputStreamFrom(ImmutableList.of(accelerometerEvent));
        Iterable<AccelerometerEvent> events = deserializer.from(in);

        Iterator<AccelerometerEvent> eventsIterator = events.iterator();
        assertTrue(eventsIterator.hasNext());
        assertEquals(accelerometerEvent, eventsIterator.next());
    }

    @Test
    public void deserializeMultipleAccelerometerEvents() throws IOException {
        InputStream in = getInputStreamFrom(ImmutableList.of(accelerometerEvent, accelerometerEvent2));
        Iterable<AccelerometerEvent> events = deserializer.from(in);

        Iterator<AccelerometerEvent> eventsIterator = events.iterator();
        assertTrue(eventsIterator.hasNext());
        assertEquals(accelerometerEvent, eventsIterator.next());
        assertTrue(eventsIterator.hasNext());
        assertEquals(accelerometerEvent2, eventsIterator.next());
    }

    private InputStream getInputStreamFrom(Iterable<AccelerometerEvent> events) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new JsonSerializer<AccelerometerEvent>()
                .serialize(events)
                .to(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}

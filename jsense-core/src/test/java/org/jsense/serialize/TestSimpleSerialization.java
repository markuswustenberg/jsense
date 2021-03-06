package org.jsense.serialize;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jsense.AccelerometerEvent;
import org.jsense.ModelFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link org.jsense.serialize.SimpleAccelerometerEventSerializer} and {@link org.jsense.serialize.SimpleAccelerometerEventDeserializer}.
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

    private AccelerometerEvent event1, event2;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        serializer = new SimpleAccelerometerEventSerializer(out);

        ModelFactory.setSeed(SEED);
        event1 = ModelFactory.newRandomAccelerometerEvent();
        event2 = ModelFactory.newRandomAccelerometerEvent();
    }

    @Test
    public void serializeSingleAccelerometerEvent() throws IOException {
        serializer.serialize(event1);
        serializer.flush();
        assertEquals(ACCELEROMETER_EVENT_SIMPLE, out.toString(Charsets.UTF_8.name()));
    }

    @Test
    public void serializeTwoAccelerometerEvents() throws IOException {
        serializer.serialize(ImmutableList.of(event2, event1));
        serializer.flush();
        assertEquals(ACCELEROMETER_EVENTS_SIMPLE, out.toString(Charsets.UTF_8.name()));
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
        new SimpleAccelerometerEventSerializer(null);
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

    @Test
    public void deserializeSingleAccelerometerEvent() throws IOException {

        Deserializer<AccelerometerEvent> deserializer = new SimpleAccelerometerEventDeserializer(new ByteArrayInputStream(ACCELEROMETER_EVENT_SIMPLE.getBytes(Charsets.UTF_8)));
        Iterable<AccelerometerEvent> events = deserializer.deserialize();

        Iterator<AccelerometerEvent> eventsIterator = events.iterator();
        assertTrue(eventsIterator.hasNext());
        assertEquals(event1, eventsIterator.next());
    }

    @Test
    public void deserializeMultipleAccelerometerEvents() throws IOException {
        Deserializer<AccelerometerEvent> deserializer = new SimpleAccelerometerEventDeserializer(new ByteArrayInputStream(ACCELEROMETER_EVENTS_SIMPLE.getBytes(Charsets.UTF_8)));
        Iterable<AccelerometerEvent> events = deserializer.deserialize();

        Iterator<AccelerometerEvent> eventsIterator = events.iterator();
        assertTrue(eventsIterator.hasNext());
        assertEquals(event2, eventsIterator.next());
        assertTrue(eventsIterator.hasNext());
        assertEquals(event1, eventsIterator.next());
    }

    @Test(expected = NullPointerException.class)
    public void sourceCantBeNull() throws IOException {
        new SimpleAccelerometerEventDeserializer(null);
    }

    @Test(expected = IOException.class)
    public void cantDeserializeAfterClose() throws IOException {
        Deserializer<AccelerometerEvent> deserializer = new SimpleAccelerometerEventDeserializer(new ByteArrayInputStream(ACCELEROMETER_EVENT_SIMPLE.getBytes(Charsets.UTF_8)));
        deserializer.close();
        deserializer.deserialize();
    }
}

package org.jsense.serialize;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import org.jsense.AccelerometerEvent;
import org.jsense.ModelFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the JSON serializer.
 *
 * @author Markus Wüstenberg
 */
public class TestJsonSerializer {

    private static final int SEED = 88951;

    private static final String ACCELEROMETER_EVENT_JSON = "[{\"absoluteTimestamp\":1518034634039666416,"
            + "\"hasRelativeTimestamp\":true,\"relativeTimestamp\":7621743594131350048,\"x\":0.4623987,"
            + "\"y\":0.83463824,\"z\":0.14952731}]";
    private static final String ACCELEROMETER_EVENTS_JSON = "[{\"absoluteTimestamp\":7862477873275847780,\"hasRelativeTimestamp\":true,"
            + "\"relativeTimestamp\":5311795296057979082,\"x\":0.4722004,\"y\":0.4104169,\"z\":0.6774789},"
            + "{\"absoluteTimestamp\":1518034634039666416,\"hasRelativeTimestamp\":true,\"relativeTimestamp\":7621743594131350048,"
            + "\"x\":0.4623987,\"y\":0.83463824,\"z\":0.14952731}]";

    private ByteArrayOutputStream out;

    private JsonSerializer<AccelerometerEvent> accelerometerEventSerializer;

    private AccelerometerEvent accelerometerEvent, accelerometerEvent2;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();

        accelerometerEventSerializer = new JsonSerializer<AccelerometerEvent>();

        ModelFactory.setSeed(SEED);
        accelerometerEvent = ModelFactory.newRandomAccelerometerEvent();
        accelerometerEvent2 = ModelFactory.newRandomAccelerometerEvent();
    }

    @Test
    public void serializeSingleAccelerometerEvent() throws IOException {
        accelerometerEventSerializer.serialize(ImmutableList.of(accelerometerEvent))
                .to(out);
        assertEquals(ACCELEROMETER_EVENT_JSON, out.toString(Charsets.UTF_8.name()));
    }

    @Test
    public void serializeTwoAccelerometerEvents() throws IOException {
        accelerometerEventSerializer.serialize(ImmutableList.of(accelerometerEvent2, accelerometerEvent))
                .to(out);
        assertEquals(ACCELEROMETER_EVENTS_JSON, out.toString(Charsets.UTF_8.name()));
    }

    @Test(expected = NullPointerException.class)
    public void serializeValueCantBeNull() {
        accelerometerEventSerializer.serialize(null);
    }

    @Test(expected = NullPointerException.class)
    public void outputStreamCantBeNull() throws IOException {
        accelerometerEventSerializer.serialize(ImmutableList.of(accelerometerEvent))
                .to(null);
    }

    @Test(expected = IllegalStateException.class)
    public void serializerMustHaveData() throws IOException {
        accelerometerEventSerializer.to(new ByteArrayOutputStream());
    }
}

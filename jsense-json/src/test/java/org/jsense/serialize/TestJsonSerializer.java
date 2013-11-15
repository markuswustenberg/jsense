package org.jsense.serialize;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import org.jsense.AccelerometerEvent;
import org.jsense.AccurateTime;
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

    private static final int SEED = 4934;

    private static final String ACCELEROMETER_EVENT_JSON = "[{\"timestamp\":1273705934278652178,\"x\":0.37998486,\"y\":0.2753954,\"z\":0.33494872}]";
    private static final String ACCELEROMETER_EVENTS_JSON = "[{\"timestamp\":1273705934278652178,\"x\":0.37998486,\"y\":0.2753954,\"z\":0.33494872},"
            + "{\"timestamp\":1273705934278652178,\"x\":0.37998486,\"y\":0.2753954,\"z\":0.33494872}]";
    private static final String ACCURATE_TIME_JSON = "[{\"time\":-1594555656197697542,\"reference\":2490017406319263069}]";

    private ByteArrayOutputStream out;

    private JsonSerializer<AccelerometerEvent> accelerometerEventSerializer;
    private JsonSerializer<AccurateTime> accurateTimeSerializer;

    private AccelerometerEvent accelerometerEvent;
    private AccurateTime accurateTime;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();

        accelerometerEventSerializer = new JsonSerializer<AccelerometerEvent>();
        accurateTimeSerializer = new JsonSerializer<AccurateTime>();

        ModelFactory.setSeed(SEED);
        accelerometerEvent = ModelFactory.newRandomAccelerometerEvent();
        accurateTime = ModelFactory.newRandomAccurateTime();
    }

    @Test
    public void serializeSingleAccelerometerEvent() throws IOException {
        accelerometerEventSerializer.serialize(ImmutableList.of(accelerometerEvent))
                .to(out);
        assertEquals(ACCELEROMETER_EVENT_JSON, out.toString(Charsets.UTF_8.name()));
    }

    @Test
    public void serializeTwoAccelerometerEvents() throws IOException {
        accelerometerEventSerializer.serialize(ImmutableList.of(accelerometerEvent, accelerometerEvent))
                .to(out);
        assertEquals(ACCELEROMETER_EVENTS_JSON, out.toString(Charsets.UTF_8.name()));
    }

    @Test
    public void serializeSingleAccurateTime() throws IOException {
        accurateTimeSerializer.serialize(ImmutableList.of(accurateTime))
                .to(out);
        assertEquals(ACCURATE_TIME_JSON, out.toString(Charsets.UTF_8.name()));
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

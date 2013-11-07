package org.jsense;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the various model classes.
 *
 * @author Markus Wüstenberg (markus@wustenberg.dk)
 */
public class TestModel {

    private static final float X = 0.1f;
    private static final float Y = 0.2f;
    private static final float Z = 0.3f;
    
    private static final float DELTA = 0.0001f;
    
    private static final Instant NOW = Instant.now();
    private static final Duration UPTIME = new Duration(1234L);

    @Test
    public void accelerometerEvent() {
        // Basics

        AccelerometerEvent accelerometerEvent = AccelerometerEvent.newBuilder()
                .setTimestamp(NOW)
                .setX(X)
                .setY(Y)
                .setZ(Z)
                .build();

        assertTrue(NOW.isEqual(accelerometerEvent.getTimestamp()));
        assertEquals(X, accelerometerEvent.getX(), DELTA);
        assertEquals(Y, accelerometerEvent.getY(), DELTA);
        assertEquals(Z, accelerometerEvent.getZ(), DELTA);
    }

    @Test(expected = IllegalStateException.class)
    public void accelerometerEventNoTimestamp() {
        AccelerometerEvent.newBuilder()
                .setX(X)
                .setY(Y)
                .setZ(Z)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void accelerometerEventNoX() {
        AccelerometerEvent.newBuilder()
                .setTimestamp(NOW)
                .setY(Y)
                .setZ(Z)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void accelerometerEventNoY() {
        AccelerometerEvent.newBuilder()
                .setTimestamp(NOW)
                .setX(X)
                .setZ(Z)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void accelerometerEventNoZ() {
        AccelerometerEvent.newBuilder()
                .setTimestamp(NOW)
                .setX(X)
                .setY(Y)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void accelerometerEventNullPointer() {
        AccelerometerEvent.newBuilder()
                .setTimestamp(null);
    }

    @Test
    public void accurateTime() {
        AccurateTime accurateTime = AccurateTime.newBuilder()
                .setTime(NOW)
                .setReference(UPTIME)
                .build();

        assertTrue(NOW.isEqual(accurateTime.getTime()));
        assertTrue(UPTIME.isEqual(accurateTime.getReference()));
    }

    @Test(expected = IllegalStateException.class)
    public void accurateTimeNoTime() {
        AccurateTime.newBuilder()
                .setReference(UPTIME)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void accurateTimeNoReference() {
        AccurateTime.newBuilder()
                .setTime(NOW)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void accurateTimeTimeNullPointer() {
        AccurateTime.newBuilder()
                .setTime(null);
    }

    @Test(expected = NullPointerException.class)
    public void accurateTimeReferenceNullPointer() {
        AccurateTime.newBuilder()
                .setReference(null);
    }
}

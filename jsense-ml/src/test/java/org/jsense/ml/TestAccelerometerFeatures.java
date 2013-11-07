package org.jsense.ml;

import com.google.common.collect.ImmutableList;
import org.joda.time.Instant;
import org.jsense.AccelerometerEvent;
import org.jsense.PhysicsConstants;
import org.jsense.ml.feature.MagnitudeMaximum;
import org.jsense.ml.feature.MagnitudeMinimum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A class for testing all accelerometer features.
 *
 * @author Markus Wüstenberg
 */
public class TestAccelerometerFeatures {

    private static final float DELTA = 0.0001f;

    private static final float X = PhysicsConstants.GRAVITY_EARTH;
    private static final float Y = 0;
    private static final float Z = 0;

    private static final float LARGER_X = 0;
    private static final float LARGER_Y = 2 * PhysicsConstants.GRAVITY_EARTH;
    private static final float LARGER_Z = 0;

    private static final Instant NOW = Instant.now();

    private static final AccelerometerEvent SMALL_EVENT = AccelerometerEvent.newBuilder()
            .setTimestamp(NOW)
            .setX(X)
            .setY(Y)
            .setZ(Z)
            .build();

    private static final AccelerometerEvent LARGE_EVENT = AccelerometerEvent.newBuilder()
            .setTimestamp(NOW)
            .setX(LARGER_X)
            .setY(LARGER_Y)
            .setZ(LARGER_Z)
            .build();

    private static final ImmutableList<AccelerometerEvent> EVENTS = ImmutableList.of(SMALL_EVENT, LARGE_EVENT);

    @Test
    public void testMagnitudeMinimum() {
        MagnitudeMinimum magnitudeMinimum = new MagnitudeMinimum();
        assertEquals(0, magnitudeMinimum.compute(EVENTS), DELTA);
    }

    @Test
    public void testMagnitudeMaximum() {
        MagnitudeMaximum magnitudeMaximum = new MagnitudeMaximum();
        assertEquals(PhysicsConstants.GRAVITY_EARTH, magnitudeMaximum.compute(EVENTS), DELTA);
    }
}

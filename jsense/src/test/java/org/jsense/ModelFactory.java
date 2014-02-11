package org.jsense;

import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.Random;

/**
 * Creates test instances of various model classes.
 *
 * @author Markus Wüstenberg
 */
public abstract class ModelFactory {

    private static Random random = new Random();

    public static void setSeed(int seed) {
        ModelFactory.random = new Random(seed);
    }

    public static AccelerometerEvent newRandomAccelerometerEvent() {
        return AccelerometerEvent.newBuilder()
                .setAbsoluteTimestamp(new Instant(random.nextLong()))
                .setX(random.nextFloat())
                .setY(random.nextFloat())
                .setZ(random.nextFloat())
                .build();
    }

    public static AccurateTime newRandomAccurateTime() {
        return AccurateTime.newBuilder()
                .setTime(new Instant(random.nextLong()))
                .setReference(new Duration(random.nextLong()))
                .build();
    }

}

package org.jsense;

import com.sun.org.apache.regexp.internal.recompile;
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
        boolean hasRelativeTimestamp = random.nextBoolean();

        AccelerometerEvent.Builder builder = AccelerometerEvent.newBuilder();
        if (hasRelativeTimestamp) {
            long relativeTimestamp;
            do {
                relativeTimestamp = random.nextLong();
            } while (relativeTimestamp < 0);
            builder.setRelativeTimestamp(relativeTimestamp);
        }

        long absoluteTimestamp;
        do {
            absoluteTimestamp = random.nextLong();
        } while (absoluteTimestamp < 0);

        return builder
                .setAbsoluteTimestamp(new Instant(absoluteTimestamp))
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

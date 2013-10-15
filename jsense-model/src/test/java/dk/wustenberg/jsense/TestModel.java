package dk.wustenberg.jsense;

import static org.junit.Assert.*;

import org.joda.time.Instant;
import org.junit.Test;

/**
 * Tests for the various model classes.
 *
 * @author Markus Wüstenberg (markus@wustenberg.dk)
 */
public class TestModel {

    private static final Instant NOW = Instant.now();

    @Test
    public void accelerometerEvent() {
        // Basics

        AccelerometerEvent accelerometerEvent = AccelerometerEvent.newBuilder()
                .setTimestamp(NOW)
                .setX(0.1f)
                .setY(0.2f)
                .setZ(0.3f)
                .build();

        assertEquals(NOW, accelerometerEvent.getTimestamp());
        assertEquals(0.1f, accelerometerEvent.getX(), 0.1f);
        assertEquals(0.2f, accelerometerEvent.getY(), 0.1f);
        assertEquals(0.3f, accelerometerEvent.getZ(), 0.1f);
    }

    @Test(expected = IllegalStateException.class)
    public void accelerometerEventNoTimestamp() {
        AccelerometerEvent.newBuilder()
                .setX(0.1f)
                .setY(0.2f)
                .setZ(0.3f)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void accelerometerEventNoX() {
        AccelerometerEvent.newBuilder()
                .setTimestamp(NOW)
                .setY(0.2f)
                .setZ(0.3f)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void accelerometerEventNoY() {
        AccelerometerEvent.newBuilder()
                .setTimestamp(NOW)
                .setX(0.1f)
                .setZ(0.3f)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void accelerometerEventNoZ() {
        AccelerometerEvent.newBuilder()
                .setTimestamp(NOW)
                .setX(0.1f)
                .setY(0.2f)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void testAcceleterometerEventNullPointer() {
        AccelerometerEvent.newBuilder()
                .setTimestamp(null);
    }
}

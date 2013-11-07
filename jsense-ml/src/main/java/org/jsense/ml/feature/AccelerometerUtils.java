package org.jsense.ml.feature;

import org.jsense.AccelerometerEvent;
import org.jsense.PhysicsConstants;

/**
 * Utility class for working with the accelerometer.
 *
 * @author Markus Wüstenberg
 */
final class AccelerometerUtils {

    private AccelerometerUtils() {

    }

    static float computeMagnitude(AccelerometerEvent event) {
        return (float) Math.sqrt(Math.pow(event.getX(), 2) + Math.pow(event.getY(), 2) + Math.pow(event.getZ(), 2))
                - PhysicsConstants.GRAVITY_EARTH;
    }
}

package org.jsense.ml.feature;

import com.google.common.annotations.Beta;
import org.jsense.AccelerometerEvent;

/**
 * This interface defines a feature of an accelerometer, tied to {@link AccelerometerEvent}.
 *
 * @param <T> The type of result.
 * @author Markus Wüstenberg
 */
@Beta
public interface AccelerometerFeature<T> {

    /**
     * Get the name of this feature.
     *
     * @return the name
     */
    String getName();

    /**
     * Returns the computed feature based on the passed events.
     *
     * @param events the events to compute on.
     * @return the result of the computation.
     */
    T compute(Iterable<AccelerometerEvent> events);

}

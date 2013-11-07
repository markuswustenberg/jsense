package org.jsense.ml.feature;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.jsense.AccelerometerEvent;

/**
 * @author Markus Wüstenberg
 */
@Beta
public final class MagnitudeMaximum implements AccelerometerFeature<Float> {

    private static final String NAME = "magnitude_maximum";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Float compute(Iterable<AccelerometerEvent> events) {
        Preconditions.checkNotNull(events);
        Preconditions.checkArgument(!Iterables.isEmpty(events), "No events.");

        float maxMagnitude = Float.MIN_VALUE;
        for (AccelerometerEvent event : events) {
            maxMagnitude = Math.max(maxMagnitude, AccelerometerUtils.computeMagnitude(event));
        }

        return maxMagnitude;
    }
}

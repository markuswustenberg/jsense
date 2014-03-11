package org.jsense.serialize.simple;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.joda.time.Instant;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Deserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * A simple {@link org.jsense.serialize.Deserializer} that parses a simple delimited representation of
 * {@link org.jsense.AccelerometerEvent}s.
 *
 * Currently loads all events into memory.
 *
 * @author Markus Wüstenberg
 */
public class AccelerometerEventDeserializer implements Deserializer<AccelerometerEvent> {

    private static final String STANDARD_DELIMITER = ",";

    private static final int ABSOLUTE_TIMESTAMP_INDEX = 0;
    private static final int HAS_RELATIVE_TIMESTAMP_INDEX = 1;
    private static final int RELATIVE_TIMESTAMP_INDEX = 2;
    private static final int X_INDEX = 3;
    private static final int Y_INDEX = 4;
    private static final int Z_INDEX = 5;

    @Override
    public Iterable<AccelerometerEvent> from(InputStream in) throws IOException {
        Preconditions.checkNotNull(in);

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));
        String line;
        List<AccelerometerEvent> events = Lists.newArrayList();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(STANDARD_DELIMITER);
            AccelerometerEvent.Builder builder = AccelerometerEvent.newBuilder();
            builder
                    .setAbsoluteTimestamp(new Instant(Long.parseLong(parts[ABSOLUTE_TIMESTAMP_INDEX])))
                    .setX(Float.parseFloat(parts[X_INDEX]))
                    .setY(Float.parseFloat(parts[Y_INDEX]))
                    .setZ(Float.parseFloat(parts[Z_INDEX]));
            if (Boolean.parseBoolean(parts[HAS_RELATIVE_TIMESTAMP_INDEX])) {
                builder.setRelativeTimestamp(Long.parseLong(parts[RELATIVE_TIMESTAMP_INDEX]));
            }
            events.add(builder.build());
        }

        return events;
    }
}

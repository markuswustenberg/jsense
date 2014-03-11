package org.jsense.serialize.simple;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Serializer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * A simple {@link org.jsense.serialize.Serializer} that creates a simple delimited representation of
 * {@link org.jsense.AccelerometerEvent}s.
 *
 *
 * A newline character (\n) is always used between multiple events, so the line separator as defined by the
 * system property <tt>line.separator</tt> is NOT used.
 *
 * @author Markus Wüstenberg
 */
@Beta
public final class AccelerometerEventSerializer implements Serializer<AccelerometerEvent> {

    private static final String STANDARD_DELIMITER = ",";

    private Iterable<AccelerometerEvent> values;

    @Override
    public Serializer<AccelerometerEvent> serialize(Iterable<AccelerometerEvent> values) {
        this.values = Preconditions.checkNotNull(values);
        return this;
    }

    @Override
    public void to(OutputStream out) throws IOException {
        Preconditions.checkNotNull(out);
        Preconditions.checkState(values != null);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8));
        for (AccelerometerEvent event : values) {
            writer.write(event.getAbsoluteTimestamp().getMillis() + STANDARD_DELIMITER);
            writer.write(event.hasRelativeTimestamp() + STANDARD_DELIMITER);
            writer.write(event.getRelativeTimestamp() + STANDARD_DELIMITER);
            writer.write(event.getX() + STANDARD_DELIMITER);
            writer.write(event.getY() + STANDARD_DELIMITER);
            writer.write(event.getZ() + "\n");
        }
        writer.flush();
    }
}

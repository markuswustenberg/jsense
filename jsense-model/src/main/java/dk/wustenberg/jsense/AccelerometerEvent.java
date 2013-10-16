package dk.wustenberg.jsense;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.joda.time.Instant;

/**
 * An {@code AccelerometerEvent} is a sample from a three-axis accelerometer, with an accurate timestamp.
 *
 * @author Markus Wüstenberg (markus@wustenberg.dk)
 */
public final class AccelerometerEvent {

    private final Instant timestamp;
    private final float x, y, z;

    private AccelerometerEvent(Builder builder) {
        timestamp = builder.timestamp;
        x = builder.x;
        y = builder.y;
        z = builder.z;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccelerometerEvent that = (AccelerometerEvent) o;

        if (Float.compare(that.x, x) != 0) {
            return false;
        }
        if (Float.compare(that.y, y) != 0) {
            return false;
        }
        if (Float.compare(that.z, z) != 0) {
            return false;
        }
        if (!timestamp.equals(that.timestamp)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timestamp, x, y, z);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("timestamp", timestamp)
                .add("x", x)
                .add("y", y)
                .add("z", z)
                .toString();
    }

    /**
     * A {@code Builder} for the AccelerometerEvent.
     */
    public static final class Builder {

        private Instant timestamp;
        private float x, y, z;

        private boolean hasTimestamp, hasX, hasY, hasZ;

        public Builder setTimestamp(Instant timestamp) {
            this.timestamp = Preconditions.checkNotNull(timestamp);
            hasTimestamp = true;
            return this;
        }

        public Builder setX(float x) {
            this.x = x;
            hasX = true;
            return this;
        }

        public Builder setY(float y) {
            this.y = y;
            hasY = true;
            return this;
        }

        public Builder setZ(float z) {
            this.z = z;
            hasZ = true;
            return this;
        }

        public AccelerometerEvent build() {
            if (!hasTimestamp || !hasX || !hasY || !hasZ) {
                throw new IllegalStateException("One of timestamp, x, y, or z hasn't been set.");
            }
            return new AccelerometerEvent(this);
        }
    }

}

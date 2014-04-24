package org.jsense;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;

/**
 * An {@code AccelerometerEvent} is a sample from a three-axis accelerometer, with an accurate absolute timestamp in
 * milliseconds, and an optional relative timestamp in nanoseconds.
 * <p/>
 * Sample values are in m/s^2. The absolute timestamp is often for synchronising with other samples, and the relative
 * timestamp is for comparing with samples from the same dataset with higher precision.
 * <p/>
 * This class is thread-safe and immutable.
 *
 * @author Markus Wüstenberg
 */
@Beta
public final class AccelerometerEvent {

    private final Instant absoluteTimestamp;
    private final boolean hasRelativeTimestamp;
    private final long relativeTimestamp;
    private final float x, y, z;

    private AccelerometerEvent(Builder builder) {
        absoluteTimestamp = builder.absoluteTimestamp;
        hasRelativeTimestamp = builder.hasRelativeTimestamp;
        if (hasRelativeTimestamp) {
            relativeTimestamp = builder.relativeTimestamp;
        } else {
            relativeTimestamp = 0;
        }
        x = builder.x;
        y = builder.y;
        z = builder.z;
    }

    /**
     * Get the absolute timestamp in milliseconds as an {@link org.joda.time.Instant}.
     *
     * @return The absolute timestamp.
     */
    public Instant getAbsoluteTimestamp() {
        return absoluteTimestamp;
    }

    /**
     * Return if there exists a relative timestamp.
     *
     * @return If a relative timestamp exists.
     */
    public boolean hasRelativeTimestamp() {
        return hasRelativeTimestamp;
    }

    /**
     * Get the relative timestamp in nanoseconds. This only makes sense if {@link #hasRelativeTimestamp()} returns true,
     * and throws an {@link java.lang.IllegalStateException} if called anyway.
     *
     * @return The relative timestamp.
     */
    public long getRelativeTimestamp() {
        Preconditions.checkState(hasRelativeTimestamp, "No relative timestamp exists.");
        return relativeTimestamp;
    }

    /**
     * Get the x-axis sample value in m/s^2.
     *
     * @return The x-axis sample value in m/s^2.
     */
    public float getX() {
        return x;
    }

    /**
     * Get the y-axis sample value in m/s^2.
     *
     * @return The y-axis sample value in m/s^2.
     */
    public float getY() {
        return y;
    }

    /**
     * Get the z-axis sample value in m/s^2.
     *
     * @return The z-axis sample value in m/s^2.
     */
    public float getZ() {
        return z;
    }

    /**
     * Get a new {@link Builder} for building an {@code AccelerometerEvent}.
     *
     * @return A new {@link Builder}.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccelerometerEvent that = (AccelerometerEvent) o;

        if (hasRelativeTimestamp != that.hasRelativeTimestamp) {
            return false;
        }
        if (relativeTimestamp != that.relativeTimestamp) {
            return false;
        }
        if (Float.compare(that.x, x) != 0) {
            return false;
        }
        if (Float.compare(that.y, y) != 0) {
            return false;
        }
        if (Float.compare(that.z, z) != 0) {
            return false;
        }
        if (!absoluteTimestamp.equals(that.absoluteTimestamp)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(absoluteTimestamp, hasRelativeTimestamp, relativeTimestamp, x, y, z);
    }

    @Override
    public String toString() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(this)
                .add("absoluteTimestamp", absoluteTimestamp)
                .add("hasRelativeTimestamp", hasRelativeTimestamp);

        if (hasRelativeTimestamp) {
            toStringHelper.add("relativeTimestamp", relativeTimestamp);
        }

        return toStringHelper
                .add("x", x)
                .add("y", y)
                .add("z", z)
                .toString();
    }

    /**
     * A {@code Builder} for the {@link AccelerometerEvent}.
     */
    public static final class Builder {

        private Instant absoluteTimestamp;
        private long relativeTimestamp;
        private float x, y, z;
        private boolean hasAbsoluteTimestamp, hasRelativeTimestamp, hasX, hasY, hasZ;

        public Builder setAbsoluteTimestamp(ReadableInstant absoluteTimestamp) {
            Preconditions.checkNotNull(absoluteTimestamp);
            this.absoluteTimestamp = absoluteTimestamp.toInstant();
            hasAbsoluteTimestamp = true;
            return this;
        }

        public Builder setRelativeTimestamp(long relativeTimestamp) {
            this.relativeTimestamp = relativeTimestamp;
            hasRelativeTimestamp = true;
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
            Preconditions.checkState(hasAbsoluteTimestamp && hasX && hasY && hasZ, "One of absoluteTimestamp, x, y, or z hasn't been set.");
            return new AccelerometerEvent(this);
        }
    }

}

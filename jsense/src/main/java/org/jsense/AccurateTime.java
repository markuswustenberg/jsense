package org.jsense;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

/**
 * {@code AccurateTime} is a way to reference an absolute, accurate time with a relative time, like uptime.
 * <p/>
 * This class is thread-safe and immutable.
 *
 * @author Markus Wüstenberg
 */
@Beta
public final class AccurateTime {

    private final Instant time;
    private final Duration reference;

    private AccurateTime(Builder builder) {
        time = builder.time;
        reference = builder.reference;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Instant getTime() {
        return time;
    }

    public Duration getReference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccurateTime that = (AccurateTime) o;

        if (!reference.equals(that.reference)) {
            return false;
        }
        if (!time.equals(that.time)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(time, reference);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("time", time)
                .add("reference", reference)
                .toString();
    }

    /**
     * A {@code Builder} for the AccurateTime.
     */
    public static final class Builder {

        private Instant time;
        private Duration reference;
        private boolean hasTime, hasReference;

        public Builder setTime(ReadableInstant time) {
            Preconditions.checkNotNull(time);
            this.time = time.toInstant();
            hasTime = true;
            return this;
        }

        public Builder setReference(ReadableDuration reference) {
            Preconditions.checkNotNull(reference);
            this.reference = reference.toDuration();
            hasReference = true;
            return this;
        }

        public AccurateTime build() {
            if (!hasTime || !hasReference) {
                throw new IllegalStateException("One of time or reference hasn't been set.");
            }
            return new AccurateTime(this);
        }

    }
}

package org.jsense.serialize.protobuf;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.io.ByteSource;
import org.joda.time.Instant;
import org.jsense.AccelerometerEvent;
import org.jsense.serialize.Deserializer;
import org.jsense.serialize.protobuf.gen.ProtoModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * A {@link org.jsense.serialize.Deserializer} that deserializes {@link org.jsense.AccelerometerEvent}s from Protocol Buffers format.
 * <p/>
 * This class is not thread-safe.
 *
 * @see org.jsense.serialize.protobuf.AccelerometerEventSerializer
 * @author Markus Wüstenberg
 */
@Beta
public final class AccelerometerEventDeserializer implements Deserializer<AccelerometerEvent> {

    private static final String CLOSED_EXCEPTION_MESSAGE = "The Deserializer is closed, no deserializing possible.";

    private final ByteSource source;
    private InputStream in;
    private boolean closed;

    public AccelerometerEventDeserializer(ByteSource source) {
        this.source = Preconditions.checkNotNull(source);
    }

    @Override
    public synchronized Iterable<AccelerometerEvent> deserialize() throws IOException {
        if (closed) {
            throw new IOException(CLOSED_EXCEPTION_MESSAGE);
        }

        if (in == null) {
            openSource();
        }

        // This is dirty: exception tunneling. Maybe find a better way at some point.
        try {
            return new AccelerometerEventIterable(in);
        } catch (IOExceptionWrapper e) {
            throw e.getIoException();
        }
    }

    @Override
    public synchronized void close() throws IOException {
        closed = true;
        if (in != null) {
            in.close();
        }
    }

    private void openSource() throws IOException {
        in = source.openBufferedStream();
    }

    /**
     * A simple wrapper around {@link java.io.IOException}s that enables tunneling.
     */
    private static class IOExceptionWrapper extends RuntimeException {

        private final IOException e;

        public IOExceptionWrapper(IOException e) {
            this.e = e;
        }

        public IOException getIoException() {
            return e;
        }
    }

    /**
     * An {@link java.lang.Iterable} that returns an {@link org.jsense.serialize.protobuf.AccelerometerEventDeserializer.AccelerometerEventIterator}.
     */
    private static class AccelerometerEventIterable implements Iterable<AccelerometerEvent> {

        private final InputStream in;

        public AccelerometerEventIterable(InputStream in) {
            this.in = in;
        }

        @Override
        public Iterator<AccelerometerEvent> iterator() {
            return new AccelerometerEventIterator(in);
        }
    }

    /**
     * An {@code AccelerometerEventIterator} reads the underlying stream without loading everything into memory.
     */
    private static class AccelerometerEventIterator extends AbstractIterator<AccelerometerEvent> {

        private final AccelerometerEvent.Builder builder = AccelerometerEvent.newBuilder();
        private final InputStream in;

        public AccelerometerEventIterator(InputStream in) {
            this.in = in;
        }

        @Override
        protected AccelerometerEvent computeNext() {
            try {
                ProtoModel.ThreeAxisSensorEvent proto;
                while ((proto = ProtoModel.ThreeAxisSensorEvent.parseDelimitedFrom(in)) != null) {
                    builder.setAbsoluteTimestamp(new Instant(proto.getAbsoluteTimestamp()))
                            .setX(proto.getX())
                            .setY(proto.getY())
                            .setZ(proto.getZ());
                    if (proto.hasRelativeTimestamp()) {
                        builder.setRelativeTimestamp(proto.getRelativeTimestamp());
                    }
                    AccelerometerEvent event = builder.build();
                    builder.reset();
                    return event;
                }

                return endOfData();
            } catch (IOException e) {
                throw new IOExceptionWrapper(e);
            }
        }
    }
}

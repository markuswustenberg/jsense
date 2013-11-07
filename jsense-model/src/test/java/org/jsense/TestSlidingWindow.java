package org.jsense;

import com.google.common.collect.ImmutableList;
import org.jsense.compute.SampleBasedSlidingWindow;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Tests everything related to the sliding window(s).
 *
 * @author Markus Wüstenberg
 */
public class TestSlidingWindow {

    private static final int FIRST_DUMMY_SAMPLE = 1;
    private static final int SECOND_DUMMY_SAMPLE = 2;
    private static final int THIRD_DUMMY_SAMPLE = 3;
    private static final int FOURTH_DUMMY_SAMPLE = 4;
    private static final int FIFTH_DUMMY_SAMPLE = 5;
    private static final int SIXTH_DUMMY_SAMPLE = 6;
    private static final int SEVENTH_DUMMY_SAMPLE = 7;
    private static final int WINDOW_SIZE = 2;

    @Test
    public void testSampleBasedSlidingWindow() {

        SampleBasedSlidingWindow<DummySample> slidingWindow = SampleBasedSlidingWindow.<DummySample>newBuilder()
                .setSize(WINDOW_SIZE)
                .add(ImmutableList.of(new DummySample(FIRST_DUMMY_SAMPLE)))
                .add(ImmutableList.of(new DummySample(SECOND_DUMMY_SAMPLE), new DummySample(THIRD_DUMMY_SAMPLE)))
                .add(ImmutableList.<DummySample>of())
                .add(ImmutableList.of(new DummySample(FOURTH_DUMMY_SAMPLE), new DummySample(FIFTH_DUMMY_SAMPLE)))
                .add(new DummySample(SIXTH_DUMMY_SAMPLE), new DummySample(SEVENTH_DUMMY_SAMPLE))
                .build();

        Iterator<Iterable<DummySample>> slidingWindowIterator = slidingWindow.iterator();

        // Check the first window
        assertTrue(slidingWindowIterator.hasNext());
        Iterable<DummySample> firstWindow = slidingWindowIterator.next();
        checkWindow(firstWindow, FIRST_DUMMY_SAMPLE, SECOND_DUMMY_SAMPLE);

        // Second window
        assertTrue(slidingWindowIterator.hasNext());
        Iterable<DummySample> secondWindow = slidingWindowIterator.next();
        checkWindow(secondWindow, THIRD_DUMMY_SAMPLE, FOURTH_DUMMY_SAMPLE);

        // Third window
        assertTrue(slidingWindowIterator.hasNext());
        Iterable<DummySample> thirdWindow = slidingWindowIterator.next();
        checkWindow(thirdWindow, FIFTH_DUMMY_SAMPLE, SIXTH_DUMMY_SAMPLE);

        // Fourth and last window
        assertTrue(slidingWindowIterator.hasNext());
        Iterable<DummySample> fourthWindow = slidingWindowIterator.next();
        Iterator<DummySample> fourthWindowIterator = fourthWindow.iterator();
        assertTrue(fourthWindowIterator.hasNext());
        DummySample current = fourthWindowIterator.next();
        assertEquals(SEVENTH_DUMMY_SAMPLE, current.number);
        assertFalse(fourthWindowIterator.hasNext());

        // Check that no more windows are present
        assertFalse(slidingWindowIterator.hasNext());
    }

    private void checkWindow(Iterable<DummySample> window, int value1, int value2) {
        Iterator<DummySample> windowIterator = window.iterator();
        assertTrue(windowIterator.hasNext());
        DummySample current = windowIterator.next();
        assertEquals(value1, current.number);
        assertTrue(windowIterator.hasNext());
        current = windowIterator.next();
        assertEquals(value2, current.number);
        assertFalse(windowIterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void sizeCantBeNegative() {
        SampleBasedSlidingWindow.newBuilder()
                .setSize(-1);
    }

    @Test(expected = NullPointerException.class)
    public void firstAddCantBeNull() {
        SampleBasedSlidingWindow.newBuilder()
                .add(null);
    }

    @Test(expected = NullPointerException.class)
    public void varargCantBeNull() {
        SampleBasedSlidingWindow.newBuilder()
                .add(new DummySample(FIRST_DUMMY_SAMPLE), (Object[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void secondVarargCantBeNull() {
        SampleBasedSlidingWindow.newBuilder()
                .add(new DummySample(FIRST_DUMMY_SAMPLE), new DummySample(SECOND_DUMMY_SAMPLE), null);
    }

    @Test(expected = IllegalStateException.class)
    public void illegalStateIfNoSize() {
        SampleBasedSlidingWindow.newBuilder()
                .add(new DummySample(FIRST_DUMMY_SAMPLE))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void illegalStateIfNoData() {
        SampleBasedSlidingWindow.newBuilder()
                .setSize(WINDOW_SIZE)
                .build();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorShouldBeUnmodifiable() {
        SampleBasedSlidingWindow<DummySample> slidingWindow = SampleBasedSlidingWindow.<DummySample>newBuilder()
                .setSize(WINDOW_SIZE)
                .add(new DummySample(FIRST_DUMMY_SAMPLE))
                .build();

        Iterator<Iterable<DummySample>> iterator = slidingWindow.iterator();
        iterator.next();
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void windowIteratorShouldBeUnmodifiable() {
        SampleBasedSlidingWindow<DummySample> slidingWindow = SampleBasedSlidingWindow.<DummySample>newBuilder()
                .setSize(WINDOW_SIZE)
                .add(new DummySample(FIRST_DUMMY_SAMPLE))
                .build();

        Iterator<Iterable<DummySample>> iterator = slidingWindow.iterator();
        Iterable<DummySample> samples = iterator.next();
        Iterator<DummySample> iterator2 = samples.iterator();
        iterator2.remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoMoreWindows() {
        SampleBasedSlidingWindow<DummySample> slidingWindow = SampleBasedSlidingWindow.<DummySample>newBuilder()
                .setSize(WINDOW_SIZE)
                .add(new DummySample(FIRST_DUMMY_SAMPLE))
                .build();

        Iterator<Iterable<DummySample>> iterator = slidingWindow.iterator();

        iterator.next();
        iterator.next();
    }

    /**
     * A mock sample used for testing.
     */
    private static class DummySample {

        private final int number;

        public DummySample(int number) {
            this.number = number;
        }

    }
}

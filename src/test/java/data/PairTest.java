package data;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.Assert;
import org.junit.Test;

public class PairTest {

    private static final Object LEFT = new Object();
    private static final Object RIGHT = new Object();

    private final Pair<?, ?> pair = Pair.of(LEFT, RIGHT);

    @Test
    public void leftReturnsTheLeftComponent() {
        Assert.assertEquals(LEFT, pair.left());
    }

    @Test
    public void rightReturnsTheRightComponent() {
        Assert.assertEquals(RIGHT, pair.right());
    }

    @Test
    public void ofLeftReturnsAPartiallyBuiltPairWhichContainsTheGivenElementOnTheLeft() {
        Assert.assertEquals(LEFT, Pair.ofLeft(LEFT).apply(RIGHT).left());
    }

    @Test
    public void ofRightReturnsAPartiallyBuiltPairWhichContainsTheGivenElementOnTheRight() {
        Assert.assertEquals(RIGHT, Pair.ofRight(RIGHT).apply(LEFT).right());
    }

    @Test
    public void withLeftReplacesTheLeftComponent() {
        final Object newLeft = new Object();
        final Pair<?, ?> modifiedPair = pair.withLeft(newLeft);
        Assert.assertEquals(newLeft, modifiedPair.left());
    }

    @Test
    public void withLeftLeavesTheRightComponentUnchanged() {
        final Object newLeft = new Object();
        final Pair<?, ?> modifiedPair = pair.withLeft(newLeft);
        Assert.assertEquals(RIGHT, modifiedPair.right());
    }

    @Test
    public void withRightReplacesTheRightComponent() {
        final Object newRight = new Object();
        final Pair<?, ?> modifiedPair = pair.withRight(newRight);
        Assert.assertEquals(newRight, modifiedPair.right());
    }

    @Test
    public void withRightLeavesTheLeftComponentUnchanged() {
        final Object newRight = new Object();
        final Pair<?, ?> modifiedPair = pair.withRight(newRight);
        Assert.assertEquals(LEFT, modifiedPair.left());
    }

    @Test
    public void flipMovesLeftToRight() {
        Assert.assertEquals(LEFT, pair.flip().right());
    }

    @Test
    public void flipMovesRightToLeft() {
        Assert.assertEquals(RIGHT, pair.flip().left());
    }

    @Test
    public void mapLeftPassesTheLeftComponentToTheMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        pair.mapLeft(capture::getAndSet);
        Assert.assertEquals(LEFT, capture.get());
    }

    @Test
    public void mapLeftReplacesTheLeftComponentWithTheOneReturnedByTheMapper() {
        final Object mappedLeft = new Object();
        final Pair mappedPair = pair.mapLeft(left -> mappedLeft);
        Assert.assertEquals(mappedLeft, mappedPair.left());
    }

    @Test
    public void mapLeftLeavesTheRightComponentUnchanged() {
        final Pair mappedPair = pair.mapLeft(left -> null);
        Assert.assertEquals(RIGHT, mappedPair.right());
    }

    @Test
    public void mapRightPassesTheRightComponentToTheMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        pair.mapRight(capture::getAndSet);
        Assert.assertEquals(RIGHT, capture.get());
    }

    @Test
    public void mapRightReplacesTheRightComponentWithTheOneReturnedByTheMapper() {
        final Object mappedRight = new Object();
        final Pair mappedPair = pair.mapRight(right -> mappedRight);
        Assert.assertEquals(mappedRight, mappedPair.right());
    }

    @Test
    public void mapRightLeavesTheLeftComponentUnchanged() {
        final Pair mappedPair = pair.mapRight(right -> null);
        Assert.assertEquals(LEFT, mappedPair.left());
    }

    @Test
    public void mapPassesTheLeftComponentToTheLeftMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        pair.map(capture::getAndSet, right -> null);
        Assert.assertEquals(LEFT, capture.get());
    }

    @Test
    public void mapPassesTheRightComponentToTheRightMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        pair.map(left -> null, capture::getAndSet);
        Assert.assertEquals(RIGHT, capture.get());
    }

    @Test
    public void mapReplacesTheLeftComponentWithTheOneReturnedByTheLeftMapper() {
        final Object mappedLeft = new Object();
        final Pair mappedPair = pair.map(left -> mappedLeft, right -> null);
        Assert.assertEquals(mappedLeft, mappedPair.left());
    }

    @Test
    public void mapReplacesTheRightComponentWithTheOneReturnedByTheRightMapper() {
        final Object mappedRight = new Object();
        final Pair mappedPair = pair.map(left -> null, right -> mappedRight);
        Assert.assertEquals(mappedRight, mappedPair.right());
    }

    @Test
    public void mapLeftMapperCanReturnNull() {
        final Pair mappedPair = pair.map(left -> null, right -> null);
        Assert.assertNull(mappedPair.left());
    }

    @Test
    public void mapRightMapperCanReturnNull() {
        final Pair mappedPair = pair.map(left -> null, right -> null);
        Assert.assertNull(mappedPair.right());
    }

    @Test(expected = IllegalStateException.class)
    public void mapPropagatesTheExceptionThrownByTheLeftMapper() {
        pair.map(left -> {
            throw new IllegalStateException();
        }, right -> null);
    }

    @Test(expected = IllegalStateException.class)
    public void mapPropagatesTheExceptionThrownByTheRightMapper() {
        pair.map(left -> null, right -> {
            throw new IllegalStateException();
        });
    }

    @Test
    public void pairsAreEqualWhenBothTheirLeftAndRightComponentsAreRespectivelyEqual() {
        final Pair one = Pair.of(LEFT, RIGHT);
        final Pair other = Pair.of(LEFT, RIGHT);
        Assert.assertTrue(one.equals(other));
    }

    @Test
    public void pairsAreNotEqualWhenLeftComponentsAreNotEqual() {
        final Pair one = Pair.of(new Object(), RIGHT);
        final Pair other = Pair.of(new Object(), RIGHT);
        Assert.assertFalse(one.equals(other));
    }

    @Test
    public void pairsAreNotEqualWhenRightComponentsAreNotEqual() {
        final Pair one = Pair.of(LEFT, new Object());
        final Pair other = Pair.of(RIGHT, new Object());
        Assert.assertFalse(one.equals(other));
    }

    @Test
    public void pairsOfNullValuesAreEqual() {
        final Pair one = Pair.of(null, null);
        final Pair other = Pair.of(null, null);
        Assert.assertTrue(one.equals(other));
    }
}

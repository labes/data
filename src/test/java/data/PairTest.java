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
}

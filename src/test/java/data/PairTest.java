package data;

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
}

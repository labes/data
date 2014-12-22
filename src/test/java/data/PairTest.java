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
}

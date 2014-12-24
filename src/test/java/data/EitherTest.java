package data;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.Assert;
import org.junit.Test;

public class EitherTest {

    private static final Object VALUE = new Object();

    private final Either left = Either.left(VALUE);
    private final Either right = Either.right(VALUE);

    @Test
    public void foldOnLeftPassesTheValueToTheLeftMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        left.fold(capture::getAndSet, null);
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void foldOnRightPassesTheValueToTheRightMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        right.fold(null, capture::getAndSet);
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void foldOnLeftReturnsTheResultOfTheLeftMapper() {
        final Object result = new Object();
        Assert.assertEquals(result, left.fold(value -> result, null));
    }

    @Test
    public void foldOnRightReturnsTheResultOfTheRightMapper() {
        final Object result = new Object();
        Assert.assertEquals(result, right.fold(null, value -> result));
    }
}

package data;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.Assert;
import org.junit.Test;

public class EitherTest {

    private static final Object VALUE = new Object();

    private final Either<Object, Object> left = Either.left(VALUE);
    private final Either<Object, Object> right = Either.right(VALUE);

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

    @Test
    public void isLeftOnLeftReturnsTrue() {
        Assert.assertTrue(left.isLeft());
    }

    @Test
    public void isLeftOnRightReturnsFalse() {
        Assert.assertFalse(right.isLeft());
    }

    @Test
    public void isRightOnRightReturnsTrue() {
        Assert.assertTrue(right.isRight());
    }

    @Test
    public void isRightOnLeftReturnsFalse() {
        Assert.assertFalse(left.isRight());
    }

    @Test
    public void flipOnLeftReturnsRightWithSameValue() {
        Assert.assertEquals(right, left.flip());
    }

    @Test
    public void flipOnRightReturnsLeftWithSameValue() {
        Assert.assertEquals(left, right.flip());
    }

    @Test
    public void mapOnLeftDoesNotApplyTheMapper() {
        left.map(null);
    }

    @Test
    public void mapOnRightPassesTheValueToTheMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        right.map(capture::getAndSet);
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void mapOnLeftLeavesTheComponentUnchanged() {
        Assert.assertEquals(left, left.map(value -> null));
    }

    @Test
    public void mapOnRightReplacesTheRightComponentWithTheOneReturnedByTheMapper() {
        final Object mappedValue = new Object();
        Assert.assertEquals(Either.right(mappedValue), right.map(value -> mappedValue));
    }

    @Test
    public void flatMapOnLeftLeavesTheComponentUnchanged() {
        Assert.assertEquals(left, left.flatMap(null));
    }

    @Test
    public void flatMapOnRightPassesTheValueToTheMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        right.flatMap(value -> {
            capture.set(value);
            return null;
        });
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void flatMapOnRightReturnsTheEitherReturnedByTheMapper() {
        final Either<Object, Object> result = Either.right(new Object());
        Assert.assertEquals(result, right.flatMap(value -> result));
    }

    @Test
    public void joinOnLeftReturnsTheOuterEither() {
        final Object value = new Object();
        final Either<Object, Either<Object, Object>> outer = Either.left(value);
        Assert.assertEquals(Either.left(value), Either.join(outer));
    }

    @Test
    public void joinOnRightReturnsTheInnerEither() {
        final Either<Object, Object> inner = Either.left(new Object());
        final Either<Object, Either<Object, Object>> outer = Either.right(inner);
        Assert.assertEquals(inner, Either.join(outer));
    }

    @Test
    public void leftsAreEqualWhenContainingTheSameValue() {
        final Either one = Either.left(VALUE);
        final Either other = Either.left(VALUE);
        Assert.assertTrue(one.equals(other));
    }

    @Test
    public void leftsAreEqualWhenContainingBothNull() {
        final Either one = Either.left(null);
        final Either other = Either.left(null);
        Assert.assertTrue(one.equals(other));
    }

    @Test
    public void leftsAreNotEqualWhenContainingDifferentValues() {
        final Either one = Either.left(new Object());
        final Either other = Either.left(new Object());
        Assert.assertFalse(one.equals(other));
    }

    @Test
    public void leftIsNotEqualToRightContainingTheSameValue() {
        Assert.assertFalse(left.equals(right));
    }

    @Test
    public void rightsAreEqualWhenContainingTheSameValue() {
        final Either one = Either.right(VALUE);
        final Either other = Either.right(VALUE);
        Assert.assertTrue(one.equals(other));
    }

    @Test
    public void rightsAreEqualWhenContainingBothNull() {
        final Either one = Either.right(null);
        final Either other = Either.right(null);
        Assert.assertTrue(one.equals(other));
    }

    @Test
    public void rightsAreNotEqualWhenContainingDifferentValues() {
        final Either one = Either.right(new Object());
        final Either other = Either.right(new Object());
        Assert.assertFalse(one.equals(other));
    }

    @Test
    public void rightIsNotEqualToLeftContainingTheSameValue() {
        Assert.assertFalse(right.equals(left));
    }
}

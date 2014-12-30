package data;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.Assert;
import org.junit.Test;

public class MaybeTest {

    private static final Object VALUE = new Object();

    private final Maybe<Object> just = Maybe.just(VALUE);
    private final Maybe<Object> nothing = Maybe.nothing();

    @Test
    public void canCreateJustNull() {
        Maybe.just(null);
    }

    @Test
    public void foldOnJustPassesTheContainedValueToTheMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        just.fold(() -> null, capture::getAndSet);
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void foldOnNothingReturnsTheResultOfTheSupplier() {
        final Object result = new Object();
        Assert.assertEquals(result, nothing.fold(() -> result, value -> null));
    }

    @Test
    public void foldOnJustReturnsTheResultOfTheMapper() {
        final Object result = new Object();
        Assert.assertEquals(result, just.fold(() -> null, value -> result));
    }
}

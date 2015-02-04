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
        just.fold(capture::getAndSet, () -> null);
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void foldOnNothingReturnsTheResultOfTheSupplier() {
        final Object result = new Object();
        Assert.assertEquals(result, nothing.fold(value -> null, () -> result));
    }

    @Test
    public void foldOnJustReturnsTheResultOfTheMapper() {
        final Object result = new Object();
        Assert.assertEquals(result, just.fold(value -> result, () -> null));
    }

    @Test
    public void justHasValue() {
        Assert.assertTrue(just.hasValue());
    }

    @Test
    public void nothingHasNotValue() {
        Assert.assertFalse(nothing.hasValue());
    }

    @Test
    public void orElseOnNothingReturnsTheAlternative() {
        final Object alternative = new Object();
        Assert.assertEquals(alternative, nothing.orElse(alternative));
    }

    @Test
    public void orElseOnJustReturnsTheContainedValue() {
        Assert.assertEquals(VALUE, just.orElse(new Object()));
    }

    @Test
    public void orElseWithSupplierOnNothingReturnsTheSuppliedAlternative() {
        final Object alternative = new Object();
        Assert.assertEquals(alternative, nothing.orElse(() -> alternative));
    }

    @Test
    public void orElseWithSupplierOnJustReturnsTheContainedValue() {
        Assert.assertEquals(VALUE, just.orElse(() -> new Object()));
    }

    @Test
    public void orElseWithSupplierOnJustDoesNotCallTheAlternativeSupplier() {
        just.orElse(() -> {
            throw new IllegalStateException();
        });
    }

    @Test
    public void orMaybeOnNothingReturnsTheAlternative() {
        final Maybe<Object> alternative = Maybe.just(new Object());
        Assert.assertEquals(alternative, nothing.orMaybe(alternative));
    }

    @Test
    public void orMaybeOnJustReturnsTheMaybeItself() {
        final Maybe<Object> alternative = Maybe.just(new Object());
        Assert.assertEquals(just, just.orMaybe(alternative));
    }

    @Test
    public void orMaybeWithSupplierOnNothingReturnsTheSuppliedAlternative() {
        final Maybe<Object> alternative = Maybe.just(new Object());
        Assert.assertEquals(alternative, nothing.orMaybe(() -> alternative));
    }

    @Test
    public void orMaybeWithSupplierOnJustReturnsTheMaybeItself() {
        Assert.assertEquals(just, just.orMaybe(() -> Maybe.just(new Object())));
    }

    @Test
    public void orMaybeWithSupplierOnJustDoesNotCallTheAlternativeSupplier() {
        just.orMaybe(() -> {
            throw new IllegalStateException();
        });
    }

    @Test
    public void filterOnNothingReturnsNothing() {
        Assert.assertEquals(nothing, nothing.filter(value -> true));
    }

    @Test
    public void filterOnJustPassesTheContainedValueToThePredicate() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        just.filter(value -> {
            capture.set(value);
            return true;
        });
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void filterOnJustReturnsJustTheValueIfItPassesThePredicateTest() {
        Assert.assertEquals(Maybe.just(VALUE), just.filter(value -> true));
    }

    @Test
    public void filterOnJustReturnsNothingIfTheValueDoesNotPassThePredicateTest() {
        Assert.assertEquals(nothing, just.filter(value -> false));
    }

    @Test
    public void mapOnNothingReturnsNothing() {
        Assert.assertEquals(nothing, nothing.map(value -> null));
    }

    @Test
    public void mapOnJustPassesTheContainedValueToTheMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        just.map(capture::getAndSet);
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void mapOnJustReturnsJustTheResultOfTheMapper() {
        final Object result = new Object();
        Assert.assertEquals(Maybe.just(result), just.map(value -> result));
    }

    @Test
    public void mapOnJustReturnsJustTheResultOfTheMapperEvenIfItIsNull() {
        Assert.assertEquals(Maybe.just(null), just.map(value -> null));
    }

    @Test
    public void flatMapOnNothingReturnsNothing() {
        Assert.assertEquals(nothing, nothing.flatMap(value -> just));
    }

    @Test
    public void flatMapOnJustPassesTheContainedValueToTheMapper() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        just.flatMap(value -> {
            capture.set(value);
            return null;
        });
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void flatMapOnJustReturnsTheMaybeReturnedByTheMapper() {
        final Maybe<Object> result = Maybe.just(new Object());
        Assert.assertEquals(result, just.flatMap(value -> result));
    }

    @Test
    public void joinOnNothingReturnsNothing() {
        Assert.assertEquals(nothing, Maybe.join(Maybe.nothing()));
    }

    @Test
    public void joinOnJustReturnsTheInnerMaybe() {
        final Maybe<Object> inner = Maybe.just(new Object());
        Assert.assertEquals(inner, Maybe.join(Maybe.just(inner)));
    }

    @Test
    public void composedFunctionPassesItsArgumentToTheFormerFunction() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        Maybe.compose(value -> {
            capture.set(value);
            return nothing;
        }, value -> null).apply(VALUE);
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void composedFunctionDoesNotApplyTheLatterOneWhenTheResultOfTheFormerFunctionIsNothing() {
        Maybe.compose(value -> nothing, value -> {
            throw new IllegalStateException();
        }).apply(VALUE);
    }

    @Test
    public void composedFunctionAppliesToTheLatterOneTheResultContainedByTheMaybeReturnedByTheFormerFunction() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        Maybe.compose(value -> just, value -> {
            capture.set(value);
            return null;
        }).apply(new Object());
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void composedFunctionReturnsNothingWhenTheResultOfTheFormerFunctionIsNothing() {
        Assert.assertEquals(nothing, Maybe.compose(value -> nothing, value -> just).apply(new Object()));
    }

    @Test
    public void composedFunctionReturnsTheResultOfTheLatterOneWhenTheResultOfTheFormerFunctionIsJust() {
        final Maybe<Object> result = Maybe.just(new Object());
        Assert.assertEquals(result, Maybe.compose(value -> just, value -> result).apply(new Object()));
    }

    @Test
    public void liftedFunctionOnNothingDoesNotApplyThePureFunction() {
        Maybe.lift(value -> {
            throw new IllegalStateException();
        }).apply(nothing);
    }

    @Test
    public void liftedFunctionOnJustPassesItsArgumentToThePureFunction() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        Maybe.lift(capture::getAndSet).apply(just);
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void liftedFunctionOnNothingReturnsNothing() {
        Assert.assertEquals(nothing, Maybe.lift(value -> null).apply(nothing));
    }

    @Test
    public void liftedFunctionOnJustReturnsJustTheResult() {
        final Object result = new Object();
        Assert.assertEquals(Maybe.just(result), Maybe.lift(value -> result).apply(just));
    }

    @Test
    public void applicativeOnNothingReturnsAFunctionThatAlwaysReturnsNothing() {
        Assert.assertEquals(nothing, Maybe.applicative(Maybe.nothing()).apply(just));
    }

    @Test
    public void applicativeOnJustReturnsAFunctionThatOnNothingDoesNotApplyThePureFunction() {
        Maybe.applicative(Maybe.just(value -> {
            throw new IllegalStateException();
        })).apply(nothing);
    }

    @Test
    public void applicativeOnJustReturnsAFunctionThatOnJustAppliesTheContainedValueToThePureFunction() {
        final AtomicReference<Object> capture = new AtomicReference<>();
        Maybe.applicative(Maybe.just(capture::getAndSet)).apply(just);
        Assert.assertEquals(VALUE, capture.get());
    }

    @Test
    public void applicativeOnJustReturnsAFunctionThatOnNothingReturnsNothing() {
        Assert.assertEquals(nothing, Maybe.applicative(Maybe.just(value -> null)).apply(nothing));
    }

    @Test
    public void applicativeOnJustReturnsAFunctionThatOnJustReturnsJustThePureFunctionResult() {
        final Object result = new Object();
        Assert.assertEquals(Maybe.just(result), Maybe.applicative(Maybe.just(value -> result)).apply(just));
    }

    @Test
    public void nothingsAreEqual() {
        Assert.assertTrue(Maybe.nothing().equals(Maybe.nothing()));
    }

    @Test
    public void justsAreEqualWhenContainingAnEqualValue() {
        Assert.assertTrue(Maybe.just(VALUE).equals(Maybe.just(VALUE)));
    }

    @Test
    public void justsAreNotEqualWhenContainingDifferentValues() {
        Assert.assertFalse(Maybe.just(new Object()).equals(Maybe.just(new Object())));
    }

    @Test
    public void justsAreEqualWhenContainingBothNull() {
        Assert.assertTrue(Maybe.just(null).equals(Maybe.just(null)));
    }

    @Test
    public void nothingIsNotEqualToJustNull() {
        Assert.assertFalse(Maybe.nothing().equals(Maybe.just(null)));
    }
}

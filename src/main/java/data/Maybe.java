package data;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Maybe<T> {

    private Maybe() {
    }

    public static <T> Maybe<T> just(T value) {
        return new Just<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> Maybe<T> nothing() {
        return NOTHING;
    }

    public abstract <R> R fold(Supplier<? extends R> onNothing, Function<? super T, ? extends R> onJust);

    private static class Just<T> extends Maybe<T> {

        private final T value;

        public Just(T value) {
            this.value = value;
        }

        @Override
        public <R> R fold(Supplier<? extends R> onNothing, Function<? super T, ? extends R> onJust) {
            return onJust.apply(value);
        }
    }

    private static final Maybe NOTHING = new Maybe() {

        @Override
        public Object fold(Supplier onNothing, Function onJust) {
            return onNothing.get();
        }
    };
}

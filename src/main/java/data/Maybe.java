package data;

import java.util.Objects;
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

    public abstract <R> Maybe<R> map(Function<? super T, ? extends R> mapper);

    public abstract <R> Maybe<R> flatMap(Function<? super T, ? extends Maybe<? extends R>> mapper);

    public static <T> Maybe<T> join(Maybe<? extends Maybe<? extends T>> maybe) {
        return maybe.flatMap(Function.<Maybe<? extends T>>identity());
    }

    private static class Just<T> extends Maybe<T> {

        private final T value;

        public Just(T value) {
            this.value = value;
        }

        @Override
        public <R> R fold(Supplier<? extends R> onNothing, Function<? super T, ? extends R> onJust) {
            return onJust.apply(value);
        }

        @Override
        public <R> Maybe<R> map(Function<? super T, ? extends R> mapper) {
            return new Just<>(mapper.apply(value));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> Maybe<R> flatMap(Function<? super T, ? extends Maybe<? extends R>> mapper) {
            return (Maybe<R>) mapper.apply(value);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Just)) {
                return false;
            }
            final Just<?> other = (Just<?>) object;
            return Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Just " + value;
        }
    }

    private static final Maybe NOTHING = new Maybe() {

        @Override
        public Object fold(Supplier onNothing, Function onJust) {
            return onNothing.get();
        }

        @Override
        public Maybe map(Function mapper) {
            return this;
        }

        @Override
        public Maybe flatMap(Function mapper) {
            return this;
        }

        @Override
        public boolean equals(Object object) {
            return object == this;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "Nothing";
        }
    };
}

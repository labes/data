package data;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
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

    public abstract <R> R fold(Function<? super T, ? extends R> onValue, Supplier<? extends R> onNothing);

    public abstract boolean hasValue();

    public abstract T orElse(T alternative);

    public abstract T orElse(Supplier<? extends T> alternative);

    public abstract Maybe<T> orMaybe(Maybe<? extends T> alternative);

    public abstract Maybe<T> orMaybe(Supplier<? extends Maybe<? extends T>> alternative);

    public abstract Maybe<T> filter(Predicate<? super T> filter);

    public abstract <R> Maybe<R> map(Function<? super T, ? extends R> mapper);

    public abstract <R> Maybe<R> flatMap(Function<? super T, ? extends Maybe<? extends R>> mapper);

    public static <T> Maybe<T> join(Maybe<? extends Maybe<? extends T>> maybe) {
        return maybe.flatMap(Function.<Maybe<? extends T>>identity());
    }

    public static <T, S, R> Function<T, Maybe<R>> compose(Function<? super T, ? extends Maybe<? extends S>> former, Function<? super S, ? extends Maybe<? extends R>> latter) {
        return value -> former.apply(value).flatMap(latter);
    }

    public static <T, R> Function<Maybe<T>, Maybe<R>> lift(Function<? super T, ? extends R> function) {
        return maybe -> maybe.map(function);
    }

    public static <T, R> Function<Maybe<T>, Maybe<R>> applicative(Maybe<? extends Function<? super T, ? extends R>> function) {
        return maybe -> function.flatMap(maybe::map);
    }

    @SuppressWarnings("unchecked")
    public static <T> Maybe<T> narrow(Maybe<? extends T> maybe) {
        return (Maybe<T>) maybe;
    }

    private static class Just<T> extends Maybe<T> {

        private final T value;

        public Just(T value) {
            this.value = value;
        }

        @Override
        public <R> R fold(Function<? super T, ? extends R> onValue, Supplier<? extends R> onNothing) {
            return onValue.apply(value);
        }

        @Override
        public boolean hasValue() {
            return true;
        }

        @Override
        public T orElse(T alternative) {
            return value;
        }

        @Override
        public T orElse(Supplier<? extends T> alternative) {
            return value;
        }

        @Override
        public Maybe<T> orMaybe(Maybe<? extends T> alternative) {
            return this;
        }

        @Override
        public Maybe<T> orMaybe(Supplier<? extends Maybe<? extends T>> alternative) {
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Maybe<T> filter(Predicate<? super T> filter) {
            return filter.test(value) ? this : NOTHING;
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
        public Object fold(Function onValue, Supplier onNothing) {
            return onNothing.get();
        }

        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public Object orElse(Object alternative) {
            return alternative;
        }

        @Override
        public Object orElse(Supplier alternative) {
            return alternative.get();
        }

        @Override
        public Maybe orMaybe(Maybe alternative) {
            return alternative;
        }

        @Override
        public Maybe orMaybe(Supplier alternative) {
            return (Maybe) alternative.get();
        }

        @Override
        public Maybe filter(Predicate filter) {
            return this;
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

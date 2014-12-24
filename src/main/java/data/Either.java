package data;

import java.util.function.Function;

public abstract class Either<L, R> {

    private Either() {
    }

    public static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    public abstract <T> T fold(Function<? super L, ? extends T> onLeft, Function<? super R, ? extends T> onRight);

    private static class Left<L, R> extends Either<L, R> {

        private final L left;

        public Left(L left) {
            this.left = left;
        }

        @Override
        public <T> T fold(Function<? super L, ? extends T> onLeft, Function<? super R, ? extends T> onRight) {
            return onLeft.apply(left);
        }
    }

    private static class Right<L, R> extends Either<L, R> {

        private final R right;

        public Right(R right) {
            this.right = right;
        }

        @Override
        public <T> T fold(Function<? super L, ? extends T> onLeft, Function<? super R, ? extends T> onRight) {
            return onRight.apply(right);
        }
    }
}

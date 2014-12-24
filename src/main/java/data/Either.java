package data;

import java.util.Objects;
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

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Left)) {
                return false;
            }
            final Left<?, ?> other = (Left<?, ?>) object;
            return Objects.equals(left, other.left);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(left);
        }

        @Override
        public String toString() {
            return "Left " + left;
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

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Right)) {
                return false;
            }
            final Right<?, ?> other = (Right<?, ?>) object;
            return Objects.equals(right, other.right);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(right);
        }

        @Override
        public String toString() {
            return "Right " + right;
        }
    }
}

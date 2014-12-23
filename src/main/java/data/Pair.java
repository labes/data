package data;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Pair<L, R> {

    private final L left;
    private final R right;

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    public static <L, R> Function<R, Pair<L, R>> ofLeft(L left) {
        return right -> new Pair<>(left, right);
    }

    public static <L, R> Function<L, Pair<L, R>> ofRight(R right) {
        return left -> new Pair<>(left, right);
    }

    public L left() {
        return left;
    }

    public R right() {
        return right;
    }

    public <LT> Pair<LT, R> withLeft(LT left) {
        return new Pair<>(left, right);
    }

    public <RT> Pair<L, RT> withRight(RT right) {
        return new Pair<>(left, right);
    }

    public Pair<R, L> flip() {
        return new Pair<>(right, left);
    }

    public <LT> Pair<LT, R> mapLeft(Function<? super L, ? extends LT> leftMapper) {
        return new Pair<>(leftMapper.apply(left), right);
    }

    public <RT> Pair<L, RT> mapRight(Function<? super R, ? extends RT> rightMapper) {
        return new Pair<>(left, rightMapper.apply(right));
    }

    public <LT, RT> Pair<LT, RT> map(Function<? super L, ? extends LT> leftMapper, Function<? super R, ? extends RT> rightMapper) {
        final Supplier<Pair<LT, RT>> leftProjection = () -> new Pair<>(leftMapper.apply(left), null);
        final Supplier<Pair<LT, RT>> rightProjection = () -> new Pair<>(null, rightMapper.apply(right));
        return Stream.of(leftProjection, rightProjection).parallel().map(Supplier::get).reduce(Pair::merge).get();
    }

    private static <L, R> Pair<L, R> merge(Pair<L, R> former, Pair<L, R> latter) {
        return former.left != null || latter.right != null ? new Pair<>(former.left, latter.right) : new Pair<>(latter.left, former.right);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> Pair<L, R> narrow(Pair<? extends L, ? extends R> pair) {
        return (Pair<L, R>) pair;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Pair)) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) object;
        return Objects.equals(left, other.left) && Objects.equals(right, other.right);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(left) ^ Objects.hashCode(right);
    }

    @Override
    public String toString() {
        return "(" + left + ',' + right + ')';
    }
}

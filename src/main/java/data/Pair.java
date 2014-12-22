package data;

import java.util.function.Function;

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
}

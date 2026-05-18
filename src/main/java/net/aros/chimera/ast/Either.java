package net.aros.chimera.ast;

import java.util.Optional;
import java.util.function.Function;

public sealed interface Either<L, R> permits Either.Left, Either.Right {
    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    <M> M map(Function<L, M> ifLeft, Function<R, M> ifRight);

    Optional<L> left();

    Optional<R> right();

    default boolean isLeft() {
        return !isRight();
    }

    default boolean isRight() {
        return !isLeft();
    }

    record Left<L, R>(L value) implements Either<L, R> {
        @Override
        public Optional<L> left() {
            return Optional.of(value);
        }

        @Override
        public Optional<R> right() {
            return Optional.empty();
        }

        @Override
        public <M> M map(Function<L, M> ifLeft, Function<R, M> ifRight) {
            return ifLeft.apply(value);
        }

        @Override
        public boolean isLeft() {
            return true;
        }
    }

    record Right<L, R>(R value) implements Either<L, R> {
        @Override
        public Optional<L> left() {
            return Optional.empty();
        }

        @Override
        public Optional<R> right() {
            return Optional.of(value);
        }

        @Override
        public <M> M map(Function<L, M> ifLeft, Function<R, M> ifRight) {
            return ifRight.apply(value);
        }

        @Override
        public boolean isRight() {
            return true;
        }
    }
}

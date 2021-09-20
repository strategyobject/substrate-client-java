package com.strategyobject.substrateclient.scale;

import lombok.val;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public interface Result<T, E> {
    /**
     * Create a success result.
     *
     * @param ok  The value.
     * @param <T> The success type.
     * @param <E> Ignored.
     * @return A success result.
     */
    static <T, E> Result<T, E> ok(final T ok) {
        return new Ok<>(ok);
    }

    /**
     * Create an error result.
     *
     * @param err The error value.
     * @param <T> Ignored.
     * @param <E> The error type.
     * @return An error result.
     */
    static <T, E> Result<T, E> err(final E err) {
        return new Err<>(err);
    }

    /**
     * @return true, if ok result, false, if error result.
     */
    boolean isOk();

    /**
     * @return true, if error result, false, if ok result.
     */
    boolean isErr();

    /**
     * Converts a Result into an Optional
     *
     * @return Optional.empty() if error result.
     */
    Optional<T> ok();

    /**
     * Converts a Result into an Optional
     *
     * @return Optional.empty() if ok result.
     */
    Optional<E> err();


    /**
     * Applies a function to the ok value.
     *
     * @param f   A map function.
     * @param <U> The return type of the map function.
     * @return The new result.
     */
    <U> Result<U, E> map(final Function<T, U> f);

    /**
     * Applies a function to the ok value or returns a fallback value.
     *
     * @param f   A map function.
     * @param <U> The return type of the map function.
     * @return The calculated value.
     */
    <U> U mapOr(final U fallback, final Function<T, U> f);

    /**
     * Applies a function to the ok value or a fallback function to an error.
     *
     * @param f   A map function.
     * @param <U> The return type of the map function.
     * @return The calculated value.
     */
    <U> U mapOrElse(final Function<E, U> fallback, final Function<T, U> f);

    /**
     * Applies a function to the error value.
     *
     * @param f   The mapping function.
     * @param <F> The type of the new error.
     * @return The new result.
     */
    <F> Result<T, F> mapErr(final Function<E, F> f);

    /**
     * Returns the given result if ok.
     *
     * @param res The result if ok.
     * @param <U> The type of the new ok value.
     * @return The new result if ok or an error result with the same error value.
     */
    <U> Result<U, E> and(final Result<U, E> res);

    /**
     * Applies a function that returns a new error if ok.
     *
     * @param f   A function applied with the ok value.
     * @param <U> The type of the new ok value.
     * @return The new result or a new result with the same error.
     */
    <U> Result<U, E> andThen(final Function<T, Result<U, E>> f);

    /**
     * @param res A result to be returned if this is an error result.
     * @param <F> the error type if applicable.
     * @return Returns the ok result or the given result.
     */
    <F> Result<T, F> or(final Result<T, F> res);

    /**
     * @param f   a function to be applied on the error value.
     * @param <F> The error type of the return value of the function.
     * @return a new result type if err, else an ok result with the current ok value.
     */
    <F> Result<T, F> orElse(final Function<E, Result<T, F>> f);

    /**
     * @param fallback a default value.
     * @return The ok value or a default value.
     */
    T unwrapOr(final T fallback);

    /**
     * @param fallback a function to calculate a default value in case of an error.
     * @return The ok value or a default value.
     */
    T unwrapOrElse(final Function<E, T> fallback);

    /**
     * Expects an ok value or throws a NoSuchElementException.
     *
     * @return The ok value.
     */
    T unwrap();

    /**
     * Expects an ok value or throws a NoSuchElementException with the given message.
     *
     * @param msg An error message.
     * @return The ok value.
     */
    T expect(final String msg);

    /**
     * Expects an error or throws a NoSuchElementException.
     *
     * @return The error value.
     */
    E unwrapErr();

    /**
     * Expects an error or throws a NoSuchElementException with the given message.
     *
     * @param msg An error message.
     * @return The error value.
     */
    E expectErr(final String msg);

    final class Ok<T, E> implements Result<T, E> {
        private final T value;

        public Ok(final T value) {
            this.value = value;
        }

        @Override
        public boolean isOk() {
            return true;
        }

        @Override
        public boolean isErr() {
            return false;
        }

        @Override
        public Optional<T> ok() {
            return Optional.of(value);
        }

        @Override
        public Optional<E> err() {
            return Optional.empty();
        }

        @Override
        public <U> Result<U, E> map(final Function<T, U> f) {
            return Result.ok(f.apply(value));
        }

        @Override
        public <U> U mapOr(U fallback, Function<T, U> f) {
            return f.apply(value);
        }

        @Override
        public <U> U mapOrElse(final Function<E, U> fallback, final Function<T, U> f) {
            return f.apply(value);
        }

        @Override
        public <F> Result<T, F> mapErr(final Function<E, F> f) {
            return Result.ok(value);
        }

        @Override
        public <U> Result<U, E> and(final Result<U, E> res) {
            return res;
        }

        @Override
        public <U> Result<U, E> andThen(final Function<T, Result<U, E>> f) {
            return f.apply(value);
        }

        @Override
        public <F> Result<T, F> or(final Result<T, F> res) {
            return Result.ok(value);
        }

        @Override
        public <F> Result<T, F> orElse(final Function<E, Result<T, F>> f) {
            return Result.ok(value);
        }

        @Override
        public T unwrapOr(final T fallback) {
            return value;
        }

        @Override
        public T unwrapOrElse(final Function<E, T> fallback) {
            return value;
        }

        @Override
        public T unwrap() {
            return value;
        }

        @Override
        public T expect(final String msg) {
            return value;
        }

        @Override
        public E unwrapErr() {
            throw new NoSuchElementException();
        }

        @Override
        public E expectErr(final String msg) {
            throw new NoSuchElementException(msg);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            val ok = (Ok<?, ?>) o;
            return value.equals(ok.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return String.format("Ok(%s)", value);
        }
    }

    final class Err<T, E> implements Result<T, E> {
        private final E value;

        public Err(final E value) {
            this.value = value;
        }

        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public boolean isErr() {
            return true;
        }

        @Override
        public Optional<T> ok() {
            return Optional.empty();
        }

        @Override
        public Optional<E> err() {
            return Optional.of(value);
        }

        @Override
        public <U> Result<U, E> map(final Function<T, U> f) {
            return Result.err(value);
        }

        @Override
        public <U> U mapOr(U fallback, Function<T, U> f) {
            return fallback;
        }

        @Override
        public <U> U mapOrElse(final Function<E, U> fallback, final Function<T, U> f) {
            return fallback.apply(value);
        }

        @Override
        public <F> Result<T, F> mapErr(final Function<E, F> f) {
            return Result.err(f.apply(value));
        }

        @Override
        public <U> Result<U, E> and(final Result<U, E> res) {
            return Result.err(value);
        }

        @Override
        public <U> Result<U, E> andThen(final Function<T, Result<U, E>> f) {
            return Result.err(value);
        }

        @Override
        public <F> Result<T, F> or(final Result<T, F> res) {
            return res;
        }

        @Override
        public <F> Result<T, F> orElse(final Function<E, Result<T, F>> f) {
            return f.apply(value);
        }

        @Override
        public T unwrapOr(final T fallback) {
            return fallback;
        }

        @Override
        public T unwrapOrElse(final Function<E, T> fallback) {
            return fallback.apply(value);
        }

        @Override
        public T unwrap() {
            throw new NoSuchElementException();
        }

        @Override
        public T expect(final String msg) {
            throw new NoSuchElementException(msg);
        }

        @Override
        public E unwrapErr() {
            return value;
        }

        @Override
        public E expectErr(final String msg) {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Err<?, ?> err = (Err<?, ?>) o;
            return value.equals(err.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return String.format("Err(%s)", value);
        }
    }
}
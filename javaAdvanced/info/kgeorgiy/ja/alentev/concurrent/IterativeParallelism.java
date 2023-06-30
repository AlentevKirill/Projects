package javaAdvanced.info.kgeorgiy.ja.alentev.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IterativeParallelism implements ListIP {

    private final ParallelMapper parallelMapper;

    public IterativeParallelism() {
        this(null);
    }

    public IterativeParallelism(final ParallelMapper parallelMapper) {
        this.parallelMapper = parallelMapper;
    }

    @Override
    public String join(final int threads, final List<?> values) throws InterruptedException {
        return evaluateParallel(threads,
                values,
                stream -> stream.map(Objects::toString).collect(Collectors.joining()),
                stream -> stream.collect(Collectors.joining()));
    }

    @Override
    public <T> List<T> filter(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate)
            throws InterruptedException {
        return evaluateParallel(threads, values, stream -> stream.filter(predicate));
    }

    @Override
    public <T, U> List<U> map(
            final int threads,
            final List<? extends T> values,
            final Function<? super T, ? extends U> function
    ) throws InterruptedException {
        return evaluateParallel(threads, values, stream -> stream.map(function));
    }

    @Override
    public <T> T maximum(
            final int threads,
            final List<? extends T> values,
            final Comparator<? super T> comparator)
            throws InterruptedException {
        return minimum(threads, values, comparator.reversed());
    }

    @Override
    public <T> T minimum(
            final int threads,
            final List<? extends T> values,
            final Comparator<? super T> comparator)
            throws InterruptedException {
        final Function<Stream<T>, T> func = stream -> stream.min(comparator).orElseThrow();
        return evaluateParallel(threads, values, func, func);
    }

    @Override
    public <T> boolean all(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate)
            throws InterruptedException {
        return evaluateParallel(
                threads,
                values,
                stream -> stream.allMatch(predicate),
                stream -> stream.allMatch(Boolean::booleanValue)
        );
    }

    @Override
    public <T> boolean any(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate)
            throws InterruptedException {
        return !all(threads, values, predicate.negate());
    }

    @Override
    public <T> int count(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate)
            throws InterruptedException {
        return evaluateParallel(
                threads,
                values,
                stream -> (int) stream.filter(predicate).count(),
                stream -> stream.reduce(0, Integer::sum));
    }

    private static <T> List<Stream<T>> splittingList(
            final int threads,
            final List<? extends T> list
    ) {
        final List<Stream<T>> result = new ArrayList<>(threads);
        final int count = list.size() / threads;
        int remains = list.size() % threads;
        for (int left = 0; left < list.size();) {
            final int right = remains > 0 ? left + count + 1 : left + count;
            remains--;
            result.add(list.subList(left, right).stream().map(a -> a));
            left = right;
        }
        return result;
    }

    private <T, R> R evaluateParallel(
            int threads, final List<? extends T> list,
            final Function<Stream<T>, R> function,
            final Function<Stream<R>, R> union
    ) throws InterruptedException {
        if (list == null) {
            throw new NullPointerException("Input list and lists element must not be null");
        }
        if (threads < 1) {
            throw new IllegalArgumentException("The number of threads must be greater than 0");
        }

        threads = Math.max(1, Math.min(threads, list.size()));
        final List<Stream<T>> subLists = splittingList(threads, list);

        final List<R> resultsInSublist = parallelMapper == null
                                         ? map(function, subLists)
                                         : parallelMapper.map(function, subLists);
        return union.apply(resultsInSublist.stream());

    }

    private static <T, R> List<R> map(
            final Function<Stream<T>, R> function,
            final List<Stream<T>> subLists
    ) throws InterruptedException {
        final List<R> results = new ArrayList<>(Collections.nCopies(subLists.size(), null));
        final List<Thread> threads = IntStream
                .range(0, subLists.size())
                .<Runnable>mapToObj(i -> () -> results.set(i, function.apply(subLists.get(i))))
                .map(Thread::new)
                .peek(Thread::start)
                .collect(Collectors.toList());

        InterruptedException exception = null;
            for (final Thread thread : threads) {
                boolean flag = false;
                do {
                    try {
                        thread.join();
                        flag = true;
                    } catch (final InterruptedException e) {
                        // :NOTE: not joined
                        if (exception == null) {
                            exception = e;
                            threads.forEach(Thread::interrupt);
                        } else {
                            exception.addSuppressed(e);
                        }
                    }
                } while (!flag);
            }
        if (exception != null) {
            throw exception;
        }
        return results;
    }

    private <T, U> List<U> evaluateParallel(
            final int threads,
            final List<? extends T> list,
            final Function<Stream<? extends T>, Stream<? extends U>> function)
            throws InterruptedException {
        return evaluateParallel(
                threads,
                list,
                stream -> function.apply(stream).collect(Collectors.toList()),
                stream -> stream.flatMap(List::stream).collect(Collectors.toList()));
    }
}

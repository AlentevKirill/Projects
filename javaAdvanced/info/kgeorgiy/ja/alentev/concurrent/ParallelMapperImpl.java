package javaAdvanced.info.kgeorgiy.ja.alentev.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParallelMapperImpl implements ParallelMapper {
    private final Queue<Runnable> queueOfTasks = new ArrayDeque<>();
    private final List<Thread> listOfThreads;
    private boolean isClosed;

    public ParallelMapperImpl(int threads) {
        if (threads < 1) {
            throw new IllegalArgumentException("The number of threads must be greater than 0");
        }

        final Runnable worker = () -> {
            try {
                while (!Thread.interrupted()) {
                    getTask().run();
                }
            } catch (final InterruptedException ignored) {
            }
        };
        listOfThreads = Stream.generate(() -> new Thread(worker))
                .limit(threads)
                .peek(Thread::start)
                .collect(Collectors.toList());
    }

    @Override
    public <T, R> List<R> map(final Function<? super T, ? extends R> f, final List<? extends T> args) throws InterruptedException {
        if (isClosed) {
            throw new IllegalStateException("Mapper is already closed");
        }
        if (f == null) {
            throw new NullPointerException("Input function must not be null");
        }
        // :NOTE: args.stream().anyMatch(Objects::isNull)
        if (args == null) {
            throw new NullPointerException("Input list of args and lists args must not be null");
        }

        final SynchronizedList<R> result = new SynchronizedList<>(args.size());
        IntStream.range(0, args.size()).forEach(i -> {
            // :NOTE: addAll
            synchronized (queueOfTasks) {
                // :NOTE: ArithmeticException
                queueOfTasks.offer(() -> {
                    try {
                        result.set(i, f.apply(args.get(i)));
                    } catch (final RuntimeException exception) {
                        // :NOTE: no propagation
                        System.err.println(exception.getMessage() + " " + exception);
                        close();
                        throw exception;
                    }
                });
                queueOfTasks.notify();
            }
        });
        /*for (int i = 0; i < args.size(); i++) {
            final int finalIndex = i; // :NOTE: IntStream
            synchronized (queueOfTasks) {
                // :NOTE: ArithmeticException
                queueOfTasks.offer(() -> result.set(finalIndex, f.apply(args.get(finalIndex))));
                queueOfTasks.notifyAll(); // :NOTE: all??
            }
        }*/
        if (isClosed) {
            throw new InterruptedException("Thread is already interrupt");
        } else {
            return result.getResultList(); // :NOTE: hang on close
        }
    }

    private static class SynchronizedList<T> {

        private final List<T> list;
        private int countActiveAction;

        public SynchronizedList(final int countActiveAction) {
            this.countActiveAction = countActiveAction;
            list = new ArrayList<>(Collections.nCopies(countActiveAction, null));
        }

        private synchronized List<T> getResultList() throws InterruptedException {
            while (countActiveAction != 0) {
                wait();
            }
            return list;
        }

        private synchronized void set(int index, T element) {
            list.set(index, element);
            if (--countActiveAction == 0) {
                notify();
            }
        }
    }

    @Override
    public void close() {
        if (isClosed) {
            return;
        }
        isClosed = true;
        listOfThreads.forEach(Thread::interrupt);
        listOfThreads.forEach(thread -> {
                    boolean flag = false;
                    do {
                        try {
                            thread.join();
                            flag = true;
                        } catch (InterruptedException ignored) {
                            // :NOTE: not joined
                        }
                    } while (!flag);
                }
        );
    }

    private Runnable getTask() throws InterruptedException {
        synchronized (queueOfTasks) {
            while (queueOfTasks.isEmpty()) {
                queueOfTasks.wait();
            }
            return queueOfTasks.poll();
        }
    }
}

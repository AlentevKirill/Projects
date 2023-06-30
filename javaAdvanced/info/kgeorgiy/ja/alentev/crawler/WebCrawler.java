package javaAdvanced.info.kgeorgiy.ja.alentev.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class WebCrawler implements Crawler {
    private static final int DEPTH_DEFAULT_VALUE = 1;
    private static final int DOWNLOADS_DEFAULT_VALUE = 2;
    private static final int EXTRACTORS_DEFAULT_VALUE = 2;
    private static final int PER_HOST_DEFAULT_VALUE = 1;

    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;
    private final int perHost;
    private final Map<String, LocalCrawler> worker;

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.downloaders = Executors.newFixedThreadPool(downloaders);
        this.extractors = Executors.newFixedThreadPool(extractors);
        this.perHost = perHost;
        this.worker = new ConcurrentHashMap<>();
    }

    @Override
    public Result download(String url, int depth) {
        return new TaskWorker(url, depth).bypass();
    }

    private class TaskWorker {
        private final Phaser phaser;
        private final Map<String, IOException> errorCollector;
        private Queue<String> currentLevel;
        private Queue<String> nextLevel;
        private final Set<String> visitedWebsites;
        private final Set<String> downloadedWebsites;
        private final int depth;

        public TaskWorker(String url, int depth) {
            this.phaser = new Phaser(1);
            this.errorCollector = new ConcurrentHashMap<>();
            this.currentLevel = new ConcurrentLinkedQueue<>();
            this.nextLevel = new ConcurrentLinkedQueue<>();
            this.visitedWebsites = ConcurrentHashMap.newKeySet();
            this.downloadedWebsites = ConcurrentHashMap.newKeySet();
            this.depth = depth;
            this.currentLevel.add(url);
        }

        private Result bypass() {
            visitedWebsites.addAll(currentLevel);
            IntStream.range(0, depth).forEach(i -> {
                currentLevel.forEach(string -> workInUrl(string, i));
                phaser.arriveAndAwaitAdvance();
                Queue<String> t = currentLevel;
                currentLevel = nextLevel;
                nextLevel = t;
                nextLevel.clear();
            });
            return new Result(List.copyOf(downloadedWebsites), errorCollector);
        }

        private void workInUrl(String url, int level) {
            LocalCrawler localCrawler;
            try {
                localCrawler = worker.computeIfAbsent(URLUtils.getHost(url), key -> new LocalCrawler());
            } catch (MalformedURLException exception) {
                errorCollector.put(url, exception);
                return;
            }
            phaser.register();
            localCrawler.addTask(() -> downloadDocument(url, level, localCrawler));
        }

        private void downloadDocument(String url, int level, LocalCrawler localCrawler) {
            try {
                Document document = downloader.download(url);
                downloadedWebsites.add(url);
                if (level < depth - 1) {
                    extractLinks(document);
                }
            } catch (IOException exception) {
                errorCollector.put(url, exception);
            } finally {
                localCrawler.doTask();
                phaser.arriveAndDeregister();
            }
        }

        private void extractLinks(Document document) {
            phaser.register();
            extractors.submit(() -> {
                try {
                    document.extractLinks()
                            .stream()
                            .filter(visitedWebsites::add)
                            .forEach(nextLevel::add);
                } catch (IOException ignored) {
                } finally {
                    phaser.arriveAndDeregister();
                }
            });
        }
    }


    private class LocalCrawler {
        private final Queue<Runnable> waitTask;
        private int countDownload;
        private final Lock lock;

        private LocalCrawler() {
            this.waitTask = new ArrayDeque<>();
            this.countDownload = 0;
            this.lock = new ReentrantLock();
        }

        public void addTask(Runnable task) {
            lock.lock();
            try {
                if (countDownload < perHost) {
                    countDownload++;
                    // :NOTE: RejectedExecutionException
                    downloaders.submit(task);
                } else {
                    waitTask.add(task);
                }
            } finally {
                lock.unlock();
            }
        }

        public void doTask() {
            lock.lock();
            try {
                if (!waitTask.isEmpty()) {
                    // :NOTE: RejectedExecutionException
                    downloaders.submit(waitTask.poll());
                } else {
                    countDownload--;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void close() {
        downloaders.shutdown();
        extractors.shutdown();
        checkClose(downloaders);
        checkClose(extractors);
    }

    private void checkClose(ExecutorService executorService) {
        try {
            if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
                System.err.println("The waiting time for the thread to close has exceeded the limits");
            }
        } catch (InterruptedException exception) {
            System.err.println(exception.getMessage() + " " + exception);
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length == 0 || args.length > 4 || args[0] == null) {
            System.err.println("The input line should look like this:" +
                               " WebCrawler url [depth [downloads [extractors [perHost]]]]." +
                               " And entered arguments must not be null");
            return;
        }

        try {
            String url = args[0];
            int depth = checkArgument(args, 1, DEPTH_DEFAULT_VALUE);
            int downloads = checkArgument(args, 2, DOWNLOADS_DEFAULT_VALUE);
            int extractors = checkArgument(args, 3, EXTRACTORS_DEFAULT_VALUE);
            int perHost = checkArgument(args, 4, PER_HOST_DEFAULT_VALUE);

            try (WebCrawler webCrawler = new WebCrawler(
                    new CachingDownloader(1),
                    downloads,
                    extractors,
                    perHost)
            ) {
                webCrawler.download(url, depth);
            } catch (IOException exception) {
                System.err.println("Exception with init downloader: " + exception.getMessage());
            }
        } catch (NumberFormatException exception) {
            System.err.println("The arguments are expected to be valid integer numbers");
        }
    }

    private static int checkArgument(String[] args, int i, int defaultValue) {
        if (args.length > i) {
            return Integer.parseInt(args[i]);
        } else {
            return defaultValue;
        }
    }
}

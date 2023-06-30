package javaAdvanced.info.kgeorgiy.ja.alentev.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javaAdvanced.info.kgeorgiy.ja.alentev.hello.Util.*;

public class HelloUDPNonblockingServer implements HelloServer {

    private static final long TIMEOUT = 800;
    private DatagramChannel datagramChannel;
    private Selector selector;
    private ExecutorService singleExecutor;
    private int bufferSize;
    private ExecutorService sender;
    private Queue<Package> packages;


    @Override
    public void start(int port, int threads) {
        if (sender != null) {
            return;
        }
        singleExecutor = Executors.newSingleThreadExecutor();
        sender = Executors.newFixedThreadPool(threads);
        packages = new ConcurrentLinkedQueue<>();
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.bind(new InetSocketAddress(port));
            datagramChannel.configureBlocking(false);
            selector = Selector.open();
            datagramChannel.register(selector, SelectionKey.OP_READ);
            bufferSize = datagramChannel.socket().getReceiveBufferSize();
        } catch (IOException ioException) {
            System.err.println("Error with register channel" + ioException.getMessage());
        }
        singleExecutor.submit(() -> {
            try {
                while (!Thread.interrupted() && datagramChannel.isOpen()) {
                    selector.select(this::keyWorker, TIMEOUT);
                }
            } catch (IOException ioException) {
                System.err.println("Error when selecting the key " + ioException.getMessage());
            }
        });
    }

    private void keyWorker(SelectionKey selectionKey) {
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        try {
            if (selectionKey.isReadable()) {
                SocketAddress address = datagramChannel.receive(buffer);
                String responseString = getResponseString(getStringOfBuffer(buffer));
                sender.submit(() -> {
                    packages.offer(new Package(responseString, address));
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                    selector.wakeup();
                });
            }
        } catch (IOException ioException) {
            System.err.println("Error reading selection key " + ioException.getMessage());
        }
        try {
            if (selectionKey.isWritable()) {
                Package responsePackage = packages.poll();
                if (responsePackage == null) {
                    throw new IOException("Cannot send a null response");
                }
                buffer.put(responsePackage.responseString.getBytes(StandardCharsets.UTF_8));
                datagramChannel.send(buffer.flip(), responsePackage.socketAddress);
                if (packages.isEmpty()) {
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        } catch (IOException ioException) {
            System.err.println("Error with sending in datagram channel " + ioException.getMessage());
        }
    }


    private static class Package {
        String responseString;
        SocketAddress socketAddress;

        Package(String responseString, SocketAddress socketAddress) {
            this.responseString = responseString;
            this.socketAddress = socketAddress;
        }
    }


    @Override
    public void close() {
        try {
            if (selector != null) {
                selector.close();
            }
        } catch (IOException ioException) {
            System.err.println("Error with closing selector " + ioException.getMessage());
        }

        channelClose(datagramChannel);
        closeExecutorService(singleExecutor);
        closeExecutorService(sender);
    }

    public static void main(String[] args) {
        try (HelloServer helloUDPServer = new HelloUDPNonblockingServer()){
            serverMain(args, helloUDPServer);
        }
    }
}

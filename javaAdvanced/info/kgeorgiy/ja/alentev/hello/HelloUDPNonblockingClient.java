package javaAdvanced.info.kgeorgiy.ja.alentev.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

import static javaAdvanced.info.kgeorgiy.ja.alentev.hello.Util.*;

public class HelloUDPNonblockingClient implements HelloClient {

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        try (Selector selector = Selector.open()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
            IntStream.range(1, threads + 1).forEach(thread -> {
                try {
                    DatagramChannel datagramChannel = DatagramChannel.open();
                    datagramChannel.configureBlocking(false);
                    datagramChannel.connect(inetSocketAddress);
                    datagramChannel.register(
                            selector,
                            SelectionKey.OP_WRITE,
                            new ChannelBuffer(thread, requests, datagramChannel.socket().getReceiveBufferSize())
                    );
                } catch (IOException ioException) {
                    System.err.println("Error with register channel" + ioException.getMessage());
                }
            });
            while (!Thread.interrupted() && !selector.keys().isEmpty()) {
                int uniq = selector.select(selectionKey ->
                        keyWorker(requests, inetSocketAddress, prefix, selectionKey), TIMEOUT_CLIENT);
                if (uniq == 0) {
                    selector.keys().forEach(selectionKey -> selectionKey.interestOps(SelectionKey.OP_WRITE));
                }
            }
        } catch (UnknownHostException unknownHostException) {
            System.err.println("No IP address for the host could be found: " + unknownHostException.getMessage());
        } catch (IOException ioException) {
            System.err.println("Error with register channel " + ioException.getMessage());
        }
    }

    private void keyWorker(
            int requests,
            InetSocketAddress inetSocketAddress,
            String prefix,
            SelectionKey selectionKey
    ) {
        DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        ChannelBuffer channelBuffer = (ChannelBuffer) selectionKey.attachment();
        ByteBuffer buffer = channelBuffer.buffer.clear();
        int request = requests - channelBuffer.requests + 1;
        String message = getRequestString(prefix, channelBuffer.thread, request);

        if (selectionKey.isReadable()) {
            try {
                datagramChannel.receive(buffer);
                String responseString = getStringOfBuffer(buffer);
                if (responseString.contains(prefix)) {
                    if (checkResponseString(responseString, prefix, channelBuffer.thread, request)) {
                        logResponse(message, responseString);
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                        if (--channelBuffer.requests == 0) {
                            channelClose(datagramChannel);
                            selectionKey.cancel();
                        }
                    }
                }
            } catch (IOException ioException) {
                System.err.println("Error reading selection key " + ioException.getMessage());
            }
            return;
        }

        if (selectionKey.isWritable()) {
            buffer.put(message.getBytes(StandardCharsets.UTF_8));
            try {
                datagramChannel.send(buffer.flip(), inetSocketAddress);
            } catch (IOException ioException) {
                System.err.println("Error with sending in datagram channel " + ioException.getMessage());
            }
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    private static class ChannelBuffer {

        private final int thread;
        private int requests;
        private final ByteBuffer buffer;

        public ChannelBuffer(int thread, int requests, int bufferSize) {
            this.thread = thread;
            this.requests = requests;
            this.buffer = ByteBuffer.allocate(bufferSize);
        }
    }

    public static void main(String[] args) {
        clientMain(args, new HelloUDPNonblockingClient());
    }
}

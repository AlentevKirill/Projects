package javaAdvanced.info.kgeorgiy.ja.alentev.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static javaAdvanced.info.kgeorgiy.ja.alentev.hello.Util.*;

public class HelloUDPClient implements HelloClient {



    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        ExecutorService executorService;
        try {
            executorService = Executors.newFixedThreadPool(threads);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
            IntStream.range(1, threads + 1)
                    .forEach(thread -> executorService
                            .submit(new Thread(() -> uDPWorker(
                                    requests,
                                    prefix,
                                    thread,
                                    inetSocketAddress
                            )))
                    );
            executorService.shutdown();
        } catch (UnknownHostException hostException) {
            System.err.println("No IP address for the host could be found: " + hostException.getMessage());
            return;
        }
        try {
            if (!executorService.awaitTermination((long) threads * requests * TIMEOUT_CLIENT, TimeUnit.SECONDS)) {
                System.err.println("Problem with close");
            }
        } catch (InterruptedException interruptedException) {
            System.err.println("Thread Interruption Error: " + interruptedException.getMessage());
        }
    }

    private void uDPWorker(int requests, String prefix, int thread, InetSocketAddress inetSocketAddress) {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.setSoTimeout(TIMEOUT_CLIENT);
            IntStream.range(1, requests + 1).forEach(request -> {
                String message = getRequestString(prefix, thread, request);
                byte[] byteMessage = message.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(
                        byteMessage,
                        0,
                        byteMessage.length,
                        inetSocketAddress
                );
                boolean flag = false;
                while (!Thread.interrupted() && !flag) {
                    try {
                        datagramSocket.send(packet);
                        int receiverLength = datagramSocket.getReceiveBufferSize();
                        DatagramPacket receiverBuff = new DatagramPacket(
                                new byte[receiverLength],
                                receiverLength
                        );
                        datagramSocket.receive(receiverBuff);
                        String responseString = new String(
                                receiverBuff.getData(),
                                0,
                                receiverBuff.getLength(),
                                StandardCharsets.UTF_8
                        );
                        if (responseString.contains(prefix)) {
                            flag = checkResponseString(responseString, prefix, thread, request);
                            if (flag) {
                                logResponse(message, responseString);
                            }
                        }
                    } catch (SocketTimeoutException socketTimeoutException) {
                        System.err.println("Error with Timeout: " + socketTimeoutException.getMessage());
                    } catch (PortUnreachableException portUnreachableException) {
                        System.err.println("Socket connection destination is currently unavailable: "
                                + portUnreachableException.getMessage());
                    } catch (IOException ioException) {
                        System.err.println("Error with sending packet: " + ioException.getMessage());
                    }
                }
            });
        } catch (SocketException socketException) {
            System.err.println("Problems with open socket: " + socketException.getMessage());
        }
    }



    public static void main(String[] args) {
        clientMain(args, new HelloUDPClient());
    }
}
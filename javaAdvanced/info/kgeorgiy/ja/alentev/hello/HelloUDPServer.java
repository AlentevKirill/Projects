package javaAdvanced.info.kgeorgiy.ja.alentev.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static javaAdvanced.info.kgeorgiy.ja.alentev.hello.Util.*;

public class HelloUDPServer implements HelloServer {

    private DatagramSocket datagramSocket;
    private ExecutorService executorService;

    @Override
    public void start(int port, int threads) {
        executorService = Executors.newFixedThreadPool(threads);
        try {
            datagramSocket = new DatagramSocket(port);
            Stream.generate(() -> (Runnable) this::uDPWorker)
                    .limit(threads)
                    .forEach(executorService::submit);
        } catch (SocketException socketException) {
            System.err.println("Error with open socket: " + socketException.getMessage());
        }
    }

    private void uDPWorker() {
        try {
            while (!Thread.interrupted()) {
                int receiverLength = datagramSocket.getReceiveBufferSize();
                DatagramPacket datagramPacket = new DatagramPacket(new byte[receiverLength], receiverLength);
                datagramSocket.receive(datagramPacket);
                String responseString = new String(
                        datagramPacket.getData(),
                        datagramPacket.getOffset(),
                        datagramPacket.getLength(),
                        StandardCharsets.UTF_8
                );
                datagramPacket.setData(getResponseString(responseString).getBytes(StandardCharsets.UTF_8));
                try {
                    datagramSocket.send(datagramPacket);
                } catch (IOException ioException) {
                    System.err.println("Error with send datagramPacket: " + ioException.getMessage());
                }
            }
        } catch (IOException ioException) {
            System.err.println("Error with receive datagramPacket: " + ioException.getMessage());
        }
    }

    @Override
    public void close() {
        datagramSocket.close();
        closeExecutorService(executorService);
    }

    public static void main(String[] args) {
        try (HelloServer helloUDPServer = new HelloUDPServer()){
            serverMain(args, helloUDPServer);
        }
    }
}

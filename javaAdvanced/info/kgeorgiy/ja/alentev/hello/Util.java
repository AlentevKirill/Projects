package javaAdvanced.info.kgeorgiy.ja.alentev.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;
import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Util {

    public static final int TIMEOUT_CLIENT = 400;

    public static void closeExecutorService(ExecutorService executorService) {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
                System.err.println("The closing time has exceeded the timeout");
            }
        } catch (InterruptedException interruptedException) {
            System.err.println("Thread Interruption Error: " + interruptedException.getMessage());
        }
    }

    public static void serverMain(String[] args, HelloServer helloUDPServer) {
        if (args == null || args.length != 2 || Arrays.stream(args).allMatch(Objects::isNull)) {
            System.err.println("There should be 2 non-null input arguments");
            return;
        }

        try {
            helloUDPServer.start(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1])
            );
        } catch (NumberFormatException exception) {
            System.err.println(
                    "Input arguments should have the following form:" + "\n" +
                            "1. the number of the port on which requests will be received;" + "\n" +
                            "2. the number of worker threads that will process requests."
            );
        }
    }

    public static void clientMain(String[] args, HelloClient helloUDPClient) {
        if (args == null || args.length != 5 || Arrays.stream(args).allMatch(Objects::isNull)) {
            System.err.println("There should be 5 non-null input arguments");
            return;
        }

        try {
            helloUDPClient.run(
                    args[0],
                    Integer.parseInt(args[1]),
                    args[2],
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4])
            );
        } catch (NumberFormatException exception) {
            System.err.println(
                    "Input arguments should have the following form:" + "\n" +
                            "1. the name or ip address of the computer on which the server is running;" + "\n" +
                            "2. the port number to send requests to;" + "\n" +
                            "3. request prefix (string);" + "\n" +
                            "4. number of parallel request streams;" + "\n" +
                            "5. the number of requests in each thread."
            );
        }
    }

    public static String getResponseString(String request) {
        return "Hello, " + request;
    }

    public static String getRequestString(String prefix, int thread, int request) {
        return prefix + thread + "_" + request;
    }

    public static String getStringOfBuffer(ByteBuffer buffer) {
        return new String(
                buffer.flip().array(),
                0,
                buffer.limit(),
                StandardCharsets.UTF_8
        );
    }

    public static boolean checkResponseString(String responseString, String prefix, int thread, int request) {
        String suffix = responseString
                .substring(responseString
                        .lastIndexOf(prefix) + prefix.length());
        return checkResponse(suffix, thread, request);
    }

    private static boolean checkResponse(
            String suffix,
            int thread,
            int request
    ) {
        try {
            String[] nums = suffix.split("_");
            NumberFormat numberFormat = NumberFormat.getInstance();
            int num1 = numberFormat.parse(nums[0]).intValue();
            int num2 = numberFormat.parse(nums[1]).intValue();
            if (num1 == thread && num2 == request) {
                return true;
            }
        } catch (ParseException parseException) {
            System.err.println("Error with parse response" + parseException.getMessage());
        }
        return false;
    }

    public static void logResponse(String message, String responseString) {
        System.out.println("Request: " + message);
        System.out.println("Response: " + responseString + "\n");
    }

    public static void channelClose(DatagramChannel datagramChannel) {
        try {
            if (datagramChannel != null) {
                datagramChannel.close();
            }
        } catch (IOException ioException) {
            System.err.println("Error with closing datagram channel " + ioException.getMessage());
        }
    }
}

package javaAdvanced.info.kgeorgiy.ja.alentev.walk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import static java.nio.file.FileVisitResult.*;


public class RecursiveWalk {

    private static int hashLength = 0;
    private static String zeroHash = ""/*"0000000000000000000000000000000000000000000000000000000000000000"*/;

    private static String linuxToWin(String fileName) {
        /*String[] name = fileName.split("/");
        return String.join(File.separator, name);*/
        return fileName.replace("/", File.separator);
    }

    private static void setHash(String hash, BufferedWriter writer, String file) throws IOException {
        writer.write(hash);
        writer.write(" ");
        writer.write(file);
        writer.newLine();
        writer.flush();
    }

    public static class WalkFiles extends SimpleFileVisitor<Path> {

        private final BufferedWriter writer;
        private final MessageDigest digest;

        public WalkFiles(BufferedWriter writer, MessageDigest digest) {
            this.writer = writer;
            this.digest = digest;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            try {
                byte[] bytes = Files.readAllBytes(file);
                BigInteger bigInteger = new BigInteger(1, digest.digest(bytes));
                String hash = bigInteger.toString(16);
                hash = zeroHash.substring(0, hashLength - hash.length()) + hash;
                setHash(hash, writer, file.toString());
            } catch (IOException e1) {
                System.err.println(e1 + "; Error reading file: " + file);
                try {
                    setHash(zeroHash, writer, file.toString());
                } catch (IOException e) {
                    System.err.println(e + "; Error writing file: " + file);
                }
            }

            return CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes atr) {
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.err.println(exc.toString() + "; Error reading file: " + file.toString());
            try {
                setHash(zeroHash, writer, file.toString());
            } catch (IOException e) {
                System.err.println(e + "; Error writing file: " + file);
            }
            return CONTINUE;
        }
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        boolean problemWithArgs = false;
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("Problem with arguments");
            return;
        }
        /*try {
            problemWithArgs = args.length != 2 || args[0] == null || args[1] == null;
        } catch (NullPointerException e) {
            problemWithArgs = true;
            System.err.println(e + " Problem with arguments");
        }*/
        if (!problemWithArgs) {
            try {
                Path fileIn = Paths.get(args[0]);
                try {
                    Path fileOut = Paths.get(args[1]);
                    if (!Files.isWritable(fileOut)) {
                        Path dir = fileOut.getParent();
                        if (dir == null) {
                            throw new InvalidPathException(args[1], "There is no file with this path");
                        }
                        Files.createDirectories(dir);
                    }
                    try {
                        List<String> text = Files.readAllLines(fileIn, StandardCharsets.UTF_8);
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        hashLength = new BigInteger(1, digest.digest(new byte[]{0})).toString(16).length();
                        zeroHash = "0".repeat(hashLength);
                        try (BufferedWriter writer = Files.newBufferedWriter(fileOut, StandardCharsets.UTF_8)) {
                            WalkFiles walkFiles = new WalkFiles(writer, digest);
                            for (String fileName : text) {
                                String normalFileName = linuxToWin(fileName);
                                try {
                                    Path file = Paths.get(normalFileName);
                                    Files.walkFileTree(file, walkFiles);
                                } catch (IOException e) {
                                    System.err.println(e + " Error when trying to crawl the tree with the root: " + normalFileName);
                                    setHash(zeroHash, writer, fileName);
                                } catch (InvalidPathException e) {
                                    System.err.println(e + " Invalid path " + normalFileName);
                                    setHash(zeroHash, writer, fileName);
                                }
                            }
                        } catch (IOException e) {
                            System.err.println(e + " Error when trying to write to a file: " + args[1]);
                        }
                    } catch (IOException e) {
                        System.err.println(e + " Error when trying to read a file: " + args[0]);
                    }
                } catch (IOException e) {
                    System.err.println(e + " Error when trying to create a directory for a path " + args[1]);
                } catch (InvalidPathException e) {
                    System.err.println(e + " Incorrect path: " + args[1]);
                }
            } catch (InvalidPathException e) {
                System.err.println(e + " Incorrect path: " + args[0]);
            }
        } else {
            System.err.println("null or empty or >2 arguments");
        }
    }
}

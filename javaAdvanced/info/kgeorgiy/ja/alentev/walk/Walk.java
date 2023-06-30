package javaAdvanced.info.kgeorgiy.ja.alentev.walk;

import java.io.BufferedReader;
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

public class Walk {
    /*private static final String zeroHash = "0000000000000000000000000000000000000000000000000000000000000000";
    private static String linuxToWin(String fileName){
        String[] name = fileName.split("/");
        return String.join(File.separator, name);
    }

    private static boolean isDirEmpty(final Path directory) throws IOException {
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
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

        public WalkFiles(BufferedWriter writer) {
            this.writer = writer;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] bytes = Files.readAllBytes(file);
                BigInteger bigInteger = new BigInteger(1, digest.digest(bytes));
                String hash = bigInteger.toString(16);
                String zeros = String.format("%032d", 0);
                hash = zeros.substring(0, 64 - hash.length()) + hash;
                setHash(hash, writer, file.toString());
            } catch (Exception e1) {
                try {
                    setHash(zeroHash, writer, file.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir,
                                                 BasicFileAttributes atr) throws IOException {
            System.out.format("Directory: %s%n", dir);
            if (atr.isDirectory()) {
                if (isDirEmpty(dir)) {
                    setHash(zeroHash, writer, dir.toString());
                }
            }
            return CONTINUE;
        }
        @Override
        public FileVisitResult visitFileFailed(Path file,
                                               IOException exc) {
            try {
                setHash(zeroHash, writer, file.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return CONTINUE;
        }
    }


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        try {
            Path fileIn = Paths.get(args[0]);
            Path fileOut = Paths.get(args[1]);
            if (!Files.isWritable(fileOut)) {
                Path dir = fileOut.getParent();
                Files.createDirectories(dir);
            }
            try (BufferedReader readerMain = Files.newBufferedReader(fileIn, StandardCharsets.UTF_8)) {
                List<String> text = Files.readAllLines(fileIn, StandardCharsets.UTF_8);
                try (BufferedWriter writer = Files.newBufferedWriter(fileOut, StandardCharsets.UTF_8)) {
                    RecursiveWalk.WalkFiles walkFiles = new RecursiveWalk.WalkFiles(writer);
                    for (String fileName : text) {
                        String normalFileName = linuxToWin(fileName);
                        try {
                            Path file = Paths.get(normalFileName);
                            Files.walkFileTree(file, walkFiles);
                        } catch (Exception e2) {
                            setHash(zeroHash, writer, fileName);
                        }
                    }
                } catch (Exception e3) {
                    System.out.println(e3.toString());
                }
            } catch (Exception e4) {

            }
        }catch (Exception e5){
            System.out.println(e5.toString());
        }
    }*/
}

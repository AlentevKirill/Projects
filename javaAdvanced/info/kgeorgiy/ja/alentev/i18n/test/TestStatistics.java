package javaAdvanced.info.kgeorgiy.ja.alentev.i18n.test;

import javaAdvanced.info.kgeorgiy.ja.alentev.i18n.TextStatistics;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestStatistics extends Assert {

    @Test
    public void test12() throws IOException {
        String out = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\out12.txt";
        String example = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\example12.txt";
        String[] args = new String[]{
                "en-US",
                "en",
                "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\test1.txt",
                out
        };
        test(args, out, example);
    }

    @Test
    public void test11() throws IOException {
        String out = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\out11.txt";
        String example = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\example11.txt";
        String[] args = new String[]{
                "en-US",
                "ru",
                "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\test1.txt",
                out
        };
        test(args, out, example);
    }

    @Test
    public void test2() throws IOException {
        String out = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\out2.txt";
        String example = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\example2.txt";
        String[] args = new String[]{
                "ru-RU",
                "en",
                "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\test2.txt",
                out
        };
        test(args, out, example);
    }

    @Test
    public void test3() throws IOException {
        String out = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\out3.txt";
        String example = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\example3.txt";
        String[] args = new String[]{
                "jp",
                "en",
                "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\test3.txt",
                out
        };
        test(args, out, example);
    }

    @Test
    public void test4() throws IOException {
        String out = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\out4.txt";
        String example = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\example4.txt";
        String[] args = new String[]{
                "ru-RU",
                "en",
                "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\test4.txt",
                out
        };
        test(args, out, example);
    }

    @Test
    public void test5() throws IOException {
        String out = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\out5.txt";
        String example = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\example5.txt";
        String[] args = new String[]{
                "ru-RU",
                "en",
                "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\test5.txt",
                out
        };
        test(args, out, example);
    }

    @Test
    public void test6() throws IOException {
        String out = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\out6.txt";
        String example = "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\example6.txt";
        String[] args = new String[]{
                "en-US",
                "ru",
                "java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test\\\\test6.txt",
                out
        };
        test(args, out, example);
    }

    @Test
    public void testError() throws IOException {
        String[] args = new String[]{
                "jp",
                "en",
        };
        try {
            test(args, null, null);
        } catch (NullPointerException e) {
            System.err.println("null");
        }
    }

    // :NOTE: Тестировать даты, валюты, числа
    private static void test(String[] args, String out, String example) throws IOException, NullPointerException {
        TextStatistics.main(args);
        Path outPath = Paths.get(out);
        String resultText = Files.readString(outPath, StandardCharsets.UTF_8);
        Path examplePath = Paths.get(example);
        String exampleText = Files.readString(examplePath, StandardCharsets.UTF_8);
        assertEquals(exampleText, resultText);
    }
}

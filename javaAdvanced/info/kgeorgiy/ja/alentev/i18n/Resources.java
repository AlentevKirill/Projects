package javaAdvanced.info.kgeorgiy.ja.alentev.i18n;

import java.util.ListResourceBundle;

public class Resources extends ListResourceBundle {
    private static final Object[][] CONTENTS = {
            {"ParseArgsExceptionru", "Входные данные должны содержать 4 не null элементов"},
            {"ParseArgsExceptionen", "The input data must contain 4 string variables without null"},
            {"UnsupportedLangru", "Выходной язык должен быть Английским или Русским"},
            {"UnsupportedLangen", "Output language should be ru or en"},
            {"invalidPathExceptionru", "Ошибка при разборе пути входного файла"},
            {"invalidPathExceptionen", "Invalid path of input file: "},
            {"readIoExceptionru", "Ошибка при чтении входного файла"},
            {"readIoExceptionen", "Can't read input text: "},
            {"writeInvalidPathExceptionru", "Ошибка при разборе пути выходного файла"},
            {"writeInvalidPathExceptionen", "Invalid path of output file: "},
            {"writeIoExceptionru", "Ошибка при записи в выходной файл"},
            {"writeIoExceptionen", "Can't write text: "},
            {"writeDirectoryInvalidPathExceptionru", "Ошибка при разборе пути директории-родителя выходного файла"},
            {"writeDirectoryInvalidPathExceptionen", "Invalid path of output file: "},
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}

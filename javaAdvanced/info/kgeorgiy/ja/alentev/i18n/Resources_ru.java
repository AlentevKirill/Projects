package javaAdvanced.info.kgeorgiy.ja.alentev.i18n;

import java.util.ListResourceBundle;

public class Resources_ru extends ListResourceBundle {
    private static final Object[][] CONTENTS = {
            {"templateHead", "{0}: {1}"},
            {"templateTitle", "{0}"},
            {"templateSummary", "{0} {1}: {2}."},
            {"templateCount", "{0} {1}: {2} ({3} {4})."},
            {"templateMinMaxString", "{0} {1}: \"{2}\"."},
            {"templateMinMaxLength", "{0} {1} {2}: {3} (\"{4}\")."},
            {"templateAverageString", "{0} {1} {2}: {3}."},
            {"templateMinMaxAverageNumbers", "{0} {1}: {2}."},
            {"templateNumberFormat", "{0, number, #.##}"},
            {"templateDateFormat", "{0, date, long}"},
            {"templateCurrencyFormat", "{0, number, #.##}"},
            //{"templateCurrencyFormat", "{0, number, currency}"},
            {"analyzedFile", "Анализируемый файл"},
            {"summaryStat", "Сводная статистика"},
            {"Number", "Число"},
            {"oneNumber", "число"},
            {"midNumber", "число"},
            {"manyNumber", "чисел"},
            {"wordStat", "Статистика по словам"},
            {"sentenceStat", "Статистика по предложениям"},
            {"dateStat", "Статистика по датам"},
            {"currencyStat", "Статистика по суммам денег"},
            {"numberStat", "Статистика по числам"},
            {"oneWord", "слово"},
            {"manyWord","слов"},
            {"womanWord","слова"},
            {"oneSentence", "предложение"},
            {"manySentence", "предложений"},
            {"womanSentence", "предложения"},
            {"oneDate", "дата"},
            {"manyDate", "дат"},
            {"womanDate", "дата"},
            {"oneCurrency", "сумма"},
            {"manyCurrency", "сумм"},
            {"womanCurrency", "сумма"},
            {"womanMin", "Минимальная"},
            {"midMin", "Минимальное"},
            {"womanMax", "Максимальная"},
            {"midMax", "Максимальное"},
            {"womanAverage", "Средняя"},
            {"midAverage", "Среднее"},
            {"length", "длина"},
            {"unique", "различных"}
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}

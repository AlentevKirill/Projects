package javaAdvanced.info.kgeorgiy.ja.alentev.i18n;

import java.util.ListResourceBundle;

public class Resources_en extends ListResourceBundle {
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
            {"analyzedFile", "Analyzed file"},
            {"summaryStat", "Summary statistics"},
            {"Number", "Number of"},
            {"oneNumber", "number"},
            {"manyNumber", "numbers"},
            {"midNumber", "number"},
            {"wordStat", "Word statistics"},
            {"sentenceStat", "Sentence statistics"},
            {"dateStat", "Date statistics"},
            {"currencyStat", "Currency statistics"},
            {"numberStat", "Number statistics"},
            {"oneWord", "word"},
            {"manyWord","words"},
            {"womanWord","word"},
            {"oneSentence", "sentence"},
            {"manySentence", "sentences"},
            {"womanSentence", "sentence"},
            {"oneDate", "date"},
            {"manyDate", "dates"},
            {"womanDate", "date"},
            {"oneCurrency", "amount"},
            {"manyCurrency", "amounts"},
            {"womanCurrency", "amount"},
            {"womanMin", "Minimum"},
            {"midMin", "Minimum"},
            {"womanMax", "Maximum"},
            {"midMax", "Maximum"},
            {"womanAverage", "Average"},
            {"midAverage", "Average"},
            {"length", "length of"},
            {"unique", "unique"}
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}

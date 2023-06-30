package javaAdvanced.info.kgeorgiy.ja.alentev.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateOutputFile {

    public static String generateOutputFile(
            Map<TypeOfElementStatistics, TextStatistics.Statistics<?>> allStatistics,
            String inputFileName,
            Locale outLocale,
            Locale inLocale
    ) throws IOException {
        ResourceBundle bundle;
        if (outLocale.getLanguage().equals("en")) {
            bundle = ResourceBundle.getBundle("info.kgeorgiy.ja.alentev.i18n.Resources_en");
        } else {
            bundle = ResourceBundle.getBundle("info.kgeorgiy.ja.alentev.i18n.Resources_ru");
        }
        String head = String.format(
                readTemplates("head.html"),
                MessageFormat.format(
                        bundle.getString("templateHead"),
                        bundle.getString("analyzedFile"),
                        inputFileName
                )
        );
        MessageFormat numberForm = new MessageFormat(bundle.getString("templateNumberFormat"), outLocale);
        MessageFormat dateForm = new MessageFormat(bundle.getString("templateDateFormat"), outLocale);
        MessageFormat currencyForm = new MessageFormat(bundle.getString("templateCurrencyFormat"), outLocale);
        List<String> list = Arrays.stream(TypeOfElementStatistics.values())
                .map(t -> MessageFormat.format(
                        bundle.getString("templateSummary"),
                        bundle.getString("Number"),
                        bundle.getString("many" + toLowerCaseEnum(t.toString(), inLocale)),
                        numberForm.format(new Object[]{allStatistics.get(t).getCountOfElement()})
                )).collect(Collectors.toList());
        list.add(0,
                MessageFormat.format(
                        bundle.getString("templateTitle"),
                        bundle.getString("summaryStat")
                )
        );
        String summery = String.format(readTemplates("summary-statistics.html"), list.toArray());
        String templateBlockString = readTemplates("block-string-statistics.html");
        String templateBlockNumber = readTemplates("block-number-statistics.html");
        list = Arrays.stream(TypeOfElementStatistics.values())
                .map(t -> {
                    TextStatistics.Statistics<?> statistics = allStatistics.get(t);
                    if (t.equals(TypeOfElementStatistics.SENTENCE) || t.equals(TypeOfElementStatistics.WORD)) {
                        String infixKey = toLowerCaseEnum(t.toString(), inLocale);
                        String[] messageFormatsString = new String[]{
                                MessageFormat.format(
                                        bundle.getString("templateTitle"),
                                        bundle.getString(infixKey.toLowerCase() + "Stat")
                                ),
                                MessageFormat.format(bundle.getString("templateCount"),
                                        bundle.getString("Number"),
                                        bundle.getString("many" + infixKey),
                                        numberForm.format(new Object[]{statistics.getCountOfElement()}),
                                        numberForm.format(new Object[]{statistics.getCountOfUniqElement()}),
                                        bundle.getString("unique")
                                ),
                                MessageFormat.format(bundle.getString("templateMinMaxString"),
                                        bundle.getString("midMin"),
                                        bundle.getString("one" + infixKey),
                                        statistics.getMinValueOfElement()
                                ),
                                MessageFormat.format(bundle.getString("templateMinMaxString"),
                                        bundle.getString("midMax"),
                                        bundle.getString("one" + infixKey),
                                        statistics.getMaxValueOfElement()
                                ),
                                MessageFormat.format(bundle.getString("templateMinMaxLength"),
                                        bundle.getString("womanMin"),
                                        bundle.getString("length"),
                                        bundle.getString("woman" + infixKey),
                                        numberForm.format(new Object[]{statistics.getMinLengthOfElement()}),
                                        statistics.getElementWithMinLength()
                                ),
                                MessageFormat.format(bundle.getString("templateMinMaxLength"),
                                        bundle.getString("womanMax"),
                                        bundle.getString("length"),
                                        bundle.getString("woman" + infixKey),
                                        numberForm.format(new Object[]{statistics.getMaxLengthOfElement()}),
                                        statistics.getElementWithMaxLength()
                                ),
                                MessageFormat.format(bundle.getString("templateAverageString"),
                                        bundle.getString("womanAverage"),
                                        bundle.getString("length"),
                                        bundle.getString("woman" + infixKey),
                                        numberForm.format(new Object[]{statistics.getAverageLength()})
                                )
                        };
                        return String.format(templateBlockString, (Object[]) messageFormatsString);
                    }
                    MessageFormat format = null;
                    String prefix = null;
                    switch (t) {
                        case DATE -> {
                            format = dateForm;
                            prefix = "woman";
                        }
                        case NUMBER -> {
                            format = numberForm;
                            prefix = "mid";
                        }
                        case CURRENCY -> {
                            format = currencyForm;
                            prefix = "woman";
                        }
                    }
                    String key = prefix + toLowerCaseEnum(t.toString(), inLocale);
                    String[] messageFormatsString = new String[]{
                            MessageFormat.format(
                                    bundle.getString("templateTitle"),
                                    bundle.getString(toLowerCaseEnum(t.toString(), inLocale).toLowerCase() + "Stat")
                            ),
                            MessageFormat.format(bundle.getString("templateCount"),
                                    bundle.getString("Number"),
                                    bundle.getString(key),
                                    numberForm.format(new Object[]{statistics.getCountOfElement()}),
                                    numberForm.format(new Object[]{statistics.getCountOfUniqElement()}),
                                    bundle.getString("unique")
                            ),
                            MessageFormat.format(bundle.getString("templateMinMaxAverageNumbers"),
                                    bundle.getString(prefix + "Min"),
                                    bundle.getString(key),
                                    format.format(new Object[]{statistics.getMinValueOfElement()})
                            ),
                            MessageFormat.format(bundle.getString("templateMinMaxAverageNumbers"),
                                    bundle.getString(prefix + "Max"),
                                    bundle.getString(key),
                                    format.format(new Object[]{statistics.getMaxValueOfElement()})
                            ),
                            MessageFormat.format(bundle.getString("templateMinMaxAverageNumbers"),
                                    bundle.getString(prefix + "Average"),
                                    bundle.getString(key),
                                    format.format(new Object[]{statistics.getAverageValueOfElement()})
                                    //formatNumbers(format, t, statistics.getAverageValueOfElement(), outLocale)
                            )
                    };
                    return String.format(templateBlockNumber, (Object[]) messageFormatsString);
                }).collect(Collectors.toList());
        list.add(0, summery);
        list.add(0, head);
        return String.format(
                readTemplates("result.html"),
                list.toArray());
    }

    /*private static String formatNumbers(MessageFormat format, TypeOfElementStatistics type, Object value, Locale locale) {
        if (type.equals(TypeOfElementStatistics.CURRENCY)) {
            return new String(NumberFormat.getCurrencyInstance(locale).format(value).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        } else {
            return format.format(new Object[]{value});
        }
    }*/

    private static String readTemplates(String templateName) throws IOException {
        // :NOTE: InputStream resourceAsStream = GenerateOutputFile.class.getResourceAsStream("/" + templateName);
        String url = Objects.requireNonNull(GenerateOutputFile.class.getResource(templateName))
                .toString()
                .replace("/", "\\")
                .replaceFirst("file:\\\\", "");

        return Files.readString(Path.of(new String(URLDecoder.decode(url, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)));
    }

    private static String toLowerCaseEnum(String string, Locale locale) {
        return string.charAt(0) + string.substring(1).toLowerCase(locale);
    }
}

package javaAdvanced.info.kgeorgiy.ja.alentev.i18n;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static javaAdvanced.info.kgeorgiy.ja.alentev.i18n.GenerateOutputFile.generateOutputFile;

public class TextStatistics {
    private static final Pattern LINE_SEP_PATTERN = Pattern.compile("\\r\\n|\\r|\\n");
    private static DateFormat LONG_DATE_FORMAT;
    private static DateFormat SHORT_DATE_FORMAT;
    private static DateFormat MEDIUM_DATE_FORMAT;
    private static DateFormat FULL_DATE_FORMAT;
    private static NumberFormat CURRENCY_FORMAT;

    //private static String currencySymbol;

    public static class Statistics<E> {

        private final int countOfElement;
        private final int countOfUniqElement;
        private final E minValueOfElement;
        private final E maxValueOfElement;
        private final int minLengthOfElement;
        private final int maxLengthOfElement;
        private final E elementWithMinLength;
        private final E elementWithMaxLength;
        private final E averageValueOfElement;
        private final double averageLength;

        public Statistics(
                int countOfElement,
                int countOfUniqElement,
                E minValueOfElement,
                E maxValueOfElement,
                int minLengthOfElement,
                int maxLengthOfElement,
                E elementWithMinLength,
                E elementWithMaxLength,
                double averageLength,
                E averageValueOfElement
        ) {
            this.countOfElement = countOfElement;
            this.countOfUniqElement = countOfUniqElement;
            this.minValueOfElement = minValueOfElement;
            this.maxValueOfElement = maxValueOfElement;
            this.minLengthOfElement = minLengthOfElement;
            this.maxLengthOfElement = maxLengthOfElement;
            this.elementWithMinLength = elementWithMinLength;
            this.elementWithMaxLength = elementWithMaxLength;
            this.averageValueOfElement = averageValueOfElement;
            this.averageLength = averageLength;
        }

        public Statistics() {
            this.countOfElement = 0;
            this.countOfUniqElement = 0;
            this.minValueOfElement = null;
            this.maxValueOfElement = null;
            this.minLengthOfElement = 0;
            this.maxLengthOfElement = 0;
            this.elementWithMinLength = null;
            this.elementWithMaxLength = null;
            this.averageValueOfElement = null;
            this.averageLength = 0;
        }

        public int getCountOfElement() {
            return countOfElement;
        }

        public int getCountOfUniqElement() {
            return countOfUniqElement;
        }

        public E getMinValueOfElement() {
            return minValueOfElement;
        }

        public E getMaxValueOfElement() {
            return maxValueOfElement;
        }

        public int getMinLengthOfElement() {
            return minLengthOfElement;
        }

        public int getMaxLengthOfElement() {
            return maxLengthOfElement;
        }

        public E getElementWithMinLength() {
            return elementWithMinLength;
        }

        public E getElementWithMaxLength() {
            return elementWithMaxLength;
        }

        public E getAverageValueOfElement() {
            return averageValueOfElement;
        }

        public double getAverageLength() {
            return averageLength;
        }
    }

    public static void main(String[] args) throws IOException {
        /*String inText = Files.readString(
                Paths.get("C:\\Users\\Кирилл\\Desktop\\java-advanced\\java-solutions\\info\\kgeorgiy\\ja\\alentev\\i18n\\test.txt"),
                StandardCharsets.UTF_8
        )*/
        //currencySymbol = new DecimalFormatSymbols(new Locale("ar", "AE")).getCurrencySymbol();

        ResourceBundle bundle = ResourceBundle.getBundle("info.kgeorgiy.ja.alentev.i18n.Resources");
        String localeException = "en";
        if (args.length != 4 || Arrays.stream(args).anyMatch(Objects::isNull)) {
            // :NOTE: локализация.
            System.err.println(bundle.getString("ParseArgsException" + localeException));
            return;
        }


        Locale inLocale = new Locale.Builder().setLanguageTag(args[0]).build(); /*new Locale("jp");*/
        Locale outLocale = new Locale.Builder().setLanguageTag(args[1]).build(); /*new Locale("en");*/
        localeException = outLocale.getLanguage();
        if (!"en".equals(outLocale.getLanguage()) && !"ru".equals(outLocale.getLanguage())) {
            // :NOTE: локализация.
            System.err.println(bundle.getString("UnsupportedLang" + localeException));
            return;
        }
        FULL_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.FULL, inLocale);
        SHORT_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT, inLocale);
        MEDIUM_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM, inLocale);
        LONG_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.LONG, inLocale);
        CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(inLocale);

        String inText = "";
        Path inPath = null;
        try {
            inPath = Paths.get(args[2]/*"java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\test.txt"*/);
            inText = Files.readString(inPath, StandardCharsets.UTF_8);
        } catch (InvalidPathException invalidPathException) {
            System.err.println(bundle.getString("invalidPathException" + localeException) + invalidPathException.getMessage());
        } catch (IOException ioException) {
            System.err.println(bundle.getString("readIoException" + localeException) + ioException.getMessage());
        }
        Map<TypeOfElementStatistics, Statistics<?>> allStatistics = parseText(inText, inLocale);
        String result = generateOutputFile(allStatistics,
                Objects.requireNonNull(inPath).getFileName().toString(),
                outLocale,
                inLocale);
        try {
            Path outPath = Paths.get(args[3]/*"java-solutions\\\\info\\\\kgeorgiy\\\\ja\\\\alentev\\\\i18n\\\\output.txt"*/);
            if (!Files.isWritable(outPath)) {
                Path dir = outPath.getParent();
                if (dir == null) {
                    throw new InvalidPathException(args[3],
                            bundle.getString("writeDirectoryInvalidPathException" + localeException)
                    );
                }
                Files.createDirectories(dir);
            }
            Files.writeString(outPath, result, StandardCharsets.UTF_8);
        } catch (InvalidPathException invalidPathException) {
            System.err.println(bundle.getString("writeInvalidPathException" + localeException) + invalidPathException.getMessage());
        } catch (IOException ioException) {
            System.err.println(bundle.getString("writeIoException" + localeException) + ioException.getMessage());
        }
    }

    private static Map<TypeOfElementStatistics, Statistics<?>> parseText(String text, Locale locale) {
        Map<TypeOfElementStatistics, Statistics<?>> allStatistics = new HashMap<>();
        allStatistics.put(TypeOfElementStatistics.SENTENCE, parseStringElement(
                BreakIterator.getSentenceInstance(locale),
                text,
                locale,
                TextStatistics::sentenceLength,
                s -> true
        ));
        allStatistics.put(TypeOfElementStatistics.WORD, parseStringElement(
                BreakIterator.getWordInstance(locale),
                text,
                locale,
                String::length,
                s -> Character.isLetter(s.charAt(0))
        ));
        allStatistics.put(TypeOfElementStatistics.NUMBER, parseNumbers(text, locale));
        allStatistics.put(TypeOfElementStatistics.DATE, parseDate(text, locale));
        allStatistics.put(TypeOfElementStatistics.CURRENCY, parseCurrency(text, locale));
        return allStatistics;
    }

    private static int sentenceLength(String sentence) {
        return LINE_SEP_PATTERN.matcher(sentence).replaceAll("").length();
    }

    private static Statistics<String> parseStringElement(
            BreakIterator breakIterator,
            String text,
            Locale inLocale,
            Function<String, Integer> checkLength,
            Predicate<String> checkWord
    ) {
        breakIterator.setText(text);
        int start = breakIterator.first();
        Collator collator = Collator.getInstance(inLocale);
        collator.setStrength(Collator.IDENTICAL);
        Set<CollationKey> setOfCollationKey = new TreeSet<>(CollationKey::compareTo);
        int count = 0;
        long sumLength = 0;
        for (int end = breakIterator.next(); end != BreakIterator.DONE; start = end, end = breakIterator.next()) {
            String elem = text.substring(start, end).trim();
            if (elem.isEmpty() || !checkWord.test(elem)) {
                continue;
            }
            sumLength += checkLength.apply(elem);
            setOfCollationKey.add(collator.getCollationKey(elem));
            count++;
        }
        List<CollationKey> collationKeyList = new ArrayList<>(setOfCollationKey);
        List<String> collationKeyListSortByLength = setOfCollationKey.stream()
                .map(CollationKey::getSourceString)
                .collect(Collectors.toList());
        collationKeyList.sort(CollationKey::compareTo);
        collationKeyListSortByLength.sort(Comparator.comparingInt(String::length));
        if (count == 0) {
            return new Statistics<>();
        }
        return new Statistics<>(
                count,
                setOfCollationKey.size(),
                collationKeyList.get(0).getSourceString(),
                collationKeyList.get(collationKeyList.size() - 1).getSourceString(),
                checkLength.apply(collationKeyListSortByLength.get(0)),
                checkLength.apply(collationKeyListSortByLength.get(collationKeyListSortByLength.size() - 1)),
                collationKeyListSortByLength.get(0),
                collationKeyListSortByLength.get(collationKeyListSortByLength.size() - 1),
                (((double) sumLength) / count),
                null
        );
    }


    private static Statistics<Double> parseNumbers(String text, Locale inLocale) {
        BreakIterator breakWord = BreakIterator.getWordInstance(inLocale);
        breakWord.setText(text);
        int start = breakWord.first();
        int skipDateOrCurrency = start;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(inLocale);
        List<Number> numbers = new ArrayList<>();
        for(int end = breakWord.next(); end != BreakIterator.DONE; start = end, end = breakWord.next()) {
            if (skipDateOrCurrency > start) {
                continue;
            }
            /*String word = text.substring(start);
            if (word.startsWith(currencySymbol)) {
                if (!Character.isLetterOrDigit(word.charAt(currencySymbol.length()))) {
                    end = breakWord.next();
                }
                continue;
            }*/
            final ParsePosition position = new ParsePosition(start);
            if (isDate(position, text) || isCurrency(position, text)) {
                skipDateOrCurrency = position.getIndex();
                continue;
            }
            Number number = numberFormat.parse(text, position);
            if (number != null) {
                numbers.add(number);
            }
        }
        Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
        Function<Stream<String>, Optional<String>> minLength = s -> s.min(lengthComparator);
        Function<Stream<String>, Optional<String>> maxLength = s -> s.max(lengthComparator);
        if (numbers.isEmpty()) {
            return new Statistics<>();
        }
        return new Statistics<>(
                numbers.size(),
                (int) numbers.stream().mapToDouble(Number::doubleValue).distinct().count(),
                getMinOrMaxOrAverageValue(numbers, DoubleStream::min),
                getMinOrMaxOrAverageValue(numbers, DoubleStream::max),
                getMinOrMaxLengthString(numbers, minLength).toString().length(),
                getMinOrMaxLengthString(numbers, maxLength).toString().length(),
                Double.parseDouble(getMinOrMaxLengthString(numbers, minLength).orElseThrow()),
                Double.parseDouble(getMinOrMaxLengthString(numbers, maxLength).orElseThrow()),
                -1,
                getMinOrMaxOrAverageValue(numbers, DoubleStream::average)
        );
    }

    private static Statistics<Date> parseDate(String text, Locale locale) {
        List<Date> dateList = getDateOrConcurrencyList(text, locale, p -> parseDateIfPossible(p, text));
        if (dateList.isEmpty()) {
            return new Statistics<>();
        }
        return new Statistics<>(
                dateList.size(),
                (int) dateList.stream().distinct().count(),
                dateList.stream().min(Date::compareTo).orElseThrow(),
                dateList.stream().max(Date::compareTo).orElseThrow(),
                -1,
                -1,
                null,
                null,
                -1,
                new Date(dateList
                        .stream()
                        .map(date -> BigInteger.valueOf(date.getTime()))
                        .reduce(BigInteger.ZERO, BigInteger::add)
                        .divide(BigInteger.valueOf(dateList.size())).longValue()
                )
        );
    }

    private static Statistics<Double> parseCurrency(String text, Locale locale) {
        List<Number> concurrencyList = getDateOrConcurrencyList(text, locale, p -> parseCurrencyIfPossible(p, text));
        if (concurrencyList.isEmpty()) {
            return new Statistics<>();
        }
        return new Statistics<>(
                concurrencyList.size(),
                (int) concurrencyList.stream().mapToDouble(Number::doubleValue).distinct().count(),
                getMinOrMaxOrAverageValue(concurrencyList, DoubleStream::min),
                getMinOrMaxOrAverageValue(concurrencyList, DoubleStream::max),
                -1,
                -1,
                null,
                null,
                -1,
                getMinOrMaxOrAverageValue(concurrencyList, DoubleStream::average)
        );
    }

    private static <E> List<E> getDateOrConcurrencyList(
            String text,
            Locale locale,
            Function<ParsePosition, E> parseFunction
    ) {
        BreakIterator breakWord = BreakIterator.getWordInstance(locale);
        breakWord.setText(text);
        int start = breakWord.first();
        int skipDateOrCurrency = start;
        List<E> result = new ArrayList<>();
        for(int end = breakWord.next(); end != BreakIterator.DONE; start = end, end = breakWord.next()) {
            if (skipDateOrCurrency > start) {
                continue;
            }
            ParsePosition position = new ParsePosition(start);
            E elem = parseFunction.apply(position);
            if (elem != null) {
                result.add(elem);
                skipDateOrCurrency = position.getIndex();
            }
        }
        return result;
    }

    private static double getMinOrMaxOrAverageValue(List<Number> list, Function<DoubleStream, OptionalDouble> func) {
        return func.apply(list.stream().mapToDouble(Number::doubleValue)).orElseThrow();
    }

    private static Optional<String> getMinOrMaxLengthString(List<Number> list, Function<Stream<String>, Optional<String>> func) {
        return func.apply(list.stream().map(Number::toString));
    }

    private static boolean isDate(ParsePosition currentPosition, String text) {
        return parseDateIfPossible(currentPosition, text) != null;
    }


    private static boolean isCurrency(ParsePosition currentPosition, String text) {
        /*if (text.startsWith(currencySymbol, currentPosition.getIndex())) {
            return parsePrefixCurrency(currentPosition, text, locale) != null;
        }*/
        return parseCurrencyIfPossible(currentPosition, text) != null;
    }

    private static Date parseDateIfPossible(ParsePosition currentPosition, String text) {
        int[] formats = new int[]{DateFormat.FULL, DateFormat.LONG, DateFormat.DEFAULT, DateFormat.SHORT};
        Date result;
        for (int format:
             formats) {
            // :NOTE: DateFormat.getDateInstance(format, locale) -> const
            switch (format) {
                case DateFormat.FULL:
                    result = FULL_DATE_FORMAT.parse(text, currentPosition);
                    break;
                    case DateFormat.LONG:
                        result = LONG_DATE_FORMAT.parse(text, currentPosition);
                        break;
                        case DateFormat.MEDIUM:
                            result = MEDIUM_DATE_FORMAT.parse(text, currentPosition);
                            break;
                            case DateFormat.SHORT:
                                result = SHORT_DATE_FORMAT.parse(text, currentPosition);
                                break;
                default:
                    return null;
            }
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /*private static Number parsePrefixCurrency(ParsePosition currentPosition, String text, Locale locale) {

        Number number = NumberFormat.getInstance(locale).parse(text, currentPosition);
        return number;
    }*/

    private static Number parseCurrencyIfPossible(ParsePosition currentPosition, String text) {
        // :NOTE: в константу
        return CURRENCY_FORMAT.parse(text, currentPosition);
    }


}

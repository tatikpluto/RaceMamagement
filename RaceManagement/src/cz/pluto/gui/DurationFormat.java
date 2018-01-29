package cz.pluto.gui;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationFormat extends Format {
    private DateTimeFormatSymbols dateTimeFormatSymbols;

    /**
     * Default duration pattern, which is used if no one is specified through constructor.
     */
    private static final String DEFAULT_PATTERN = "d h m s sss";

    /**
     * Pattern used by factory method {@link #getDaysInstance()}.
     * Internally it equals to {@link #DEFAULT_PATTERN}.
     */
    public static final String DAYS_PATTERN = DEFAULT_PATTERN;

    /**
     * Pattern used by factory method {@link #getHoursInstance()}.
     */
    public static final String HOURS_PATTERN = "h m s sss";

    public static final long MILLISECOND = 1;
    public static final long SECOND_IN_MILLIS = 1000 * MILLISECOND;
    public static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;
    public static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
    public static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;

    public static final long DAY_IN_HOURS = 24;
    public static final long HOUR_IN_MINUTES = 60;
    public static final long MINUTE_IN_SECONDS = 60;

    private static final int FIELD_COUNT = 6;

    /**
     * Regular expression pattern used to parse input value.
     */
    private Pattern compiledPattern;

    /**
     * <p/>
     * Special format of duration used for parsing and formating.
     * </P>
     * <p/>
     * <B>Duration symbols</B>
     * <UL>
     * <LI>D - long format of day (currently unsupported)</LI>
     * <LI>d - short format of day</LI>
     * <LI>H - long format of hour (currently unsupported)</LI>
     * <LI>h - short format of hour</LI>
     * <LI>M - long format of minute (currently unsupported)</LI>
     * <LI>m - short format of minute</LI>
     * <LI>S - long format of second (currently unsupported)</LI>
     * <LI>s - short format of hour</LI>
     * <LI>SSS - long format of millisecond (currently unsupported)</LI>
     * <LI>sss - short format of millisecond</LI>
     * </UL>
     * </P>
     * <p/>
     * <B>Quotation symbol</B>
     * <UL>
     * <LI>' - all text between two apostrophes is quoted</LI>
     * </UL>
     * </P>
     */
    private String pattern;

    /**
     * Fields used by format. Fields are initialized inside {@link #compile(String)}
     * method.
     */
    private String[] field;

    /**
     * Array holding order of fields according to duration format.
     * Used for formatting.
     */
    private FieldType[] fieldType;

    /**
     * Array holding multipliers according to duration format.
     * Used for parsing.
     */
    private long[] multiplier;

    private boolean daySet = false;
    private boolean hourSet = false;
    private boolean minuteSet = false;
    private boolean secondSet = false;
    private boolean millisecondSet = false;

    /**
     * Currently used locale.
     */
    private Locale locale;

    public DurationFormat() {
        this(DEFAULT_PATTERN);
    }

    /**
     * Constructor with duration pattern and default locale.
     *
     * @param pattern
     * @see #pattern
     */
    public DurationFormat(String pattern) {
        this(pattern, Locale.getDefault());
    }

    public DurationFormat(String pattern, Locale locale) {
        initLocalVariables();
        init(pattern, locale);
    }

    private void initLocalVariables() {
        field = new String[FIELD_COUNT];

        fieldType = new FieldType[FIELD_COUNT - 1];

        multiplier = new long[FIELD_COUNT - 1];
    }

    /**
     * Factory method with predefined pattern {@link #DAYS_PATTERN} and default locale.
     *
     * @return instance of DurationFormat according to {@link #DAYS_PATTERN}
     */
    public static DurationFormat getDaysInstance() {
        return getDaysInstance(Locale.getDefault());
    }

    /**
     * Factory method with predefined pattern {@link #DAYS_PATTERN} and desired locale.
     *
     * @return instance of DurationFormat according to {@link #DAYS_PATTERN}
     */
    public static DurationFormat getDaysInstance(Locale locale) {
        return new DurationFormat(DAYS_PATTERN, locale);
    }

    /**
     * Factory method with predefined pattern {@link #HOURS_PATTERN} and default locale.
     *
     * @return instance of DurationFormat according to {@link #HOURS_PATTERN}
     */
    public static DurationFormat getHoursInstance() {
        return getHoursInstance(Locale.getDefault());
    }

    /**
     * Factory method with predefined pattern {@link #HOURS_PATTERN} and desired locale.
     *
     * @return instance of DurationFormat according to {@link #HOURS_PATTERN}
     */
    public static DurationFormat getHoursInstance(Locale locale) {
        return new DurationFormat(HOURS_PATTERN, locale);
    }

    /**
     * Initialize format. Called from constructors. Internally it uses
     * call of {@link #applyPattern(String)} to set up used pattern.
     *
     * @param pattern initial pattern of this duration format
     * @param locale  used locale
     */
    private void init(String pattern, Locale locale) {
        this.locale = locale;
        dateTimeFormatSymbols = new DateTimeFormatSymbols(this.locale);
        applyPattern(pattern);
    }

    @Override
    public Object clone() {
        DurationFormat other = (DurationFormat) super.clone();

        other.compiledPattern = this.compiledPattern != null ? Pattern.compile(this.compiledPattern.pattern(), this.compiledPattern.flags()) : null;
        other.pattern = this.pattern;
        other.field = this.field != null ? Arrays.copyOf(this.field, this.field.length) : null;
        other.fieldType = this.fieldType != null ? Arrays.copyOf(this.fieldType, this.fieldType.length) : null;
        other.multiplier = this.multiplier != null ? Arrays.copyOf(this.multiplier, this.multiplier.length) : null;
        other.daySet = this.daySet;
        other.hourSet = this.hourSet;
        other.minuteSet = this.minuteSet;
        other.secondSet = this.secondSet;
        other.millisecondSet = this.millisecondSet;
        other.locale = this.locale != null ? (Locale) this.locale.clone() : null;

        return other;
    }

    /**
     * Compile input duration pattern as regular expression pattern and set
     * some local variables as side effect.
     *
     * @param pattern duration pattern
     * @return compiled pattern, set local variable as side effect:
     * <UL>
     * <LI>{@link #daySet}</LI>
     * <LI>{@link #hourSet}</LI>
     * <LI>{@link #minuteSet}</LI>
     * <LI>{@link #secondSet}</LI>
     * <LI>{@link #millisecondSet}</LI>
     * <LI>{@link #multiplier}</LI>
     * <LI>{@link #field}</LI>
     * <LI>{@link #fieldType}</LI>
     * </UL>
     * @throws IllegalArgumentException               if duration pattern seems to be wrong
     * @throws java.util.regex.PatternSyntaxException if associated regex pattern can't be compiled
     * @see #pattern
     */
    private Pattern compile(String pattern) {
        final int extraSpace = 32;
        final String regexCharsToEscape = "+()^$.{}[]|?:*";
        final String negativeRegex = "(\\s*+(?:-)?+\\s*+)";
        boolean isInsideQuotation = false;
        boolean isNegativeSet = false;
        int multiplierIdx = 0;
        int fieldIdx = 0;
        int fieldTypeIdx = 0;

        daySet = false;
        hourSet = false;
        minuteSet = false;
        secondSet = false;
        millisecondSet = false;

        // temp string buffers used for pattern and fields constructions
        StringBuilder parseRegex = new StringBuilder(pattern.length() + extraSpace);
        StringBuffer fieldBuffer[] = new StringBuffer[FIELD_COUNT];
        for (int i = 0; i < FIELD_COUNT; i++) {
            fieldBuffer[i] = new StringBuffer(pattern.length() / FIELD_COUNT + extraSpace);
        }

        char[] chars = pattern.toCharArray();

        // Go through all characters in duration pattern and try to construct
        // associated regular expression pattern used in parsing. Also tries
        // to construct fields used in formatting.
        for (int i = 0; i < chars.length; i++) {
            // escape character
            if (chars[i] == '\\') {
                if (i + 1 == chars.length) { // last character
                    throw new IllegalArgumentException("Last character can't be single escape character (\\).");
                } else if (chars[i + 1] == '\\') { // escaped escape character
                    parseRegex.append(isInsideQuotation ? "\\" : "\\\\");
                    fieldBuffer[fieldIdx].append('\\');
                } else { // escaped some other character
                    if (!isInsideQuotation) parseRegex.append('\\');
                    parseRegex.append(chars[i + 1]);
                    fieldBuffer[fieldIdx].append(chars[i + 1]);
                }
                i++; // move up one, because we have consumpted two characters
                continue;
            }

            // set inside quotation flag, remove (do not add) quotations marks from (to) regex
            // and use special quotations escape marks in regex (\Q, \E)
            if (chars[i] == '\'') {
                if (isInsideQuotation) {
                    parseRegex.append("\\E");
                } else {
                    parseRegex.append("\\Q");
                }
                isInsideQuotation = !isInsideQuotation;
                continue;
            }

            // leave text inside quotations unchanged and continue
            if (isInsideQuotation) {
                parseRegex.append(chars[i]);
                fieldBuffer[fieldIdx].append(chars[i]);
                continue;
            }

            // day symbol, value is stored in captured group
            if (chars[i] == 'd') {
                if (daySet)
                    throw new IllegalArgumentException("Day already used in pattern. It can be used at most once.");
                daySet = true;
                multiplier[multiplierIdx++] = DAY_IN_MILLIS;
                fieldType[fieldTypeIdx++] = FieldType.DAY;
                if (!(isNegativeSet)) parseRegex.append(negativeRegex);
                isNegativeSet = true;
                parseRegex.append("(?:(\\d*+)\\s*+").append(dateTimeFormatSymbols.getShortDay()).append(")?+");
                fieldBuffer[++fieldIdx].append(' ').append(dateTimeFormatSymbols.getShortDay());

                // long day symbol, value is stored in captured group
            } else if (chars[i] == 'D') {
                throw new UnsupportedOperationException("Long day is not currently supported.");
                //if(daySet==true) throw new IllegalArgumentException("Day already used in pattern. It can be used at most once.");
                //daySet = true;
                //multiplier[multiplierIdx++] = DAY;
                //fieldType[fieldTypeIdx++] = FieldType.DAY;
                //if(!(isNegativeSet)) parseRegex.append(negativeRegex);
                //isNegativeSet = true;

                // hour symbol, value is stored in captured group
            } else if (chars[i] == 'h') {
                if (hourSet)
                    throw new IllegalArgumentException("Hour already used in pattern. It can be used at most once.");
                hourSet = true;
                multiplier[multiplierIdx++] = HOUR_IN_MILLIS;
                fieldType[fieldTypeIdx++] = FieldType.HOUR;
                if (!(isNegativeSet)) parseRegex.append(negativeRegex);
                isNegativeSet = true;
                parseRegex.append("(?:(\\d*+)\\s*+").append(dateTimeFormatSymbols.getShortHour()).append(")?+");
                fieldBuffer[++fieldIdx].append(' ').append(dateTimeFormatSymbols.getShortHour());

                // long hour symbol, value is stored in captured group
            } else if (chars[i] == 'H') {
                throw new UnsupportedOperationException("Long hour is not currently supported.");
                //if(hourSet==true) throw new IllegalArgumentException("Hour already used in pattern. It can be used at most once.");
                //hourSet = true;
                //multiplier[multiplierIdx++] = HOUR;
                //fieldType[fieldTypeIdx++] = FieldType.HOUR;
                //if(!(isNegativeSet)) parseRegex.append(negativeRegex);
                //isNegativeSet = true;

                // minute symbol, value is stored in captured group
            } else if (chars[i] == 'm') {
                if (minuteSet)
                    throw new IllegalArgumentException("Minute already used in pattern. It can be used at most once.");
                minuteSet = true;
                multiplier[multiplierIdx++] = MINUTE_IN_MILLIS;
                fieldType[fieldTypeIdx++] = FieldType.MINUTE;
                if (!(isNegativeSet)) parseRegex.append(negativeRegex);
                isNegativeSet = true;
                parseRegex.append("(?:(\\d*+)\\s*+").append(dateTimeFormatSymbols.getShortMinute()).append(")?+");
                fieldBuffer[++fieldIdx].append(' ').append(dateTimeFormatSymbols.getShortMinute());

                // long minute symbol, value is stored in captured group
            } else if (chars[i] == 'M') {
                throw new UnsupportedOperationException("Long minute is not currently supported.");
                //if(minuteSet==true) throw new IllegalArgumentException("Minute already used in pattern. It can be used at most once.");
                //minuteSet = true;
                //multiplier[multiplierIdx++] = MINUTE;
                //fieldType[fieldTypeIdx++] = FieldType.MINUTE;
                //if(!(isNegativeSet)) parseRegex.append(negativeRegex);
                //isNegativeSet = true;

                // second or millisecond symbol, value is stored in captured group
            } else if (chars[i] == 's') {
                if (!(isNegativeSet)) parseRegex.append(negativeRegex);
                isNegativeSet = true;
                parseRegex.append("(?:(\\d*+)\\s*+"); // number + space + localized name
                if (i + 2 < chars.length && chars[i + 1] == 's' && chars[i + 2] == 's') {
                    if (millisecondSet)
                        throw new IllegalArgumentException("Millisecond already used in pattern. It can be used at most once.");
                    millisecondSet = true;
                    multiplier[multiplierIdx++] = MILLISECOND;
                    fieldType[fieldTypeIdx++] = FieldType.MILLISECOND;
                    parseRegex.append(dateTimeFormatSymbols.getShortMillisecond()).append(")?+");
                    fieldBuffer[++fieldIdx].append(' ').append(dateTimeFormatSymbols.getShortMillisecond());
                    i += 2;
                } else {
                    if (secondSet)
                        throw new IllegalArgumentException("Second already used in pattern. It can be used at most once.");
                    secondSet = true;
                    multiplier[multiplierIdx++] = SECOND_IN_MILLIS;
                    fieldType[fieldTypeIdx++] = FieldType.SECOND;
                    parseRegex.append(dateTimeFormatSymbols.getShortSecond()).append(")?+");
                    fieldBuffer[++fieldIdx].append(' ').append(dateTimeFormatSymbols.getShortSecond());
                }

                // long second or millisecond symbol, value is stored in captured group
            } else if (chars[i] == 'S') {
                if (i + 2 < chars.length && chars[i + 1] == 'S' && chars[i + 2] == 'S') {
                    throw new UnsupportedOperationException("Long millisecond is not currently supported.");
                    //if(millisecondSet==true) throw new IllegalArgumentException("Millisecond already used in pattern. It can be used at most once.");
                    //millisecondSet = true;
                    //multiplier[multiplierIdx++] = MILLISECOND;
                    //fieldType[fieldTypeIdx++] = FieldType.MILLISECOND;
                    //i+=2;
                } 
                throw new UnsupportedOperationException("Long second is not currently supported.");
                //if(secondSet==true) throw new IllegalArgumentException("Second already used in pattern. It can be used at most once.");
                //secondSet = true;
                //multiplier[multiplierIdx++] = SECOND;
                //fieldType[fieldTypeIdx++] = FieldType.SECOND;
                // symbol to escape
            } else if (regexCharsToEscape.indexOf(chars[i]) != -1) {
                parseRegex.append('\\').append(chars[i]);
                fieldBuffer[fieldIdx].append(chars[i]);

                // space
            } else if (chars[i] == ' ') {
                parseRegex.append("\\s*+");
                fieldBuffer[fieldIdx].append(chars[i]);

                // other symbols
            } else {
                parseRegex.append(chars[i]);
                fieldBuffer[fieldIdx].append(chars[i]);
            }
        }

        // if we still inside quotation after pattern parsing, it means an error, so, throw exception
        if (isInsideQuotation)
            throw new IllegalArgumentException("Improper usage of quotation marks (unpaired count of quotations).");

        // set members variables according to previous computing
        for (int i = 0; i < FIELD_COUNT; i++) {
            field[i] = fieldBuffer[i].toString();
        }

        return Pattern.compile(parseRegex.toString());
    }

    /**
     * Get currently used pattern for formatting.
     *
     * @return current pattern
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String toPattern() {
        return pattern;
    }

    /**
     * Applies the given pattern string to this duration format.
     *
     * @param pattern new pattern used for parsing/formating
     */
    public void applyPattern(String pattern) {
        initLocalVariables();

        compiledPattern = compile(pattern);
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        String res = format((Long) obj);
        toAppendTo.append(res);
        return new StringBuffer(res);
    }

    public String format(Long duration) {
        // check null case
        if (duration == null) {
            return "";
        }

        boolean negative = false;
        boolean zero = false;

        if (duration == 0) {
            zero = true;
        } else if (duration < 0) {
            negative = true;
            duration = -duration;
        }

        long remainder = duration;

        long days = duration / DAY_IN_MILLIS;
        remainder -= days * DAY_IN_MILLIS;

        long hours = remainder / HOUR_IN_MILLIS;
        remainder -= hours * HOUR_IN_MILLIS;

        long minutes = remainder / MINUTE_IN_MILLIS;
        remainder -= minutes * MINUTE_IN_MILLIS;

        long seconds = remainder / SECOND_IN_MILLIS;

        long milliseconds = duration % 1000;

        if (!daySet) {
            hours += days * DAY_IN_HOURS;
            days = 0;
        }

        if (!hourSet) {
            minutes += hours * HOUR_IN_MINUTES;
            hours = 0;
        }

        if (!minuteSet) {
            seconds += minutes * MINUTE_IN_SECONDS;
            minutes = 0;
        }

        if (!secondSet) {
            milliseconds += seconds * SECOND_IN_MILLIS;
            seconds = 0;
        }

        StringBuilder res = new StringBuilder();
        int fieldOrder = 0;

        // if value is negative, add negative prefix
        if (negative) res.append('-');

        res.append(field[fieldOrder]);

        if (zero) {

            // if value is zero, use first found field only
            res.append(0);
            res.append(field[++fieldOrder]);

        } else {

            // go through all fields, if the value is not zero
            for (int fieldTypeOrder = 0; (fieldTypeOrder < FIELD_COUNT - 1) && (fieldType[fieldTypeOrder] != null); fieldTypeOrder++) {

                switch (fieldType[fieldTypeOrder]) {
                    case UNKNOWN:
                        break;
                    case DAY: {
                        if (daySet) {
                            if (days == 0) { // skip zero field
                                fieldOrder++;
                                continue;
                            }
                            res.append(days);
                            res.append(field[++fieldOrder]);
                        }
                        break;
                    }

                    case HOUR: {
                        if (hourSet) {
                            if (hours == 0) { // skip zero field
                                fieldOrder++;
                                continue;
                            }
                            res.append(hours);
                            res.append(field[++fieldOrder]);
                        }
                        break;
                    }

                    case MINUTE: {
                        if (minuteSet) {
                            if (minutes == 0) { // skip zero field
                                fieldOrder++;
                                continue;
                            }
                            res.append(minutes);
                            res.append(field[++fieldOrder]);
                        }
                        break;
                    }

                    case SECOND: {
                        if (secondSet) {
                            if (seconds == 0) { // skip zero field
                                fieldOrder++;
                                continue;
                            }
                            res.append(seconds);
                            res.append(field[++fieldOrder]);
                        }
                        break;
                    }

                    case MILLISECOND: {
                        if (millisecondSet) {
                            if (milliseconds == 0) { // skip zero field
                                fieldOrder++;
                                continue;
                            }
                            res.append(milliseconds);
                            res.append(field[++fieldOrder]);
                        }
                        break;
                    }
                } // switch
            } // for
        }

        String value = res.toString().trim();

        if (value.isEmpty()) {
            value = res.append(0).toString().trim();
        }

        // return trimmed string buffer
        return value;
    }

    /**
     * Implementation of {@link java.text.Format#parseObject(String, ParsePosition)}.
     *
     * @param duration string representation of duration to parse
     * @param pos      starting position of parsing
     * @return <UL>
     * <LI>parsed value, if parsing was successful</LI>
     * <LI>null, if error was occured</LI>
     * </UL>
     */
    @Override
    public Object parseObject(String duration, ParsePosition pos) {
        long parsedDuration = 0;
        boolean negative = false;

        // prepare string to parse from given position
        String parsingString = duration.substring(pos.getIndex());

        Matcher m = compiledPattern.matcher(parsingString);
        boolean isOk = m.matches();

        if (isOk) {
            // matches is OK, count parsed value - go through all captured groups
            // group No. 0 - whole regex
            // group No. 1 - minus sign
            int count = m.groupCount();
            int shift = 2;
            if (m.group(1) != null && m.group(1).trim().equals("-")) negative = true;
            for (int i = shift; i <= count; i++) {
                String g = m.group(i);
                if (g != null && g.length() != 0) {
                    long l = Long.parseLong(g);
                    parsedDuration += l * multiplier[i - shift];
                }
            }
            // set position to first unused character according to Format spec
            // it should be used for next call of parseObject
            pos.setIndex(pos.getIndex() + m.end());
        } else {
            // matches is NOT OK, set error index to error position
            boolean lookOk = m.lookingAt();
            pos.setErrorIndex(lookOk ? pos.getIndex() + m.end() : pos.getIndex());
        }

        if (negative) parsedDuration = -parsedDuration;

        // return parsed value or <code>null</code>, if parsing was unsuccessful
        return isOk ? parsedDuration : null;
    }

    public Long parse(String duration) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        Long result = (Long) parseObject(duration, pos);
        if (pos.getErrorIndex() >= 0) {
            throw new ParseException("Unparseable duration: \"" + duration + "\", pattern: \"" + pattern + "\", near by index: " + pos.getErrorIndex(), pos.getErrorIndex());
        }
        return result;
    }

    public enum FieldType {
        @SuppressWarnings({"UnusedDeclaration"})
        UNKNOWN, DAY, HOUR, MINUTE, SECOND, MILLISECOND
    }

    public static class Field extends Format.Field {
        public Field(String name) {
            super(name);
        }

        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field DAY_FIELD = new Field("Day");
        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field SHORT_DAY_FIELD = new Field("Short day");
        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field HOUR_FIELD = new Field("Hour");
        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field SHORT_HOUR_FIELD = new Field("Short hour");
        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field MINUTE_FIELD = new Field("Minute");
        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field SHORT_MINUTE_FIELD = new Field("Short minute");
        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field SECOND_FIELD = new Field("Second");
        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field SHORT_SECOND_FIELD = new Field("Short second");
        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field MILLISECOND_FIELD = new Field("Millisecond");
        @SuppressWarnings({"UnusedDeclaration"})
        public final static Field SHORT_MILLISECOND_FIELD = new Field("Short millisecond");
    }

    public static String updateDurationUserValueToFormat(String userValue, String pattern) {
        if (pattern == null || pattern.equals("")) {
            pattern = DAYS_PATTERN;
        }
        StringTokenizer tokenizer = new StringTokenizer(pattern, " ");
        userValue = userValue.trim();
        String newValue = "";
        String prevToken = null;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!userValue.contains(token)) {
                if (userValue.replaceAll("[\\d|\\s]", "").equals("") && newValue.equals("")) {
                    return userValue + (userValue.contains(" ") ? "" : " ") + token;
                }
            } else {
                newValue += prevToken == null ?
                        userValue.substring(0, userValue.indexOf(token) + 1) :
                        userValue.substring(userValue.indexOf(prevToken) + 1, userValue.indexOf(token) + 1);
                if (userValue.endsWith(token)) {
                    return newValue;
                } 
                if (userValue.substring(userValue.indexOf(token) + 1).replaceAll("[\\d|\\s]", "").equals("")) {
                    if (tokenizer.hasMoreTokens()) {
                        newValue += userValue.substring(userValue.indexOf(token) + 1) + " " + tokenizer.nextToken();
                        return newValue;
                    }
                }
            }
            prevToken = token;
        }
        return newValue;
    }

    public static long getStepValue(String userValue, String pattern) {
        if (userValue == null) {
            return 0;
        }
        String lastPatternElement = updateDurationUserValueToFormat(userValue, pattern).replaceAll("[\\d]", "");
        if (lastPatternElement == null) {
            return 1;
        }
        if (lastPatternElement.endsWith("d")){
            return 24;
        }
        return 1;
    }
}

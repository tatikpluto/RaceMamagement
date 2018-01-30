package cz.pluto.gui;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

@SuppressWarnings("serial")
public class DateTimeFormatSymbols extends DateFormatSymbols {
    String[] days = null;
    String shortDay = null;
    String[] hours = null;
    String shortHour = null;
    String[] minutes = null;
    String shortMinute = null;
    String[] seconds = null;
    String shortSecond = null;
    String[] milliseconds = null;
    String shortMillisecond = null;

    /**
     * Construct a DateTimeFormatSymbols object by loading format data from
     * resources for the default locale.
     *
     * @exception  java.util.MissingResourceException
     *             if the resources for the default locale cannot be
     *             found or cannot be loaded.
     */
    public DateTimeFormatSymbols() {
        this(Locale.getDefault());
    }

    /**
     * Construct a DateTimeFormatSymbols object by loading format data from
     * resources for the given locale.
     *
     * @exception  java.util.MissingResourceException
     *             if the resources for the specified locale cannot be
     *             found or cannot be loaded.
     */
    public DateTimeFormatSymbols(Locale locale) {
        super(locale);
        initializeLocaleData(locale);
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public String getShortDay() {
        return shortDay;
    }

    public void setShortDay(String shortDay) {
        this.shortDay = shortDay;
    }

    public String[] getHours() {
        return hours;
    }

    public void setHours(String[] hours) {
        this.hours = hours;
    }

    public String getShortHour() {
        return shortHour;
    }

    public void setShortHour(String shortHour) {
        this.shortHour = shortHour;
    }

    public String[] getMinutes() {
        return minutes;
    }

    public void setMinutes(String[] minutes) {
        this.minutes = minutes;
    }

    public String getShortMinute() {
        return shortMinute;
    }

    public void setShortMinute(String shortMinute) {
        this.shortMinute = shortMinute;
    }

    public String[] getSeconds() {
        return seconds;
    }

    public void setSeconds(String[] seconds) {
        this.seconds = seconds;
    }

    public String getShortSecond() {
        return shortSecond;
    }

    public void setShortSecond(String shortSecond) {
        this.shortSecond = shortSecond;
    }

    public String[] getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(String[] milliseconds) {
        this.milliseconds = milliseconds;
    }

    public String getShortMillisecond() {
        return shortMillisecond;
    }

    public void setShortMillisecond(String shortMillisecond) {
        this.shortMillisecond = shortMillisecond;
    }

    private void initializeLocaleData(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle(TimeFormatData.class.getName(), locale);

        setDays(rb.getStringArray("days"));
        setShortDay(rb.getString("shortDay"));

        setHours(rb.getStringArray("hours"));
        setShortHour(rb.getString("shortHour"));

        setMinutes(rb.getStringArray("minutes"));
        setShortMinute(rb.getString("shortMinute"));

        setSeconds(rb.getStringArray("seconds"));
        setShortSecond(rb.getString("shortSecond"));

        setMilliseconds(rb.getStringArray("milliseconds"));
        setShortMillisecond(rb.getString("shortMillisecond"));
    }
    
}

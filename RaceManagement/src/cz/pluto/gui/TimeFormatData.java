package cz.pluto.gui;

import java.util.ListResourceBundle;

public class TimeFormatData extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {

            // "days" key
            new Object[] {
                "days", // key
                new String[] {"day", "days"} // value
            },

            // "shortDay" key
            new Object[] {
                "shortDay", // key
                "d" // value
            },

            // "hours" key
            new Object[] {
                "hours", // key
                new String[] {"hour", "hours"} // value
            },

            // "shortHour" key
            new Object[] {
                "shortHour", // key
                "h" // value
            },

            // "minutes" key
            new Object[] {
                "minutes", // key
                new String[] {"minute", "minutes"} // value
            },

            // "shortMinute" key
            new Object[] {
                "shortMinute", // key
                "m" // value
            },

            // "seconds" key
            new Object[] {
                "seconds", // key
                new String[] {"second", "seconds"} // value
            },

            // "shortSecond" key
            new Object[] {
                "shortSecond", // key
                "s" // value
            },

            // "milliseconds" key
            new Object[] {
                "milliseconds", // key
                new String[] {"millisecond", "milliseconds"} // value
            },

            // "shortMillisecond" key
            new Object[] {
                "shortMillisecond", // key
                "ms" // value
            }

        };
    }
}

package cz.pluto.tool;

import java.time.Duration;

public class Tool {
    
    
    public static String durationToString(Duration dur) {
        return String.format("%d:%02d:%02d", dur.toHours(), dur.minusHours(dur.toHours()).toMinutes(), dur.minusMinutes(dur.toMinutes()).getSeconds());
    }
    
    public static String durationToStringStart(Duration dur) {
        Duration cas = Duration.ofHours(10).plus(dur);
        return String.format("%02d:%02d", cas.toHours(), cas.minusHours(cas.toHours()).toMinutes());
    }

}

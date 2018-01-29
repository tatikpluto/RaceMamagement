package cz.pluto.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {
    /**
     * Startovaci cislo
     */
    int startNumber;
    /**
     * Vysledny cas
     */
    long time;
    /**
     * Vysledny cas
     */
    String string_time;
    
   
    @XmlElement
    public int getStartNumber() {
        return startNumber;
    }
    
    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
    }
    
    @XmlElement
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    
    @XmlElement
    public String getStringTime() {
        return string_time;
    }
    public void setStringTime(String time) {
        this.string_time = time;
    }
    
}

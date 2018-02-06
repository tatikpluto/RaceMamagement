package cz.pluto.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Kategorie zavodniku
 */
@XmlRootElement
public class Category {
    /**
     * poradi pro startovani
     */
    int sequenceNumber;
    /**
     * Jmeno kategorie
     */
    String name;
    /**
     * Pohlavi
     */
    Gender  gender=Gender.both;
    /**
     * Intervalové starty
     */
    boolean intervalovyStart = false;
    
    /**
     * Interval pro intervalove starty
     */
    Integer interval = null;
    
    /**
     * Rocnik narozeni od (null neni omezeni)
     */
    Integer minYear = null;
    /**
     * Rocnik narozeni do (null neni omezeni)
     */
    Integer maxYear = null;
    
    /**
     * Startovni cisla od (null neni omezeni)
     */
    Integer minNumber = null;
    /**
     * Startovni cisla do (null neni omezeni)
     */
    Integer maxNumber = null;
    /**
     * delka trate
     */
    String delka = null;
    
    /**
     * Odpoèet od vysledného èasu závodníka
     */
    long startTime = 0;
    
    @XmlElement
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    @XmlElement
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    @XmlElement
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    @XmlElement
    public String getDelka() {
        return delka;
    }
    
    public void setDelka(String delka) {
        this.delka = delka;
    }
    @XmlElement
    public Integer getMinYear() {
        return minYear;
    }
    
    public void setMinYear(Integer minYear) {
        this.minYear = minYear;
    }
    @XmlElement
    public Integer getMaxYear() {
        return maxYear;
    }
    
    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;
    }
    @XmlElement
    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    @XmlElement
    public Integer getMinNumber() {
        return minNumber;
    }
    
    public void setMinNumber(Integer minNumber) {
        this.minNumber = minNumber;
    }
    @XmlElement
    public Integer getMaxNumber() {
        return maxNumber;
    }
    
    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }
    @XmlElement
    public boolean isIntervalovyStart() {
        return intervalovyStart;
    }

    public void setIntervalovyStart(boolean intervalovyStart) {
        this.intervalovyStart = intervalovyStart;
    }
    @XmlElement
    public Integer getInterval() {
        return interval;
    }
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return name;
    }
}

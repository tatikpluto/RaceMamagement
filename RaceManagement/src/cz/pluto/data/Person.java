package cz.pluto.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import cz.pluto.tool.BooleanAdapter;

/*
 * Zavodnik
 */
@XmlRootElement
public class Person {
    /**
     * Id pro zavodnika a jeho startovaci cislo
     */
    Integer startNumber;
    /**
     * Jmeno zavodnika
     */
    String name;
    /**
     * Prijmeni zavodnika
     */
    String surname;
    /**
     * Rocnik zavodnika, udava prislustnost do kategorie
     */
    int year;
    /**
     * Pohlavi
     */
    boolean isWoman;
    /**
     * Klub
     */
    String club;
    /**
     * Jmeno kategorie
     */
    String categoryName;
    /**
     * Vysledny cas zavodnika
     */
    Long time;
    
    public Person() {
    }
    
    @XmlElement
    public Integer getStartNumber() {
        return startNumber;
    }
    
    public void setStartNumber(Integer startNumber) {
        this.startNumber = startNumber;
    }
    @XmlElement
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @XmlElement
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    @XmlElement
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    @XmlElement
    public boolean isWoman() {
        return isWoman;
    }
    public void setWoman(boolean woman) {
        this.isWoman = woman;
    }
    @XmlElement
    public String getClub() {
        return club;
    }
    public void setClub(String club) {
        this.club = club;
    }
    @XmlElement
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String catName) {
        this.categoryName = catName;
    }
    @XmlElement
    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    
    public String getLabel() {
        return surname+" "+name;
    }
    
}

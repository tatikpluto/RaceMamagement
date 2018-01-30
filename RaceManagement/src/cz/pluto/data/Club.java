package cz.pluto.data;

import javax.xml.bind.annotation.XmlElement;

/**
 * Sportovni klub
 */
public class Club {
    /**
     * Id klubu  1 - n
     */
    int clubId;
    /**
     * Jmeno klubu
     */
    String name;

    public int getId() {
        return clubId;
    }
    @XmlElement
    public void setId(int id) {
        this.clubId = id;
    }
    public String getName() {
        return name;
    }
    @XmlElement
    public void setName(String name) {
        this.name = name;
    }
}

package cz.pluto.data;

import javax.xml.bind.annotation.XmlElement;

/**
 * Sportovni klub
 */
public class Club {
    /**
     * Id klubu  1 - n
     */
    int club_id;
    /**
     * Jmeno klubu
     */
    String name;

    public int getId() {
        return club_id;
    }
    @XmlElement
    public void setId(int id) {
        this.club_id = id;
    }
    public String getName() {
        return name;
    }
    @XmlElement
    public void setName(String name) {
        this.name = name;
    }
}

package cz.pluto.data;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Race {

    String name;
    String place;
    String start;
    
    List<Category> categories;
    List<Person>   persons;
    List<Result>   results;
    
    public Race() {
        name = place = start = "";
        categories=new ArrayList<>();
        persons=new ArrayList<>();
        results=new ArrayList<>();

    }
   
    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @XmlElement
    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    @XmlElement
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    
    
    @XmlElement(name = "Category", type = Category.class, required = false)
    @XmlElementWrapper
    public List<Category> getCategories() {
        return categories;
    }
    
    @XmlElement(name = "Person", type = Person.class, required = false)
    @XmlElementWrapper
    public List<Person> getPersons() {
        return persons;
    }
    
    @XmlElement(name = "Result", type = Result.class, required = false)
    @XmlElementWrapper
    public List<Result> getResults() {
        return results;
    }
    
    
    public Person getPersonById(int id) {
        for (Person p : persons) {
            if (p.getPersonId()==id)
                return p;
        }
        return null;
    }
    
}

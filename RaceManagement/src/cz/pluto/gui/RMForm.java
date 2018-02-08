package cz.pluto.gui;

import java.awt.Dimension;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.pluto.data.Category;
import cz.pluto.data.Gender;
import cz.pluto.data.Person;
import cz.pluto.data.Race;
import cz.pluto.data.Result;

import javax.swing.JTabbedPane;

public class RMForm extends JInternalFrame {
    static int openFrameCount = 0;
    static final int XOFFSET = 30; 
    static final int YOFFSET = 30;
    
    private JLabel lName = new JLabel("Název závodu: ");
    private JLabel lPlace = new JLabel("Místo závodu: ");
    private JLabel lStart = new JLabel("Start závodu: ");
    private JTextField raceName;    
    private JTextField racePlace;
    private JTextField raceStart;
    private JTabbedPane tabs;
    
    protected CategoryForm categoryForm;
    protected PersonForm   personForm;
    protected ResultForm   resultForm;
    
    public transient Race race;
    
    public String filename = null;
    
    public RMForm(String title) {
        super(title, true, false, true, false);
        setSize(1700,900);
        
        race = new Race();
        
        getContentPane().setLayout(new FormLayout( "f:d:g", "p,f:d:g"));
        CellConstraints cc = new CellConstraints();
        
        JPanel top = new JPanel(new SpringLayout());
        top.setAlignmentY(11);
        raceName = new JTextField(20);
        top.add(lName);
        top.add(raceName);

        
        racePlace = new JTextField(20);
        top.add(lPlace);
        top.add(racePlace);
        
        raceStart = new JTextField(20);
        top.add(lStart);
        top.add(raceStart);
        SpringUtilities.makeCompactGrid(top,
                1, 6, //rows, cols
                6, 6, //initX, initY
                6, 6);//xPad, yPad
        
        getContentPane().add(top, cc.xy (1, 1));
        tabs = new JTabbedPane();
        tabs.setPreferredSize(new Dimension(1700, 850));
        getContentPane().add(tabs, cc.xy (1, 2));
        
        categoryForm = new CategoryForm(this);
        tabs.addTab("Kategorie", categoryForm);
        personForm = new PersonForm(this);
        tabs.addTab("Startující", personForm);
        resultForm = new ResultForm(this);
        tabs.addTab("Výsledky", resultForm);

    }
    
    public Category getCategory(int year, boolean isWoman) {
        for (Category cat : race.getCategories()) {
            if ((cat.getMinYear()!=null && cat.getMinYear()>year)
               ||
               (cat.getMaxYear()!=null && cat.getMaxYear()<year)
               ||
               (cat.getGender()!=Gender.both && isWoman && cat.getGender()==Gender.male)
               ||
               (cat.getGender()!=Gender.both && !isWoman && cat.getGender()==Gender.female)
               ||
               (cat.getMaxYear()==null && cat.getMinYear()>year)
               ||
               (cat.getMinYear()==null && cat.getMaxYear()<year))
                continue;
            
            return cat;
        }
        return null;
    }
    
    public Category getCategory(String categoryName) {
        if (categoryName!=null) {
            for (Category cat : race.getCategories()) {
                if (cat.getName().equals(categoryName))
                    return cat;
            }
        }
        return null;
    }
    
    public Person getPerson(int startNumber) {
        for (Person per : race.getPersons()) {            
            if (per.getStartNumber()!=null && per.getStartNumber()==startNumber)
                return per;
        }
        return null;
    }
    
    public Result getResult(int startNumber) {
        for (Result res : race.getResults()) {
            if (res.getStartNumber()==startNumber)
                return res;
        }
        return null;
    }
    
    public void saveToRace() {
        race.setName(raceName.getText());
        race.setPlace(racePlace.getText());
        race.setStart(raceStart.getText());
    }
    
    public void setFileName(String newFileName) {
        filename = newFileName;
        setTitle("Závod: "+filename);
    }
    
    public void load() {
        raceName.setText(race.getName());
        racePlace.setText(race.getPlace());
        raceStart.setText(race.getStart());
        
        categoryForm.loadTable();
        personForm.loadTable();
        personForm.reloadCategoriesInCombo();
        personForm.perStartNumber.setText(Integer.toString(PersonForm.maxNumber+1));
        
        for (Person per : race.getPersons()) {
            if (per.getStartNumber()!=null) {
                Result res = getResult(per.getStartNumber());
                if (res!=null) {
                    per.setTime(res.getTime());
                }
            }
        }
        
        resultForm.loadTable();
    }
    
    public void sortPersons() {
        Collator coll = Collator.getInstance(new Locale("cs", "CZ"));
        coll.setStrength(Collator.PRIMARY);
        race.getPersons().sort(Comparator.comparing(Person::getStartNumber, Comparator.nullsLast(Comparator.naturalOrder())));
    }

}

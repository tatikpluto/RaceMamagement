package cz.pluto.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.pluto.data.Category;
import cz.pluto.data.Person;

@SuppressWarnings("serial")
public class PersonEditForm extends JDialog {
    private RMForm masterForm;
    private Person person;
    
    private JTextField perName;
    private JTextField perSurname;
    private JTextField perClube;
    private JCheckBox isWoman;
    private JFormattedTextField perYear;
    JFormattedTextField perStartNumber;
    private JComboBox<Category> comboCategory;
    
    public PersonEditForm(RMForm rmForm, Person p) {
        super();
        this.masterForm = rmForm;
        this.person = p;        
        setLocationRelativeTo(null);
        init();
        pack(); 
        setVisible(true);
    }
    
    public void init() {
        JPanel panel = new JPanel(new FormLayout( "p, 2dlu, f:p:g", "p,p,p,p,p,p,p,p,f:d:g"));
        CellConstraints cc = new CellConstraints();
        
        panel.add(new JLabel("Jméno:"), cc.xy (1, 1)); 
        perName = new JTextField(30);
        panel.add(perName, cc.xyw(3, 1, 1));
        
        perName.setText(person.getName());
        
        panel.add(new JLabel("Pøíjmení:"), cc.xy (1, 2)); 
        perSurname= new JTextField(30);
        panel.add(perSurname, cc.xyw(3, 2, 1));
        
        perSurname.setText(person.getSurname());
        
        panel.add(new JLabel("Roèník:"), cc.xy (1, 3)); 
        perYear= new JFormattedTextField(NumberFormat.getIntegerInstance());
        perYear.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(perYear, cc.xyw(3, 3, 1));
        
        perYear.setValue(person.getYear());
        
        panel.add(new JLabel("Žena:"), cc.xy (1, 4));
        isWoman = new JCheckBox();
        panel.add(isWoman, cc.xyw(3, 4, 1));
        
        isWoman.setSelected(person.isWoman());
        
        panel.add(new JLabel("Klub:"), cc.xy (1, 5)); 
        perClube= new JTextField(30);
        panel.add(perClube, cc.xyw(3, 5, 1));
        
        perClube.setText(person.getClub());
        
        panel.add(new JLabel("Startovní èíslo:"), cc.xy (1, 6)); 
        perStartNumber= new JFormattedTextField(NumberFormat.getIntegerInstance());
        perStartNumber.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(perStartNumber, cc.xyw(3, 6, 1));
        
        if (person.getStartNumber()!=null) {
            perStartNumber.setValue(person.getStartNumber());
            perStartNumber.setEditable(false);
        }
        
        panel.add(new JLabel("Kategorie:"), cc.xy (1, 7)); 
        comboCategory= new JComboBox<>();
        comboCategory.setEditable(false);
        panel.add(comboCategory, cc.xyw(3, 7, 1));
        
        for (Category cat : masterForm.race.getCategories()) {
            comboCategory.addItem(cat);
        }
        comboCategory.setSelectedItem(masterForm.getCategory(person.getCategoryName()));
        
        JPanel btns = new JPanel(new FormLayout( "f:d:g, p, p", "p"));
        CellConstraints ccBtns = new CellConstraints();
        
        JButton okButton = new JButton("OK");
        btns.add(okButton, ccBtns.xyw(2, 1, 1));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (update())
                    dispose();
            }
        });
        
        JButton cButton = new JButton("Cancel");
        btns.add(cButton, ccBtns.xyw(3, 1, 1));
        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(btns, cc.xyw(1, 8, 3));
        setLayout(new FormLayout("f:d:g", "p"));
        CellConstraints mainCC = new CellConstraints();
        add(panel, mainCC.xy(1, 1));
    }
    
    
    private boolean update() {
        boolean change = false;
        Integer stNumber = null;
        if (perStartNumber.getValue()!=null && person.getStartNumber()==null) {
            stNumber =((Number)perStartNumber.getValue()).intValue();
            Person check = masterForm.getPerson(stNumber);
            if (check!=null) {
                JOptionPane.showMessageDialog(this, "Startovní èíslo "+stNumber+" je již pøiøazeno !");
                return false;
            }
            
            person.setStartNumber(stNumber);
            change = true;
        }
        if (perName.getText()!=null && !perName.getText().isEmpty()) {
            if (!person.getName().equals(perName.getText())){
                person.setName(perName.getText());
                change = true;
            }
        }
        if (perSurname.getText()!=null && !perSurname.getText().isEmpty()) {
            if (!person.getSurname().equals(perSurname.getText())){
                person.setSurname(perSurname.getText());
                change = true;
            }
        }
        if ( person.getClub()==null || !person.getClub().equals(perClube.getText())) {
            person.setClub(perClube.getText());
            change = true;
        }
        
        if ( !person.getCategoryName().equals(comboCategory.getSelectedItem().toString())) {
            person.setCategoryName((String)comboCategory.getSelectedItem().toString());
            change = true;
        }
        if (isWoman.isSelected()!=person.isWoman()) {
            person.setWoman(new Boolean(isWoman.isSelected()));
            change = true;
        }
        
        Integer year =((Number)perYear.getValue()).intValue();
        if (year!=null && person.getYear()!=year.intValue()) {
            person.setYear(year);
            change = true;
        }
        if (change)
            masterForm.personForm.reloadTable();
        return true;
    }
    
}

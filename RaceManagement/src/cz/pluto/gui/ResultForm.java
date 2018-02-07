package cz.pluto.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.pluto.data.Category;
import cz.pluto.data.Person;
import cz.pluto.data.Result;
import cz.pluto.tool.Tool;

public class ResultForm extends JPanel {
    
    private JFormattedTextField resNumber;
    private JFormattedTextField resTime;
    
    private JTable table;
    private DefaultTableModel tableModel;
    
    private RMForm rmForm;
    
    public ResultForm(RMForm master) {
        super(new FormLayout( "f:d:g, p", "f:d:g"));
        rmForm = master;
        CellConstraints cc = new CellConstraints();
        add(createTable(), cc.xy (1, 1)); 
        add(createRightPanel(), cc.xy (2, 1));
    }
    
    public JScrollPane createTable() {
        createTableModel();
        table = new JTable(tableModel);
        updateColumns();
        createPopupMenu();
        loadTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        return scrollPane;
    }
    
    private void createTableModel() {
        tableModel = new DefaultTableModel(0, 4) {
            @Override
            public boolean isCellEditable(int row, int column){  
                return false;  
            }
        };
    }
    
    private void updateColumns() {
        TableColumnModel columnModel = table.getColumnModel();
        
        TableColumn tc = columnModel.getColumn(0);
        tc.setIdentifier("startNumber");
        tc.setHeaderValue("Startoví èíslo");
        
        tc = columnModel.getColumn(1);
        tc.setIdentifier("name");
        tc.setHeaderValue("Pøíjmení a jméno");
        
        tc = columnModel.getColumn(2);
        tc.setIdentifier("kategorie");
        tc.setHeaderValue("Kateborie");
        
        tc = columnModel.getColumn(3);
        tc.setIdentifier("time");
        tc.setHeaderValue("Výsledný èas");
    }
    
    private void createPopupMenu() {
        JPopupMenu tableMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Smazat výsledek");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = table.getSelectedRow();
                if (selRow!=-1 && selRow<rmForm.race.getResults().size()) {
                    Result res = rmForm.race.getResults().get(selRow);
                    Person per = rmForm.getPerson(res.getStartNumber());
                    per.setTime(null);
                    rmForm.race.getResults().remove(selRow);
                    reloadTable();
                    rmForm.personForm.reloadTable();
                }
            }
        });
        tableMenu.add(deleteItem);
        table.setComponentPopupMenu(tableMenu);
    }
    
    public JPanel createRightPanel() {
        JPanel panel = new JPanel(new FormLayout( "p, 2dlu, 50dlu", "p,p,p,f:d:g"));
        CellConstraints cc = new CellConstraints();        
        
        panel.add(new JLabel("Startovní èíslo:"), cc.xy (1, 1)); 
        resNumber= new JFormattedTextField(NumberFormat.getIntegerInstance());
        resNumber.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(resNumber, cc.xyw(3, 1, 1));
        
        panel.add(new JLabel("Výsledný èas:"), cc.xy (1, 2)); 
        resTime= new JFormattedTextField(DurationFormat.getHoursInstance());
        resTime.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(resTime, cc.xyw(3, 2, 1));
        
        JButton addButton = new JButton("Uložit");
        panel.add(addButton, cc.xyw(1, 3, 3));
        
        addButton.addActionListener(e -> createResult());

        return panel;
    }
    
    private void createResult() {
        if (resNumber.getValue()==null || resTime.getValue()==null) {
            JOptionPane.showMessageDialog(rmForm, "Nevyplanìné hodnoty !");
            return;
        }
            
        
        Result res = new Result();
        res.setStartNumber(((Number)resNumber.getValue()).intValue());
        Person per = rmForm.getPerson(res.getStartNumber());
        if (per==null) {
            JOptionPane.showMessageDialog(rmForm, "Neexistující startovní èíslo: "+res.getStartNumber());
            return;
        }
        res.setTime((Long)resTime.getValue());
        res.setStringTime(Tool.durationToString(Duration.ofMillis(res.getTime())));
        
        if (rmForm.getResult(res.getStartNumber())==null) {
            addRow(res, true);
            Category cat = rmForm.getCategory(per.getCategoryName());
            per.setTime(res.getTime()-cat.getStartTime());
            if (cat.isIntervalovyStart() && cat.getInterval()!=null) {
                Long time = per.getTime();
                Duration dur = Duration.ofSeconds(cat.getInterval().intValue());
                
                //nastaveni vysledneho casu podle intervalu
                List<Person> persons = new ArrayList<>();
                for (Person p : rmForm.race.getPersons()) {
                    if (p.getCategoryName().equals(cat.getName()))
                        persons.add(p);
                }
                Collections.sort(persons, new Comparator<Person>() {
                    public int compare(Person p1, Person p2) {
                        return p1.getStartNumber().compareTo(p1.getStartNumber());
                    }
                });
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getPersonId()==per.getPersonId()) {
                        per.setTime(time - (dur.multipliedBy(i)).toMillis());
                        break;
                    }
                }
            }
        }else {
            JOptionPane.showMessageDialog(rmForm, "Výsledný èas pro startovní èíslo "+res.getStartNumber()+" je již zadán.");
        }
        
        resNumber.setValue(null);
        resTime.setValue(null);
    }
    
    private boolean addRow(Result res, boolean isNew) {
        Person person = rmForm.getPerson(res.getStartNumber());
        if (person != null) {

            if (isNew) {
                rmForm.race.getResults().add(res);
            }
            Vector<Object> rowData = new Vector<>(3);

            rowData.add(0, res.getStartNumber());
            rowData.add(1, person.getLabel());
            rowData.add(2, person.getCategoryName());
            rowData.add(3, Tool.durationToString(Duration.ofMillis(res.getTime())));
            tableModel.addRow(rowData);
            return true;
        }
        return false;
    }
    
    protected void loadTable() {
        for (Result res : rmForm.race.getResults()) {
            addRow(res, false);
        }
    }
    
    public void reloadTable() {
        createTableModel();
        table.setModel(tableModel);
        updateColumns();
        loadTable();
    }

}

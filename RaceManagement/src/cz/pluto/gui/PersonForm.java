package cz.pluto.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.pluto.data.Category;
import cz.pluto.data.Person;


@SuppressWarnings("serial")
public class PersonForm extends JPanel {
    
    static int maxNumber = 0;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private TableColumnModel columnModel;
    private JPopupMenu tableMenu;
    
    private JTextField perName;
    private JTextField perSurname;
    private JTextField perClube;
    private JCheckBox isWoman;
    private JFormattedTextField perYear;
    JFormattedTextField perStartNumber;
    private JComboBox<Category> comboCategory;
    
    private RMForm rmForm;
    
    public PersonForm(RMForm master) {
        super(new FormLayout( "f:d:g, p", "f:d:g"));
        rmForm = master;
        CellConstraints cc = new CellConstraints();
        add(createTable(), cc.xy (1, 1)); 
        add(createRightPanel(), cc.xy (2, 1));
        
    }
    
    public JScrollPane createTable() {
        createTableModel();
        table = new JTable(tableModel);
        createPopupMenu();
        updateColumns();
        loadTable();
        table.setSelectionBackground(Color.GREEN.brighter());
        return new JScrollPane(table);
    }
    
    private void createTableModel() {
        tableModel = new DefaultTableModel(0, 5) {
            @Override
            public boolean isCellEditable(int row, int column){  
                return false;  
            }
        };
    }
    
    private void updateColumns() {
        columnModel = table.getColumnModel();
        TableColumn tc = columnModel.getColumn(0);
        tc.setIdentifier("startNumber");
        tc.setHeaderValue("Startoví èíslo");
        
        tc = columnModel.getColumn(1);
        tc.setIdentifier("name");
        tc.setHeaderValue("Pøíjmení a jméno");
        
        tc = columnModel.getColumn(2);
        tc.setIdentifier("year");
        tc.setHeaderValue("Roèník");
        
        tc = columnModel.getColumn(3);
        tc.setIdentifier("club");
        tc.setHeaderValue("Klub");
        
        tc = columnModel.getColumn(4);
        tc.setIdentifier("category");
        tc.setHeaderValue("Kategorie");
    }
    
    private void createPopupMenu() {
        tableMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Smazat závodníka");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = table.getSelectedRow();
                if (selRow!=-1 && selRow<rmForm.race.getPersons().size()) {
                    rmForm.race.getPersons().remove(selRow);
                    reloadTable();
                }
            }
        });
        tableMenu.add(deleteItem);
        
        JMenuItem edit = new JMenuItem("Editovat závodníka");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = table.getSelectedRow();
                if (selRow!=-1 && selRow<rmForm.race.getPersons().size()) {
                    PersonEditForm frame = new PersonEditForm(rmForm, rmForm.race.getPersons().get(selRow));
                }
            }
        });
        tableMenu.add(edit);
        
        JMenuItem deleteSt = new JMenuItem("Smazat starovní èíslo");
        deleteSt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = table.getSelectedRow();
                if (selRow!=-1 && selRow<rmForm.race.getPersons().size()) {
                    int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete startovní èíslo  "+rmForm.race.getPersons().get(selRow).getLabel()+"!","Pozor", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION){
                    rmForm.race.getPersons().get(selRow).setStartNumber(null);;
                    reloadTable();
                    }
                }
            }
        });
        tableMenu.add(deleteSt);
        
        table.setComponentPopupMenu(tableMenu);
        //tableMenu.addPopupMenuListener(new MyPopupMenuListener(table, tableMenu));
    }
    
    public JPanel createRightPanel() {
        JPanel panel = new JPanel(new FormLayout( "p, 2dlu, p", "p,p,p,p,p,p,p,p,f:d:g"));
        CellConstraints cc = new CellConstraints();
        
        panel.add(new JLabel("Jméno:"), cc.xy (1, 1)); 
        perName = new JTextField(30);
        panel.add(perName, cc.xyw(3, 1, 1)); 
        
        panel.add(new JLabel("Pøíjmení:"), cc.xy (1, 2)); 
        perSurname= new JTextField(30);
        panel.add(perSurname, cc.xyw(3, 2, 1));
        
        panel.add(new JLabel("Roèník:"), cc.xy (1, 3)); 
        perYear= new JFormattedTextField(NumberFormat.getIntegerInstance());
        perYear.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(perYear, cc.xyw(3, 3, 1));
        
        panel.add(new JLabel("Žena:"), cc.xy (1, 4));
        isWoman = new JCheckBox();
        panel.add(isWoman, cc.xyw(3, 4, 1));
        
        panel.add(new JLabel("Klub:"), cc.xy (1, 5)); 
        perClube= new JTextField(30);
        panel.add(perClube, cc.xyw(3, 5, 1));
        
        panel.add(new JLabel("Startovní èíslo:"), cc.xy (1, 6)); 
        perStartNumber= new JFormattedTextField(NumberFormat.getIntegerInstance());
        perStartNumber.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(perStartNumber, cc.xyw(3, 6, 1));
        
        panel.add(new JLabel("Kategorie:"), cc.xy (1, 7)); 
        comboCategory= new JComboBox<>();
        comboCategory.setEditable(false);
        panel.add(comboCategory, cc.xyw(3, 7, 1));
        
        JButton addButton = new JButton("Vytvoøit závodníka");
        panel.add(addButton, cc.xyw(1, 8, 3));
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPerson();
            }
        });
        
        
        
        return panel;
    }
    
    private void createPerson() {
        if (perName.getText()==null || perName.getText().isEmpty() || perSurname.getText()==null || perSurname.getText().isEmpty() 
            || perYear.getValue()==null) {
            JOptionPane.showMessageDialog(rmForm, "Nevyplanìná potøebná pole (Jméno, Pøíjmení, Roèník)!");
            return;
        }
        
        Integer stNumber = null;
        if (perStartNumber.getValue()!=null) {
            stNumber =((Number)perStartNumber.getValue()).intValue();
            Person check = rmForm.getPerson(stNumber);
            if (check!=null) {
                JOptionPane.showMessageDialog(rmForm, "Startovní èíslo "+stNumber+" je již pøiøazeno !");
                return;
            }
        }
            
        Person newPer = new Person();
        newPer.setName(perName.getText());
        newPer.setSurname(perSurname.getText());
        newPer.setYear(((Long)perYear.getValue()).intValue());
        newPer.setClub(perClube.getText());        
        newPer.setWoman(new Boolean(isWoman.isSelected()));
        newPer.setStartNumber(stNumber);
        
        Category cat = (Category)comboCategory.getSelectedItem();
        if (cat==null)
            cat = rmForm.getCategory(newPer.getYear(), newPer.isWoman());
        if (cat!=null)
            newPer.setCategoryName(cat.getName());
        
        rmForm.race.getPersons().add(newPer);
        reloadTable();
        updateEdit(newPer);
    }
    
    private boolean addRow(Person newPer, int rowId) {
        Vector<Object> rowData = new Vector<>(5);
        
        rowData.add(0, newPer.getStartNumber());
        rowData.add(1, newPer.getLabel());
        rowData.add(2, newPer.getYear());
        rowData.add(3, newPer.getClub());
        rowData.add(4, newPer.getCategoryName());
        tableModel.addRow(rowData);
        return true;
    }
    
    protected void loadTable() {
        rmForm.sortPersons();
        int rowId = 0;
        for (Person per : rmForm.race.getPersons()) {
            addRow(per, rowId);
            rowId++;
            if (per.getStartNumber()!=null)
                maxNumber = Math.max(maxNumber, per.getStartNumber());
        }
    }
    
    public void reloadTable() {
        createTableModel();
        table.setModel(tableModel);
        updateColumns();
        loadTable();
    }
    
    
    private void updateEdit(Person newPer) {
        if (maxNumber==0 && perStartNumber.getValue()!=null)
            maxNumber=(((Number)perStartNumber.getValue()).intValue());
        
        maxNumber = Math.max(maxNumber, newPer.getStartNumber());
        perStartNumber.setValue(maxNumber+1);
        perName.setText(null);
        perSurname.setText(null);
        perYear.setValue(null);
        perClube.setText(null);
        isWoman.setSelected(false);
        comboCategory.setSelectedItem(null);
    }
    
    
    public void reloadCategoriesInCombo() {
        comboCategory.removeAllItems();
        comboCategory.addItem(null);
        for (Category cat : rmForm.race.getCategories()) {
            comboCategory.addItem(cat);
        }
    }

}

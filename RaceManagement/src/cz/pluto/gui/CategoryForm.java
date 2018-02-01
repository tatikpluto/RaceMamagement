package cz.pluto.gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
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
import cz.pluto.data.Gender;
import cz.pluto.gui.listeners.MyPopupMenuListener;
import cz.pluto.tool.Tool;

@SuppressWarnings("serial")
public class CategoryForm extends JPanel {
    
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JTextField catName;
    private JTextField catDelka;
    private JFormattedTextField catStartTime;
    private JComboBox<Gender> catGender;
    private JFormattedTextField catMinYear;
    private JFormattedTextField catMaxYear;
    private JFormattedTextField catMinNumber;
    private JFormattedTextField catMaxNumber;
    
    private RMForm rmForm;
    
    public CategoryForm(RMForm master) {
        super(new FormLayout( "f:d:g, p", "f:d:g"));
        rmForm = master;
        CellConstraints cc = new CellConstraints();
        add(createTable(), cc.xy (1, 1)); 
        add(createRightPanel(), cc.xy (2, 1));
    }
    
    
    public JScrollPane createTable() {
        table = new JTable();
        reloadTable();
        createPopupMenu();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        return scrollPane;
    }
    
    private void createTableModel() {
        tableModel = new DefaultTableModel(0, 7) {
            @Override
            public boolean isCellEditable(int row, int column){  
                return false;  
            }
        };
    }
    
    private void updateColumns() {
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn tc = columnModel.getColumn(0);
        tc.setIdentifier("name");
        tc.setHeaderValue("N�zev kategorie");
        
        tc = columnModel.getColumn(1);
        tc.setIdentifier("minYear");
        tc.setHeaderValue("Od");
        
        tc = columnModel.getColumn(2);
        tc.setIdentifier("maxYear");
        tc.setHeaderValue("Do");
        
        tc = columnModel.getColumn(3);
        tc.setIdentifier("gender");
        tc.setHeaderValue("Pohlav�");
        
        tc = columnModel.getColumn(4);
        tc.setIdentifier("delka");
        tc.setHeaderValue("D�lka");
        
        tc = columnModel.getColumn(5);
        tc.setIdentifier("startTime");
        tc.setHeaderValue("Startovn� �as");
        
        tc = columnModel.getColumn(6);
        tc.setIdentifier("numbers");
        tc.setHeaderValue("Startov� ��sla");
    }
    
    private void createPopupMenu() {
        JPopupMenu tableMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Smazat kategorii");
        deleteItem.addActionListener(e ->
            {
                int selRow = table.getSelectedRow();
                if (selRow!=-1 && selRow<rmForm.race.getCategories().size()) {
                    rmForm.race.getCategories().remove(selRow);
                    reloadTable();
                }
            }
        );
        tableMenu.add(deleteItem);
        
        JMenuItem edit = new JMenuItem("Editovat kategorii");
        edit.addActionListener(e->
            {
                int selRow = table.getSelectedRow();
                if (selRow!=-1 && selRow<rmForm.race.getCategories().size()) {
                    new CategoryEditForm(rmForm, rmForm.race.getCategories().get(selRow));
                }
            }
        );
        tableMenu.add(edit);
        
       
        table.setComponentPopupMenu(tableMenu);
        tableMenu.addPopupMenuListener(new MyPopupMenuListener(table, tableMenu));
    }
    
    public JPanel createRightPanel() {
        JPanel panel = new JPanel(new FormLayout( "p, 2dlu, p", "p,p,p,p,p,p,p,p,p,f:d:g"));
        CellConstraints cc = new CellConstraints();
        
        panel.add(new JLabel("N�zev:"), cc.xy (1, 1)); 
        catName = new JTextField(30);
        panel.add(catName, cc.xyw(3, 1, 1)); 
        
        DecimalFormat yearFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        yearFormat.setMinimumIntegerDigits(0);
        yearFormat.setMaximumIntegerDigits(4);
        
        panel.add(new JLabel("Od:"), cc.xy (1, 2)); 
        catMinYear= new JFormattedTextField(yearFormat);
        catMinYear.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(catMinYear, cc.xyw(3, 2, 1));
        
        panel.add(new JLabel("Do:"), cc.xy (1, 3)); 
        catMaxYear= new JFormattedTextField(yearFormat);
        catMaxYear.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(catMaxYear, cc.xyw(3, 3, 1));
        
        panel.add(new JLabel("Pohlav�:"), cc.xy (1, 4)); 
        catGender= new JComboBox<>();
        catGender.addItem(Gender.both);
        catGender.addItem(Gender.male);
        catGender.addItem(Gender.female);
        panel.add(catGender, cc.xyw(3, 4, 1));
        
        DecimalFormat numberFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(1);
        numberFormat.setMaximumIntegerDigits(4);
        
        panel.add(new JLabel("��sla od:"), cc.xy (1, 5)); 
        catMinNumber= new JFormattedTextField(numberFormat);
        catMinNumber.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(catMinNumber, cc.xyw(3, 5, 1));
        
        panel.add(new JLabel("��sla do:"), cc.xy (1, 6)); 
        catMaxNumber= new JFormattedTextField(numberFormat);
        catMaxNumber.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(catMaxNumber, cc.xyw(3, 6, 1));
        
        panel.add(new JLabel("D�lka:"), cc.xy (1, 7)); 
        catDelka= new JTextField(30);
        catDelka.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(catDelka, cc.xyw(3, 7, 1));
        
        panel.add(new JLabel("Startovn� �as:"), cc.xy (1, 8)); 
        catStartTime= new JFormattedTextField(DurationFormat.getHoursInstance());
        catStartTime.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(catStartTime, cc.xyw(3, 8, 1));
        
        JButton addButton = new JButton("Vytvo�it kategorii");
        panel.add(addButton, cc.xyw(1, 9, 3));
        addButton.addActionListener(e-> createCategory());
        
        return panel;
    }
    
    private void createCategory() {
        if (catName.getText()==null)
            return;
            
        Category newCat = new Category();
        newCat.setName(catName.getText());
        if (catMinYear.getValue()!=null)
            newCat.setMinYear(((Long)catMinYear.getValue()).intValue());
        
        if (catMaxYear.getValue()!=null)
            newCat.setMaxYear(((Long)catMaxYear.getValue()).intValue());
        
        newCat.setGender((Gender)catGender.getSelectedItem());
        
        if (catMinNumber.getValue()!=null)
            newCat.setMinNumber(((Long)catMinNumber.getValue()).intValue());
        if (catMaxNumber.getValue()!=null)
            newCat.setMaxNumber(((Long)catMaxNumber.getValue()).intValue());
        
        rmForm.race.getCategories().add(newCat);
        addRow(newCat);
        
        catName.setText(null);
        catDelka.setText(null);
        catMinYear.setValue(null);
        catMaxYear.setValue(null);
        catMinNumber.setValue(null);
        catMaxNumber.setValue(null);
        catStartTime.setValue(null);
        rmForm.personForm.reloadCategoriesInCombo();
    }
    
    private void addRow(Category newCat) {
        Vector<Object> rowData = new Vector<>(5);
        
        rowData.add(0, newCat.getName());
        rowData.add(1, newCat.getMinYear());
        rowData.add(2, newCat.getMaxYear());
        rowData.add(3, newCat.getGender());
        rowData.add(4, newCat.getDelka());
        rowData.add(5, Tool.durationToString(Duration.ofMillis(newCat.getStartTime())));
        String numbers="";
        if (newCat.getMinNumber()!=null)
            numbers += newCat.getMinNumber()+" - ";
        if (newCat.getMaxNumber()!=null)
            numbers += (numbers.isEmpty()?" - ":"")+newCat.getMaxNumber();
        rowData.add(6, numbers);
        tableModel.addRow(rowData);
        
    }
    
    protected void loadTable() {
        for (Category category : rmForm.race.getCategories()) {
            addRow(category);
        }
    }
    
    public void reloadTable() {
        createTableModel();
        table.setModel(tableModel);
        updateColumns();
        loadTable();
    }

}

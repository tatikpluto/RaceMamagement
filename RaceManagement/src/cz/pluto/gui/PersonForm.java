package cz.pluto.gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.pluto.data.Category;
import cz.pluto.data.Person;


public class PersonForm extends JPanel {
    
    static int maxNumber = 0;
    
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JTextField perName;
    private JTextField perSurname;
    private JTextField perClube;
    private JCheckBox isWoman;
    private JFormattedTextField perYear;
    JTextField perStartNumber;
    private JComboBox<Category> comboCategory;
    
    private RMForm rmForm;
    
    private final int IDCOLUMN = 5;
    
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
        loadTable(null);
        table.setSelectionForeground(Color.GREEN.brighter());
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                if (e.getClickCount() == 2) {
                    List<RowSorter.SortKey> sortKeys = new ArrayList<>(1);
                    TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();
                    if ( sorter != null) {
                        List<RowSorter.SortKey> oldSortKeys = (List<SortKey>) sorter.getSortKeys();
                        if (oldSortKeys.get(0).getColumn()==col) {
                            if (oldSortKeys.get(0).getSortOrder()==SortOrder.ASCENDING) 
                                sortKeys.add(new RowSorter.SortKey(col, SortOrder.DESCENDING)); 
                            else
                                sortKeys.add(new RowSorter.SortKey(col, SortOrder.ASCENDING));
                        }else {
                            sortKeys.add(new RowSorter.SortKey(col, SortOrder.ASCENDING));
                        }
                    }else {
                        sorter = new TableRowSorter<>(table.getModel());
                        sortKeys.add(new RowSorter.SortKey(col, SortOrder.ASCENDING));
                    }
                    sorter.setSortKeys(sortKeys);
                    table.setRowSorter(sorter);
                }
            }
        });
        
        return new JScrollPane(table);
    }
    
    private void createTableModel() {
        tableModel = new DefaultTableModel(0, 6) {
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
        tc.setIdentifier("year");
        tc.setHeaderValue("Roèník");
        
        tc = columnModel.getColumn(3);
        tc.setIdentifier("club");
        tc.setHeaderValue("Klub");
        
        tc = columnModel.getColumn(4);
        tc.setIdentifier("category");
        tc.setHeaderValue("Kategorie");
        
        tc = columnModel.getColumn(IDCOLUMN);
        tc.setIdentifier("id");
        tc.setHeaderValue("ID");
    }
    
    private void createPopupMenu() {
        JPopupMenu tableMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Smazat závodníka");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = table.getSelectedRow();
                if (selRow!=-1) {
                    Object obj = table.getValueAt(selRow, IDCOLUMN);
                    if (obj!=null && obj instanceof Integer) {
                        Person p = rmForm.race.getPersonById((Integer)obj);
                        if (p!=null) {
                            rmForm.race.getPersons().remove(p);
                            reloadTable(null);
                        }
                    }
                }
            }
        });
        tableMenu.add(deleteItem);
        
        JMenuItem edit = new JMenuItem("Editovat závodníka");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = table.getSelectedRow();
                if (selRow!=-1) {
                    Object obj = table.getValueAt(selRow, IDCOLUMN);
                    if (obj!=null && obj instanceof Integer) {
                        Person p = rmForm.race.getPersonById((Integer)obj);
                        if (p!=null) {
                            new PersonEditForm(rmForm, p);
                        }
                    }
                }
            }
        });
        tableMenu.add(edit);
        
        JMenuItem deleteSt = new JMenuItem("Smazat starovní èíslo");
        deleteSt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = table.getSelectedRow();
                if (selRow!=-1) {
                    Object obj = table.getValueAt(selRow, IDCOLUMN);
                    if (obj!=null && obj instanceof Integer) {
                        Person p = rmForm.race.getPersonById((Integer)obj);
                        if (p!=null) {
                            p.setStartNumber(null);
                            reloadTable(null);
                        }
                    }
                }
            }
        });
        tableMenu.add(deleteSt);
        
        JMenuItem copyValue = new JMenuItem("Zkopírovat hodnotu");
        copyValue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = table.getSelectedRow();
                int selCol = table.getSelectedColumn();
                if (selRow!=-1) {
                    Object obj = table.getValueAt(selRow, selCol);
                    if (obj!=null) {
                    	StringSelection select = new StringSelection(obj.toString());
                        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipBoard.setContents(select, null);
                    }
                }
            }
        });
        tableMenu.add(copyValue);
        
        table.setComponentPopupMenu(tableMenu);
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
        
        DecimalFormat stCislaFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        stCislaFormat.setMinimumIntegerDigits(0);
        stCislaFormat.setMaximumIntegerDigits(3);
        
        panel.add(new JLabel("Startovní èíslo:"), cc.xy (1, 6)); 
        perStartNumber= new JTextField(30);
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
        if (perStartNumber.getText()!=null) {
        	String text = perStartNumber.getText();
        	try {
            stNumber = Integer.parseInt(text);
        	} catch(NumberFormatException nfe) {
        		
        	}
			if (stNumber != null) {
				Person check = rmForm.getPerson(stNumber);
				if (check != null) {
					JOptionPane.showMessageDialog(rmForm,
							"Startovní èíslo " + stNumber + " je již pøiøazeno: " + check.getLabel());
					return;
				}
        	}
        }
            
        Person newPer = new Person();
        newPer.setName(perName.getText());
        newPer.setSurname(perSurname.getText());
        newPer.setYear(((Long)perYear.getValue()).intValue());
        newPer.setClub(perClube.getText());        
        newPer.setWoman(isWoman.isSelected());
        newPer.setStartNumber(stNumber);
        
        Category cat = (Category)comboCategory.getSelectedItem();
        if (cat==null)
            cat = rmForm.getCategory(newPer.getYear(), newPer.isWoman());
        if (cat!=null)
            newPer.setCategoryName(cat.getName());
        
        rmForm.race.getPersons().add(newPer);
        reloadTable(newPer);
        updateEdit(newPer);
    }
    
    private boolean addRow(Person newPer) {
        Vector<Object> rowData = new Vector<>(5);
        
        rowData.add(0, newPer.getStartNumber());
        rowData.add(1, newPer.getLabel());
        rowData.add(2, newPer.getYear());
        rowData.add(3, newPer.getClub());
        rowData.add(4, newPer.getCategoryName());
        rowData.add(IDCOLUMN, newPer.getPersonId());
        tableModel.addRow(rowData);
        return true;
    }
    
    protected void loadTable(Person lastPer) {
        tableModel.setRowCount(0);
        rmForm.sortPersons();

        for (Person per : rmForm.race.getPersons()) {
            addRow(per);
            if (per.getStartNumber()!=null)
                maxNumber = Math.max(maxNumber, per.getStartNumber());
        }
    }
    
    public void reloadTable(Person lastPer) {
        /*createTableModel();
        updateColumns();
        table.setModel(tableModel);*/
        loadTable(lastPer);
        rmForm.updateTitle();
    }
    
    
    private void updateEdit(Person newPer) {
        if (maxNumber==0 && newPer.getStartNumber()!=null)
            maxNumber=newPer.getStartNumber().intValue();
        if (newPer.getStartNumber()!=null)
        	maxNumber = Math.max(maxNumber, newPer.getStartNumber());
        perStartNumber.setText(Integer.toString(maxNumber+1));
        perName.setText(null);
        perSurname.setText(null);
        perYear.setValue(null);
        perClube.setText(null);
        isWoman.setSelected(false);
        comboCategory.setSelectedItem(null);
        perName.requestFocusInWindow();
    }
    
    
    public void reloadCategoriesInCombo() {
        comboCategory.removeAllItems();
        comboCategory.addItem(null);
        for (Category cat : rmForm.race.getCategories()) {
            comboCategory.addItem(cat);
        }
    }

}

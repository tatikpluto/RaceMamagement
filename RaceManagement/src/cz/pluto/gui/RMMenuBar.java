package cz.pluto.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import cz.pluto.data.Category;
import cz.pluto.data.Gender;
import cz.pluto.data.Person;
import cz.pluto.tool.PdfTool;
import cz.pluto.tool.XmlTool;

@SuppressWarnings("serial")
public class RMMenuBar extends JMenuBar {

    public RMMenuBar() {
        super();
        //Set up the lone menu.
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        add(menu);

        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newRMForm();
            }
        });
        menu.add(menuItem);
        
        //Set up the open menu item.
        menuItem = new JMenuItem("Open");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load();
            }
        });
        menu.add(menuItem);
        
        //Set up the save menu item.
        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        menu.add(menuItem);

        //Set up the quit menu item.
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("quit");
        menu.add(menuItem);
        
        JMenu printMenu = new JMenu("Export...");
        printMenu.setMnemonic(KeyEvent.VK_P);
        add(printMenu);
        
        
        menuItem = new JMenuItem("Ulo�it P�ihl�en� do PDF");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RM.desktop.getSelectedFrame()!=null) {
                    try {
                        PdfTool.createPersonList(((RMForm)RM.desktop.getSelectedFrame()).race, ((RMForm)RM.desktop.getSelectedFrame()).filename);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        printMenu.add(menuItem);
        //Set up the first menu item.
        menuItem = new JMenuItem("Ulo�it Startovku do PDF");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RM.desktop.getSelectedFrame()!=null) {
                    try {
                        PdfTool.createRaceList(((RMForm)RM.desktop.getSelectedFrame()).race, ((RMForm)RM.desktop.getSelectedFrame()).filename);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        printMenu.add(menuItem);
        //Set up the first menu item.
        menuItem = new JMenuItem("Ulo�it V�sledky do PDF");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RM.desktop.getSelectedFrame()!=null) {
                    try {
                        PdfTool.createRaceResult(((RMForm)RM.desktop.getSelectedFrame()).race, ((RMForm)RM.desktop.getSelectedFrame()).filename);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        printMenu.add(menuItem);
        
        
        JMenu actionMenu = new JMenu("Akce");
        add(actionMenu);
        
        //Set up the open menu item.
        menuItem = new JMenuItem("Sazat v�echny v�sledky");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete vymazat v�echny v�sledky?","Pozor", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    eraseResults();
                }
            }
        });
        actionMenu.add(menuItem);
        
        //Set up the open menu item.
        menuItem = new JMenuItem("Rozd�lit kategori podle pohlav�");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete rozd�lit Benjam�nky?","Pozor", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    splitCategory("Benjam�nci");
                }
            }
        });
        actionMenu.add(menuItem);
        
        //Set up the open menu item.
        menuItem = new JMenuItem("Slou�it Benjam�nky do jedn�");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete slou�it Benjam�nky do jedn� kategorie?","Pozor", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    mergeCategory("Benjam�nci");
                }
            }
        });
        actionMenu.add(menuItem);
        
    }
    
    private RMForm newRMForm() {
        RMForm frame = new RMForm(null);
        frame.setVisible(true); //necessary as of 1.3
        RM.addForm(frame);
        try {
            frame.setSelected(true);
            frame.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {}//nejaka chyba
        return frame;
    }
    
    public void load() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("xml file", "xml"));
        fileChooser.setDialogTitle("Specify a file to load");
        int userSelection = fileChooser.showOpenDialog(RM.desktop);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            RMForm form = newRMForm();
            form.setFileName(f.getAbsolutePath());
            XmlTool.importFromXML(form, f);
            form.load();
        }
    }
    
    public void save() {
        RMForm frame =  (RMForm)RM.desktop.getSelectedFrame();
        if (frame == null)
            return;
        frame.saveToRace();
        if (frame.filename!=null) {
            XmlTool.exportToXml(frame, new File(frame.filename));
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("xml file", "xml"));
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().toString();
            if (!filename .endsWith(".xml"))
                 filename += ".xml";
            XmlTool.exportToXml(frame, new File(filename));
            frame.setFileName(filename);
            System.out.println("Save as file: " + filename);
        }
    }
    
    public void eraseResults() {
        RMForm frame =  (RMForm)RM.desktop.getSelectedFrame();
        if (frame == null)
            return;
        frame.race.getResults().clear();
        for (Person per : frame.race.getPersons()) {
            per.setTime(null);
        }
        frame.personForm.reloadTable();
        frame.resultForm.reloadTable();
    }
    
    public void splitCategory(String catName) {
        RMForm frame =  (RMForm)RM.desktop.getSelectedFrame();
        if (frame == null)
            return;
        
        Category oldCat = frame.getCategory(catName);
        if (oldCat==null || oldCat.getGender()!=Gender.both)
            return;
        
        oldCat.setName(catName+" - chlapci");
        oldCat.setGender(Gender.male);
        
        
        Category newCat = new Category();
        newCat.setName(catName+" - d�vky");
        newCat.setGender(Gender.female);
        newCat.setDelka(oldCat.getDelka());
        newCat.setMinYear(oldCat.getMinYear());
        newCat.setMaxYear(oldCat.getMaxYear());
        newCat.setMinNumber(oldCat.getMinNumber());
        newCat.setMaxNumber(oldCat.getMaxNumber());
        frame.race.getCategories().add(0, newCat);
        
        for (Person per : frame.race.getPersons()) {
            if (per.getCategoryName().equals(catName)) {
                if (per.isWoman())
                    per.setCategoryName(catName+" - d�vky");
                else
                    per.setCategoryName(catName+" - chlapci");
            }
        }
        frame.categoryForm.reloadTable();
        frame.personForm.reloadTable();
    }
    
    
    public void mergeCategory(String catName) {
        RMForm frame =  (RMForm)RM.desktop.getSelectedFrame();
        if (frame == null)
            return;
        if (frame.race.getCategories().get(0).getName().startsWith(catName) && frame.race.getCategories().get(1).getName().startsWith(catName)) {
            frame.race.getCategories().remove(0);
            Category oldCat = frame.race.getCategories().get(0);
            oldCat.setName(catName);
            oldCat.setGender(Gender.both);
            
            for (Person per : frame.race.getPersons()) {
                if (per.getCategoryName().startsWith(catName)) {
                    per.setCategoryName(catName);
                }
            }
            frame.categoryForm.reloadTable();
            frame.personForm.reloadTable();
        }
    }
    
    public void prepocistVysledky() {
        RMForm frame =  (RMForm)RM.desktop.getSelectedFrame();
        if (frame == null)
            return;
        
        for (Person per : frame.race.getPersons()) {
            per.setTime(null);
        }
        
    }
}

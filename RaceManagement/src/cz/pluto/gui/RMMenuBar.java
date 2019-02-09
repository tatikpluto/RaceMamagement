package cz.pluto.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import cz.pluto.data.Result;
import cz.pluto.tool.PdfTool;
import cz.pluto.tool.XmlTool;

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
        
        
        menuItem = new JMenuItem("Uložit Pøihlášené do PDF");
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
        menuItem = new JMenuItem("Uložit Startovku do PDF");
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
        menuItem = new JMenuItem("Uložit Výsledky do PDF");
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
        
        //Set up the remove results menu item.
        menuItem = new JMenuItem("Smazat všechny výsledky");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete vymazat všechny výsledky?","Pozor", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    eraseResults();
                }
            }
        });
        actionMenu.add(menuItem);
        
        //Set up the remove persons menu item.
        menuItem = new JMenuItem("Smazat všechny závodníky");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete vymazat všechny závodníky?","Pozor", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    erasePersons();
                }
            }
        });
        actionMenu.add(menuItem);
        
        //Set up the split menu item.
        menuItem = new JMenuItem("Rozdìlit kategori podle pohlaví");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete rozdìlit Benjamínky?","Pozor", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    splitCategory("Benjamínci");
                }
            }
        });
        actionMenu.add(menuItem);
        
        //Set up the join menu item.
        menuItem = new JMenuItem("Slouèit Benjamínky do jedné");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete slouèit Benjamínky do jedné kategorie?","Pozor", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    mergeCategory("Benjamínci");
                }
            }
        });
        actionMenu.add(menuItem);
        
        //Set up the recalculate menu item.
        menuItem = new JMenuItem("Pøepoèíst výsledky");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete pøepoèíst výsledky?","Pozor", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                	prepocistVysledky();
                }
            }
        });
        actionMenu.add(menuItem);
        
        //Set up the set start numbers menu item.
        menuItem = new JMenuItem("Pøiøaï startovní èísla.");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Opravdu chcete pøiøadit startovní èísla?","Pozor", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                	priradStartovniCisla();
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
        frame.personForm.reloadTable(null);
        frame.resultForm.reloadTable();
    }
    
    public void erasePersons() {
        RMForm frame =  (RMForm)RM.desktop.getSelectedFrame();
        if (frame == null)
            return;
        frame.race.getResults().clear();
        frame.race.getPersons().clear();
        frame.personForm.reloadTable(null);
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
        newCat.setName(catName+" - dívky");
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
                    per.setCategoryName(catName+" - dívky");
                else
                    per.setCategoryName(catName+" - chlapci");
            }
        }
        frame.categoryForm.reloadTable();
        frame.personForm.reloadTable(null);
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
            frame.personForm.reloadTable(null);
        }
    }
    
    public void prepocistVysledky() {
        RMForm frame =  (RMForm)RM.desktop.getSelectedFrame();
        if (frame == null)
            return;
        
        for (Person per : frame.race.getPersons()) {
            per.setTime(null);
            if (per.getStartNumber()!=null) {
              Result res = frame.getResult(per.getStartNumber());
              if (res != null) {
            	  Category cat = frame.getCategory(per.getCategoryName());
                  per.setTime(res.getTime()-cat.getStartTime());
              }
            }
        }
    }
    
    public void priradStartovniCisla() {
    	RMForm frame =  (RMForm)RM.desktop.getSelectedFrame();
        if (frame == null)
            return;
        List<Person> todo = new ArrayList<Person>();
        for (Person per : frame.race.getPersons()) {
        	if (per.getStartNumber()==null)
        		todo.add(per);
        }
        Collator col = Collator.getInstance();
        Collections.sort(todo, new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                if (!col.equals(p1.getClub(), p2.getClub()))
                    return col.compare(p1.getClub(), p2.getClub());
                else {
                    return col.compare(p1.getLabel(), p2.getLabel());
                }
            }
        });
        int lastStartNumber = PersonForm.maxNumber+1;
        for (Person per : todo) {
        	per.setStartNumber(lastStartNumber++);
        }
        PersonForm.maxNumber = lastStartNumber;
        frame.personForm.reloadTable(null);
    }
}

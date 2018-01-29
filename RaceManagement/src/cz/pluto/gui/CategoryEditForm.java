package cz.pluto.gui;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.pluto.data.Category;

@SuppressWarnings("serial")
public class CategoryEditForm extends JDialog {
    
    private RMForm masterForm;
    private Category category;
    
    private JTextField catName;
    private JFormattedTextField catTime;
    
    public CategoryEditForm(RMForm rmForm, Category p) {
        super();
        this.masterForm = rmForm;
        this.category = p;
        Point point = MouseInfo.getPointerInfo().getLocation();
        setLocation(point.x, point.y);
        init();
        pack(); 
        setVisible(true);
    }
    
    public void init() {
        JPanel panel = new JPanel(new FormLayout( "p, 2dlu, p", "p,p,p"));
        CellConstraints cc = new CellConstraints();
        
        panel.add(new JLabel("Kategorie:"), cc.xy (1, 1)); 
        catName = new JTextField(30);
        panel.add(catName, cc.xyw(3, 1, 1));
        catName.setText(category.getName());
        catName.setEditable(false);
        
        panel.add(new JLabel("Startovní èas:"), cc.xy (1, 2)); 
        catTime= new JFormattedTextField(DurationFormat.getHoursInstance());
        catTime.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(catTime, cc.xyw(3, 2, 1));
        catTime.setValue(category.getStartTime());
        
        
        
        JPanel btns = new JPanel(new FormLayout( "f:d:g, p, p", "p"));
        CellConstraints ccBtns = new CellConstraints();
        
        JButton okButton = new JButton("OK");
        btns.add(okButton, ccBtns.xyw(2, 1, 1));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
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
        panel.add(btns, cc.xyw(1, 3, 3));
        add(panel);
    }
    
    private void update() {
        Long l = (Long)catTime.getValue();
        category.setStartTime(l==null ? 0 : l);
        masterForm.categoryForm.reloadTable();
    }

}

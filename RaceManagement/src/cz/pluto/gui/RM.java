package cz.pluto.gui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

@SuppressWarnings("serial")
public class RM extends JFrame implements ActionListener {

    static JDesktopPane desktop;
    
    public RM() throws HeadlessException {
        super("Race Management");
        // TODO Auto-generated constructor stub
        desktop = new JDesktopPane(); //a specialized layered pane
        setContentPane(desktop);
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        setJMenuBar(new RMMenuBar());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        RM frame = new RM();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
                  } catch (Exception e) {
                  }
                createAndShowGUI();
            }
        });
    }
    
    public static void addForm(RMForm form) {
        desktop.add(form);
    }

}

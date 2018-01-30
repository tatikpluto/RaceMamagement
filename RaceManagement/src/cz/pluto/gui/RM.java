package cz.pluto.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

@SuppressWarnings("serial")
public class RM extends JFrame implements ActionListener {

    static JDesktopPane desktop = new JDesktopPane(); //a specialized layered pane
    
    public RM() {
        super("Race Management");
        setContentPane(desktop);
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        setJMenuBar(new RMMenuBar());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        RM frame = new RM();
        frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Display the window.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
                  } catch (Exception e) {
                      System.out.println("Problem s UI");
                  }
                createAndShowGUI();
            }
        });
    }
    
    public static void addForm(RMForm form) {
        desktop.add(form);
    }

}

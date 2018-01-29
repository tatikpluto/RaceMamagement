package cz.pluto.gui.listeners;

import java.awt.Point;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class MyPopupMenuListener implements PopupMenuListener {
    
    private JTable table;
    private JPopupMenu tableMenu;

    public MyPopupMenuListener(JTable table, JPopupMenu tableMenu) {
        this.table = table;
        this.tableMenu = tableMenu;
    }
    
    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(tableMenu, new Point(0, 0), table));
                if (rowAtPoint > -1) {
                    table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                }
            }
        });
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
    }

}

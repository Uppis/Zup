package com.vajasoft.zup;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;


public class ComparisonRenderer extends DefaultTableCellRenderer /*JMenuItem*/ implements TableCellRenderer {
    private final ImageIcon imgAdditional = new ImageIcon(ComparisonRenderer.class.getResource("/images/additional.gif"));
    private final ImageIcon imgNewer = new ImageIcon(ComparisonRenderer.class.getResource("/images/newer.gif"));
    private final ImageIcon imgOlder = new ImageIcon(ComparisonRenderer.class.getResource("/images/older.gif"));
    private final Dimension size = new Dimension(imgAdditional.getIconHeight(), imgAdditional.getIconWidth());

    public ComparisonRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel ret = (JLabel)super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
        EntryComparison comp = (EntryComparison)value;
        switch(comp) {
        case ADDITIONAL:
            ret.setIcon(imgAdditional);
            break;
        case NEWER:
            ret.setIcon(imgNewer);
            break;
        case OLDER:
            ret.setIcon(imgOlder);
            break;
        default:
            ret.setIcon(null);
            break;
        }
        return this;
    }

//    @Override
//    public Dimension getMaximumSize() {
//        return size;
//    }
//
//    @Override
//    public Dimension getMinimumSize() {
//        return size;
//    }
//
//    @Override
//    public Dimension getPreferredSize() {
//        return size;
//    }

}

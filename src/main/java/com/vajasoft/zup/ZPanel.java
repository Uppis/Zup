package com.vajasoft.zup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.zip.ZipFile;

import javax.swing.*;
import javax.swing.table.TableRowSorter;

public class ZPanel extends JPanel {
    private final BorderLayout borderLayout1 = new BorderLayout();
    private final JScrollPane jScrollPane1 = new JScrollPane();
    private final JTable tblZipEntries = new JTable();
    private final ZupTableModel model = new ZupTableModel();
    private final TableRowSorter sorter = new TableRowSorter(model);

    public ZPanel() {
        jbInit();
    }

    public void setFile(ZipFile f) {
        model.setFile(f);
    }

    private void jbInit() {
        this.setLayout(borderLayout1);
        tblZipEntries.setToolTipText("");
        tblZipEntries.setIntercellSpacing(new Dimension(5, 1));
        tblZipEntries.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tblZipEntries.setModel(model);
        tblZipEntries.setRowSorter(sorter);
        jScrollPane1.getViewport().add(tblZipEntries);
        this.add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }
}

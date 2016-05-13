package com.vajasoft.zup;

import java.util.*;
import java.util.zip.*;

public class ComparisonZupTableModel extends ZupTableModel {
    private final Map<String, ZipEntry> lookup = new HashMap<>();
    private boolean comparisonSet;

    public ComparisonZupTableModel() {
    }

    @Override
    public int getColumnCount() {
        return super.getColumnCount() + 1;
    }

    @Override
    public String getColumnName(int col) {
        return col == 0 ? "Comp" : super.getColumnName(col - 1);
    }

    @Override
    public Class getColumnClass(int col) {
        return col == 0 ? EntryComparison.class : super.getColumnClass(col - 1);
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object ret;
        if (col == 0) {
            ret = EntryComparison.IDENTICAL;
            if (comparisonSet) {
                ZipEntry e = getValue(row);
                ZipEntry other = lookup.get(e.getName());
                if (other == null) {
                    ret = EntryComparison.ADDITIONAL;
                } else if (e.getTime() > other.getTime()) {
                    ret = EntryComparison.NEWER;
                } else if (e.getTime() < other.getTime()) {
                    ret = EntryComparison.OLDER;
                }
            }
        } else {
            ret = super.getValueAt(row, col - 1);
        }
        return ret;
    }

//    public void setFile(ZipFile file) {
//        setComparisonFile(null);
//        super.setFile(file);
//    }
    
    public void setComparison(ZipFile file) {
        setComparisonFile(file);
        fireTableDataChanged();
    }
    
    private void setComparisonFile(ZipFile file) {
        lookup.clear();
        if (file != null) {
            Enumeration<? extends ZipEntry> entries = file.entries();
            for (int i = 0; entries.hasMoreElements(); i++) {
                ZipEntry e = entries.nextElement();
                if (e.getSize() > 0 || !e.getName().endsWith("/")) {
                    lookup.put(e.getName(), e);
                }
            }
        }
        comparisonSet = file != null;
    }
}

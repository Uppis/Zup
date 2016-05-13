package com.vajasoft.zup;

import java.text.Format;
import java.text.NumberFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.table.AbstractTableModel;

public class ZupTableModel extends AbstractTableModel {

    private static final String[] HEADERS = {"Name", "Type", "Modified", "Size", "Ratio", "Packed", "Path"};
    private static final Class[] CLASSES = {String.class, String.class, Date.class, Long.class, Number.class, Long.class, String.class};

    private final Format formatter = NumberFormat.getPercentInstance();
    private final List<ZipEntry> data = new ArrayList<>();
    private int[][] rowForLongestDataInCol;
    private long uncompressedSize;

    public ZupTableModel() {
        initColLengths();
    }

    @Override
    public int getColumnCount() {
        return HEADERS.length;
    }

    @Override
    public String getColumnName(int col) {
        return HEADERS[col];
    }

    @Override
    public Class getColumnClass(int c) {
        return CLASSES[c];
    }

    @Override
    public Object getValueAt(int row, int col) {
        ZipEntry e = getValue(row);
        Object ret = null;
        switch (col) {
            case 0:
                ret = getName(e.getName());
                break;
            case 1:
                ret = getType(e.getName());
                break;
            case 2:
                java.util.Date date = new java.util.Date(e.getTime());

                //ret = sFormatter.format(date);
                ret = date;
                break;
            case 3:
                ret = e.getSize();
                break;
            case 4:
                double ratio = (double) e.getCompressedSize() / e.getSize();
                ret = formatter.format(1.0 - ratio);
                break;
            case 5:
                ret = e.getCompressedSize();
                break;
            case 6:
                ret = getPath(e.getName());
                break;
        }
        return ret;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    public long getUncompressedSize() {
        return uncompressedSize;
    }

    public int getRowForLongestDataInCol(int col) {
        return rowForLongestDataInCol[col][0];
    }
    
    public void setFile(ZipFile file) {
        data.clear();
        uncompressedSize = 0;
        initColLengths();
        if (file != null) {
            Enumeration<? extends ZipEntry> entries = file.entries(); // ???
            for (int i = 0; entries.hasMoreElements(); i++) {
                ZipEntry e = entries.nextElement();
                if (e.getSize() > 0 || !e.getName().endsWith("/")) {
                    addEntry(e);
                }
                uncompressedSize += e.getSize();
            }
        }
        fireTableDataChanged();
    }

    public ZipEntry getValue(int forRow) {
        return data.get(forRow);
    }

    protected void addEntry(ZipEntry e) {
        data.add(e);
        checkColLengths(getRowCount() - 1);
    }

    private void initColLengths() {
        int cols = getColumnCount();
        rowForLongestDataInCol = new int[cols][];
        for (int col = 0; col < cols; col++ )
            rowForLongestDataInCol[col] = new int[]{0, 0};
    }
    
    private void checkColLengths(int row) {
        for (int col = getColumnCount() - 1; col >= 0; col-- ) {
            if (getColumnClass(col).equals(String.class)) {
                String val = (String)getValueAt(row, col);
                if (val.length() > rowForLongestDataInCol[col][1]) {
                    rowForLongestDataInCol[col][0] = row;
                    rowForLongestDataInCol[col][1] = val.length();
                }
            }
        }
    }

    private static String getPath(String filename) {
        String ret = "";
        int ix = filename.lastIndexOf('/');
        if (ix >= 0) {
            ret = filename.substring(0, ix);
        }
        return ret;
    }

    private static String getName(String filename) {
        String ret = filename;
        int ix = filename.lastIndexOf('/');
        if (ix >= 0) {
            ret = filename.substring(ix + 1);
        }
        return ret;
    }

    private static String getType(String filename) {
        String ret = "";
        int ix = filename.lastIndexOf('.');
        if (ix >= 0) {
            ret = filename.substring(ix + 1);
        }
        return ret;
    }
}

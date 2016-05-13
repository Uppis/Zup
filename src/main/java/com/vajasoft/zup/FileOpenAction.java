package com.vajasoft.zup;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.*;

public class FileOpenAction extends AbstractAction {
    public static final String SELECTED_FILE_PROPERTY = "selectedFile";

    private JFrame parent;
    private final JFileChooser fileChooser = new JFileChooser();

    public FileOpenAction() {
        fileChooser.setFileFilter(new ArchiveFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
    }

    public FileOpenAction(JFrame parent) {
        this();
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            putValue(SELECTED_FILE_PROPERTY, fileChooser.getSelectedFile());
        }
    }

    @Override
    public void putValue(String key, Object newValue) {
        super.putValue(key, newValue);
        if (SELECTED_FILE_PROPERTY.equals(key) && newValue != null)
            fileChooser.setCurrentDirectory((File)newValue);
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        Object old = oldValue;
        // Make sure change event is fired even when the new value is the same as old
        if (SELECTED_FILE_PROPERTY.equals(SELECTED_FILE_PROPERTY) && (old != null && newValue != null && old.equals(newValue))) {
            old = null;
        }
        super.firePropertyChange(propertyName, old, newValue);
    }
    
    public JFrame getParent() {
        return parent;
    }

    public void setParent(JFrame parent) {
        this.parent = parent;
    }
}

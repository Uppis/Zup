package com.vajasoft.zup;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.zip.ZipFile;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class ZFile extends JFrame {

    private static final String TITLE = "Zup";

    private JPanel contentPane;
    private JMenuBar menuBar1 = new JMenuBar();
    private JMenu menuFile = new JMenu();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenuItem menuFileOpen = new JMenuItem();
    private JMenuItem menuFileCompare = new JMenuItem();
    private JMenuItem menuFileSwitch = new JMenuItem();
    private JMenuItem menuFileClose = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpAbout = new JMenuItem();
    private JToolBar toolBar = new JToolBar();
    private JButton btnFileOpen = new JButton();
    private JButton btnCompare = new JButton();
    private JButton btnSwitch = new JButton();
    private JButton btnHelp = new JButton();
    private BorderLayout borderLayout1 = new BorderLayout();
    private Action fileOpenAction = new FileOpenAction(this);
    private Action fileCompareAction = new FileOpenAction(this);
    private Action fileSwitchAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            fileSwitchAction_actionPerformed();
        }
    };
    private Action fileCloseAction;
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTable tblZipEntries = new JTable();
    private ComparisonZupTableModel model = new ComparisonZupTableModel(); //new ZupTableModel();
    private TableRowSorter sorter = new TableRowSorter(model);
    private JTextField fldSelected = new JTextField();
    private JTextField fldTotal = new JTextField();
    private JSplitPane statusBar = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, fldSelected, fldTotal);

    private File archive;
    private File comparison;

    //Construct the frame
    public ZFile(String[] args) {
        super(TITLE);
        this.fileCloseAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeArchive();
            }
        };
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
            fileOpenAction.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if (FileOpenAction.SELECTED_FILE_PROPERTY.equals(evt.getPropertyName())) {
                    openArchive((File)evt.getNewValue());
                }
            });
            fileCompareAction.setEnabled(false);
            fileCompareAction.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if (FileOpenAction.SELECTED_FILE_PROPERTY.equals(evt.getPropertyName())) {
                    openComparison((File)evt.getNewValue());
                }
            });
            fileSwitchAction.setEnabled(false);
            fileCloseAction.setEnabled(false);
            tblZipEntries.setDefaultRenderer(EntryComparison.class, new ComparisonRenderer());

            if (args.length > 0) {
                fileOpenAction.putValue(FileOpenAction.SELECTED_FILE_PROPERTY, new java.io.File(args[0]));
                if (args.length > 1) {
                    fileCompareAction.putValue(FileOpenAction.SELECTED_FILE_PROPERTY, new java.io.File(args[1]));
                }
            }
            /* TEST CODE */
            TableCellRenderer r = tblZipEntries.getColumnModel().getColumn(0).getHeaderRenderer();
            /* TEST CODE */
        } catch (Exception e) {
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
        contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(1200, 900));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        statusBar.setMinimumSize(new Dimension(4, 27));
        menuFile.setText("File");
        menuFile.setMnemonic('F');
        menuFileExit.setText("Exit");
        menuFileExit.setMnemonic('X');
        menuFileExit.addActionListener(this::fileExit_actionPerformed);
        menuHelp.setText("Help");
        menuHelp.setMnemonic('H');
        menuHelpAbout.setText("About");
        menuHelpAbout.addActionListener(this::helpAbout_actionPerformed);
        btnFileOpen.setAction(fileOpenAction);
        btnFileOpen.setIcon(new ImageIcon(ZFile.class.getResource("/images/openFile.gif")));
        btnFileOpen.setFocusable(false);
        btnFileOpen.setToolTipText("Open File");
        btnCompare.setAction(fileCompareAction);
        btnCompare.setIcon(new ImageIcon(ZFile.class.getResource("/images/compareFile.gif")));
        btnCompare.setDisabledIcon(new ImageIcon(ZFile.class.getResource("/images/compareFileDisabled.gif")));
        btnCompare.setToolTipText("Compare");
        btnCompare.setFocusable(false);
        btnSwitch.setAction(fileSwitchAction);
        btnSwitch.setIcon(new ImageIcon(ZFile.class.getResource("/images/switchFiles.gif")));
        btnSwitch.setDisabledIcon(new ImageIcon(ZFile.class.getResource("/images/switchFilesDisabled.gif")));
        btnSwitch.setToolTipText("Switch");
        btnSwitch.setFocusable(false);
        btnHelp.setIcon(new ImageIcon(ZFile.class.getResource("/images/help.gif")));
        btnHelp.setToolTipText("Help");
        btnHelp.setFocusable(false);
        menuFileOpen.setAction(fileOpenAction);
        menuFileOpen.setText("Open...");
        menuFileOpen.setMnemonic('O');
        menuFileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.KeyEvent.CTRL_MASK, false));
        menuFileCompare.setAction(fileCompareAction);
        menuFileCompare.setText("Compare to...");
        menuFileCompare.setMnemonic('M');
        menuFileSwitch.setAction(fileSwitchAction);
        menuFileSwitch.setText("Switch");
        menuFileSwitch.setMnemonic('S');
        menuFileClose.setAction(fileCloseAction);
        menuFileClose.setText("Close");
        menuFileClose.setMnemonic('C');
        menuFileClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.KeyEvent.CTRL_MASK, false));
        tblZipEntries.setToolTipText("");
        tblZipEntries.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
//        tblZipEntries.setIntercellSpacing(new Dimension(5, 1));
        tblZipEntries.setModel(model);
        tblZipEntries.setRowSorter(sorter);
        fldSelected.setBackground(SystemColor.control);
        fldSelected.setEditable(false);
        fldSelected.setMinimumSize(new Dimension(50, 23));
        fldTotal.setBackground(SystemColor.control);
        fldTotal.setEditable(false);
        fldTotal.setMinimumSize(new Dimension(50, 23));
        statusBar.setResizeWeight(.5);
        toolBar.add(btnFileOpen);
        toolBar.add(btnCompare);
        toolBar.add(btnSwitch);
        toolBar.add(btnHelp);
        menuFile.add(menuFileOpen);
        menuFile.add(menuFileCompare);
        menuFile.add(menuFileSwitch);
        menuFile.add(menuFileClose);
        menuFile.addSeparator();
        menuFile.add(menuFileExit);
        menuHelp.add(menuHelpAbout);
        menuBar1.add(menuFile);
        menuBar1.add(menuHelp);
        this.setJMenuBar(menuBar1);
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(statusBar, BorderLayout.SOUTH);
        contentPane.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(tblZipEntries, null);
        for (int c = 0; c < tblZipEntries.getColumnCount(); c++) {
            tblZipEntries.getColumnModel().getColumn(c).sizeWidthToFit();
        }
    }

    //File | Exit action performed
    public void fileExit_actionPerformed(ActionEvent e) {
        dispose();
    }

    //Help | About action performed
    public void helpAbout_actionPerformed(ActionEvent e) {
        AboutBox dlg = new AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.setVisible(true);
    }

    private void openArchive(File f) {
        closeComparison();
        try (ZipFile zf = new ZipFile(f)) {
            archive = f;
            model.setFile(zf);
            if (model.getRowCount() > 0) {
                tblZipEntries.getSelectionModel().setSelectionInterval(0, 0);
                tblZipEntries.requestFocusInWindow();
            }
            setTitle();
            fldTotal.setText("Total " + model.getRowCount() + " files, " + (model.getUncompressedSize() / 1024) + "KB");
            setColumnSizes();
            fileCompareAction.setEnabled(true);
            fileCloseAction.setEnabled(true);
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(this, e, "Error opening file: " + f, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeArchive() {
        closeComparison();
        archive = null;
        model.setFile(null);
        setTitle();
        fldTotal.setText("");
        fileCompareAction.setEnabled(false);
        fileCloseAction.setEnabled(false);
    }

    private void openComparison(File f) {
        try (ZipFile zf = new ZipFile(f)) {
            comparison = f;
            model.setComparison(zf);
            setTitle();
            fileSwitchAction.setEnabled(true);
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(this, e, "Error opening file: " + f, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeComparison() {
        comparison = null;
        model.setComparison(null);
        fileSwitchAction.setEnabled(false);
    }

    private void fileSwitchAction_actionPerformed() {
        File tmp = archive;
        openArchive(comparison);
        openComparison(tmp);
    }

    private void setTitle() {
        StringBuilder title = new StringBuilder(TITLE);
        if (archive != null) {
            title.append(" - ").append(archive.getAbsolutePath());
            if (comparison != null) {
                title.append(" compared to ").append(comparison.getAbsolutePath());
            }
        }
        setTitle(title.toString());
    }

    private void setColumnSizes() {
        ZupTableModel mod = (ZupTableModel)tblZipEntries.getModel();
        TableCellRenderer headerRenderer = tblZipEntries.getTableHeader().getDefaultRenderer();
        TableColumnModel colMod = tblZipEntries.getColumnModel();

        for (int col = 0; col < mod.getColumnCount(); col++) {
            TableColumn column = colMod.getColumn(col);
//            Component comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
//            int headerWidth = comp.getPreferredSize().width;
            int row = mod.getRowForLongestDataInCol(col);
            TableCellRenderer columnRenderer = tblZipEntries.getDefaultRenderer(mod.getColumnClass(col));
            Component comp = columnRenderer.getTableCellRendererComponent(tblZipEntries, mod.getValueAt(row, col), false, false, row, col);
            int cellWidth = comp.getPreferredSize().width;
//            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
            column.setPreferredWidth(cellWidth);
        }
    }
}

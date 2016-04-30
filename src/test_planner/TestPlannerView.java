package test_planner;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import gaze_selection_library.*;
import gaze_library.*;

import javax.swing.JFileChooser;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;

/**
 * @author Thorsten Bövers
 * This class represents the GUI for the TestPlanner.
 */
public class TestPlannerView extends javax.swing.JFrame {  
    
    private static final long serialVersionUID = 2998991275057589184L;
       
    /**
     * Standard constructor to initialize the form.
     */
    public TestPlannerView() {
        initComponents();
        
        // Display the form
        this.setVisible(true);
    }
    
    /**
     * Getter for the TargetSelectionType.
     * @return the TargetSelectionType
     */
    public TargetSelectionType getTargetSelectionType() {
        return (TargetSelectionType) jComboBox_targetSelectionType.getSelectedItem();
    }

    /**
     * Setter for the TargetSelectionType.
     * @param targetSelectionType 
     */
    public void setTargetSelectionType(TargetSelectionType targetSelectionType) {
        jComboBox_targetSelectionType.setSelectedItem(targetSelectionType);
    }
    
    /**
     * Getter for the GazeDataProviderType.
     * @return the GazeDataProviderType
     */
    public GazeDataProviderType getGazeDataProviderType() {
        return (GazeDataProviderType) jComboBox_gazeDataProviderType.getSelectedItem();
    }

    /**
     * Setter for the GazeDataProviderType.
     * @param gazeDataProviderType 
     */
    public void setGazeDataProviderType(GazeDataProviderType gazeDataProviderType) {
        jComboBox_gazeDataProviderType.setSelectedItem(gazeDataProviderType);
    }
    
    /**
     * Getter for the NumSelection.
     * @return the NumSelection
     */
    public int getNumSelections() {
        return (int) jSpinner_numSelections.getValue();
    }

    /**
     * Setter for the NumSelection.
     * @param numSelections 
     */
    public void setNumSelections(int numSelections) {
        jSpinner_numSelections.setValue(numSelections);
    }

    /**
     * Getter for the RepetitionType.
     * @return the RepetitionType
     */
    public RepetitionType getRepType() {
        return (RepetitionType) jComboBox_repetitionType.getSelectedItem();
    }

    /**
     * Setter for the RepetitionType.
     * @param repType 
     */
    public void setRepType(RepetitionType repType) {
        jComboBox_repetitionType.setSelectedItem(repType);
    }

    /**
     * Getter for the description.
     * @return the description
     */
    public String getDescription() {
        return jTextArea_description.getText();
    }

    /**
     * Setter for the description.
     * @param description 
     */
    public void setDescription(String description) {
        jTextArea_description.setText(description);
    }

    /**
     * Getter for the SelectedListItemIndex.
     * @return the SelectedListItemIndex
     */
    public int getSelectedListItemIndex() {        
        return jList_items.getSelectedIndex();
    }
    
    /**
     * Setter for the SelectedListItemIndex.
     * @param index 
     */
    public void setSelectedListItemIndex(int index) {
        jList_items.setSelectedIndex(index);
    }
    
    /**
     * Getter for the ListModel.
     * @return the ListModel
     */
    public DefaultListModel<String> getListModel() {
        return (DefaultListModel<String>) jList_items.getModel();
    }
    
    /**
     * Setter for the ListModel.
     * @param model 
     */
    public void setListModel(DefaultListModel<String> model) {
        jList_items.setModel(model);
    }
    
    /**
     * Setter for the ListItem.
     * @param position
     * @param text 
     */
    public void setListItem(int position, String text) {
        DefaultListModel<String> model = (DefaultListModel<String>) jList_items.getModel();
        model.setElementAt(text, position);
        jList_items.setModel(model);
    }
        
    /**
     * This method sets the GUI elements for the TestRunProperties enabled or disabled.
     * @param value 
     */
    public void enableTestRunProperties(boolean value) {
        jComboBox_repetitionType.setEnabled(value);
        jComboBox_targetSelectionType.setEnabled(value);
        jComboBox_gazeDataProviderType.setEnabled(value);
        jSpinner_numSelections.setEnabled(value);
        jButton_setProperties.setEnabled(value);
    }
    
    /**
     * This method sets the GUI buttons for the editing the list enabled or disabled.
     */
    public void updateEditListItemButtons() {
        if (jList_items.getSelectedIndex() == -1) {
            jButton_deleteTest.setEnabled(false);
            jButton_shiftUp.setEnabled(false);
            jButton_shiftDown.setEnabled(false);
        } else {
            jButton_deleteTest.setEnabled(true);
            jButton_shiftUp.setEnabled(true);
            jButton_shiftDown.setEnabled(true);
        }        
    }
    
    /**
     * This method sets the GUI buttons for the editing the experiment enabled or disabled.
     */
    public void updateExperimentEditButtons() {
        if (((DefaultListModel<String>) jList_items.getModel()).isEmpty()) {
            jButton_save.setEnabled(false);
            jButton_saveAs.setEnabled(false);
            jButton_runTests.setEnabled(false);
        } else {
            jButton_save.setEnabled(true);
            jButton_saveAs.setEnabled(true);
            jButton_runTests.setEnabled(true);
        }        
    }
    
    /**
     * This method shows a FileChooser to get the path for saving the experiment.
     * @return save path
     */
    public String showSaveFileChooser() {
        javax.swing.JFileChooser saveFileChooser = new javax.swing.JFileChooser();
        saveFileChooser.setCurrentDirectory(new java.io.File("C:\\Users\\Admin\\Documents\\ma-eyetracking\\Code\\GazeFramework\\"));
        javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter("XML Files", "xml");
        saveFileChooser.setFileFilter(filter);
        int returnValue = saveFileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return saveFileChooser.getSelectedFile().getPath();
        } else {
            // cancel button clicked
           return null; 
        }  
    }
    
    /**
     * This method shows a FileChooser to get the path for opening an experiment.
     * @return path of experiment file
     */
    public String showOpenFileChooser() {
        javax.swing.JFileChooser openFileChooser = new javax.swing.JFileChooser();
        openFileChooser.setCurrentDirectory(new java.io.File("E:\\Eclipse Project\\Recorded Data\\InputTest\\"));
        javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter("XML Files", "xml");
        openFileChooser.setFileFilter(filter);
        int returnValue = openFileChooser.showOpenDialog(null);        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (openFileChooser.getSelectedFile().getName().endsWith(".xml")) {
                return openFileChooser.getSelectedFile().getPath();
            } else {
                JOptionPane.showMessageDialog(null,"Can't open file. Please select a XML file");
                return null;
            }                  
        } else {
            // cancel button clicked
           return null; 
        }        
    }
    
    /**
     * This method adds the referenced ActionListener to the buttons and comboboxes.
     * @param l 
     */
    public void setActionListener(ActionListener l){
        this.jButton_new.addActionListener(l);
        this.jButton_open.addActionListener(l);
        this.jButton_save.addActionListener(l);
        this.jButton_saveAs.addActionListener(l);
        this.jButton_runTests.addActionListener(l);
        this.jButton_addTest.addActionListener(l);
        this.jButton_deleteTest.addActionListener(l);
        this.jButton_shiftUp.addActionListener(l);
        this.jButton_shiftDown.addActionListener(l);
        this.jButton_setProperties.addActionListener(l);
        this.jComboBox_repetitionType.addActionListener(l);
        this.jComboBox_targetSelectionType.addActionListener(l);
        this.jComboBox_gazeDataProviderType.addActionListener(l);
    }    
    
    /**
     * This method adds the referenced ListSelectionListener to the jList.
     * @param l 
     */
    public void setListSelectionListener(ListSelectionListener l) {
        this.jList_items.addListSelectionListener(l);
    }
    
    /**
     * This method adds the referenced ChangeListener to the spinner.
     * @param l 
     */
    public void setChangeListener(ChangeListener l) {
        this.jSpinner_numSelections.addChangeListener(l);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_items = new javax.swing.JList<String>();
        jToolBar1 = new javax.swing.JToolBar();
        jButton_new = new javax.swing.JButton();
        jButton_open = new javax.swing.JButton();
        jButton_save = new javax.swing.JButton();
        jButton_saveAs = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton_runTests = new javax.swing.JButton();
        jButton_addTest = new javax.swing.JButton();
        jButton_deleteTest = new javax.swing.JButton();
        jButton_shiftUp = new javax.swing.JButton();
        jButton_shiftDown = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSpinner_numSelections = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_description = new javax.swing.JTextArea();
        jComboBox_targetSelectionType = new javax.swing.JComboBox<TargetSelectionType>();
        jButton_setProperties = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jComboBox_gazeDataProviderType = new javax.swing.JComboBox<GazeDataProviderType>();
        jComboBox_repetitionType = new javax.swing.JComboBox<RepetitionType>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Test Planner");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        jLabel2.setText("Test Runs:");

        jList_items.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList_items);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton_new.setText("New");
        jButton_new.setFocusable(false);
        jButton_new.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_new.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton_new);

        jButton_open.setText("Open");
        jButton_open.setFocusable(false);
        jButton_open.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_open.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton_open);

        jButton_save.setText("Save");
        jButton_save.setEnabled(false);
        jButton_save.setFocusable(false);
        jButton_save.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_save.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton_save);

        jButton_saveAs.setText("Save As");
        jButton_saveAs.setEnabled(false);
        jButton_saveAs.setFocusable(false);
        jButton_saveAs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_saveAs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton_saveAs);
        jToolBar1.add(jSeparator1);

        jButton_runTests.setText("Run Experiment");
        jButton_runTests.setEnabled(false);
        jButton_runTests.setFocusable(false);
        jButton_runTests.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_runTests.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton_runTests);

        jButton_addTest.setText("Add Test...");

        jButton_deleteTest.setText("Delete Test");
        jButton_deleteTest.setEnabled(false);

        jButton_shiftUp.setText("Shift Up");
        jButton_shiftUp.setEnabled(false);

        jButton_shiftDown.setText("Shift Down");
        jButton_shiftDown.setEnabled(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Test Run Properties"));

        jLabel3.setText("Number of Selections:");

        jSpinner_numSelections.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner_numSelections.setEnabled(false);

        jLabel4.setText("Repetition Type:");

        jLabel5.setText("Target Selection Type:");

        jLabel6.setText("Description:");

        jTextArea_description.setEditable(false);
        jTextArea_description.setColumns(20);
        jTextArea_description.setRows(5);
        jTextArea_description.setText(getDescription());
        jScrollPane2.setViewportView(jTextArea_description);

        jComboBox_targetSelectionType.setModel(new javax.swing.DefaultComboBoxModel<TargetSelectionType>(TargetSelectionType.values()));
        jComboBox_targetSelectionType.setActionCommand("targetSelectionTypeChanged");
        jComboBox_targetSelectionType.setEnabled(false);

        jButton_setProperties.setText("Set Properties");
        jButton_setProperties.setEnabled(false);

        jLabel7.setText("Gaze Data Provider Type:");

        jComboBox_gazeDataProviderType.setModel(new javax.swing.DefaultComboBoxModel<GazeDataProviderType>(GazeDataProviderType.values()));
        jComboBox_gazeDataProviderType.setActionCommand("gazeDataProviderTypeChanged");
        jComboBox_gazeDataProviderType.setEnabled(false);

        jComboBox_repetitionType.setModel(new javax.swing.DefaultComboBoxModel<RepetitionType>(RepetitionType.values()));
        jComboBox_repetitionType.setActionCommand("repetitionTypeChanged");
        jComboBox_repetitionType.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox_gazeDataProviderType, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox_targetSelectionType, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_setProperties))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSpinner_numSelections, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox_repetitionType, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(43, 43, 43))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox_gazeDataProviderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox_targetSelectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_setProperties))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner_numSelections, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jComboBox_repetitionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(0, 143, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton_deleteTest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_addTest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_shiftUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_shiftDown, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton_addTest)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_deleteTest)
                        .addGap(93, 93, 93)
                        .addComponent(jButton_shiftUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_shiftDown))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_addTest;
    private javax.swing.JButton jButton_deleteTest;
    private javax.swing.JButton jButton_new;
    private javax.swing.JButton jButton_open;
    private javax.swing.JButton jButton_runTests;
    private javax.swing.JButton jButton_save;
    private javax.swing.JButton jButton_saveAs;
    private javax.swing.JButton jButton_setProperties;
    private javax.swing.JButton jButton_shiftDown;
    private javax.swing.JButton jButton_shiftUp;
    private javax.swing.JComboBox<GazeDataProviderType> jComboBox_gazeDataProviderType;
    private javax.swing.JComboBox<RepetitionType> jComboBox_repetitionType;
    private javax.swing.JComboBox<TargetSelectionType> jComboBox_targetSelectionType;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList_items;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSpinner jSpinner_numSelections;
    private javax.swing.JTextArea jTextArea_description;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}

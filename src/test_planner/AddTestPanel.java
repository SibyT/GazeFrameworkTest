package test_planner;
import gaze_selection_library.*;
import gaze_library.*;

/**
 * @author Thorsten B�vers
 * This class represents a JPanel which is used to set the properties of a new added TestPlanItem.
 */
public class AddTestPanel extends javax.swing.JPanel {
    
    private static final long serialVersionUID = -1510408294580174486L;
    
    /**
     * This list stores the properties of GazeTarget.
     */
    private  java.util.LinkedList<String> _targetSelectionTypeProperties = new java.util.LinkedList<String>(java.util.Arrays.asList("1","4","70","100","KeyboardKeyC"));
    
    /**
     * Standard constructor to initialize the form.
     */
    public AddTestPanel() {
        initComponents();
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
     * Getter for the TestPlanItem.
     * @return the TestPlanItem
     */
    public TestPlanItem getTestPlanItem() {
        TestPlanItem testPlanItem = new TestPlanItem(getGazeDataProviderType(), getTargetSelectionType(), getTargetSelectionTypeProperties(), getNumSelections(), getRepType());
        return  testPlanItem;
    }
    
    /**
     * Getter for the TargetSelectionTypeProperties.
     * @return the TargetSelectionTypeProperties
     */
    private java.util.LinkedList<String> getTargetSelectionTypeProperties() {
        return _targetSelectionTypeProperties;
    }
    
    /**
     * Getter for the TargetSelectionType.
     * @return the TargetSelectionType
     */
    private TargetSelectionType getTargetSelectionType() {
        return (TargetSelectionType) jComboBox_targetSelectionType.getSelectedItem();
    }

    /**
     * Getter for the NumSelections.
     * @return the NumSelections
     */
    private int getNumSelections() {
        return (int) jSpinner_numSelections.getValue();
    }

    /**
     * Getter for the RepetitionType.
     * @return the RepetitionType
     */
    private RepetitionType getRepType() {
        return (RepetitionType) jComboBox_repetitionType.getSelectedItem();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jComboBox_targetSelectionType = new javax.swing.JComboBox<TargetSelectionType>();
        jButton_setProperties = new javax.swing.JButton();
        jComboBox_repetitionType = new javax.swing.JComboBox<RepetitionType>();
        jSpinner_numSelections = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jComboBox_gazeDataProviderType = new javax.swing.JComboBox<GazeDataProviderType>();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel5.setText("Target Selection Type:");

        jComboBox_targetSelectionType.setModel(new javax.swing.DefaultComboBoxModel<TargetSelectionType>(TargetSelectionType.values()));

        jButton_setProperties.setText("Set Properties");
        jButton_setProperties.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_setPropertiesActionPerformed(evt);
            }
        });

        jComboBox_repetitionType.setModel(new javax.swing.DefaultComboBoxModel<RepetitionType>(RepetitionType.values()));

        jSpinner_numSelections.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel7.setText("Gaze Data Provider Type:");

        jComboBox_gazeDataProviderType.setModel(new javax.swing.DefaultComboBoxModel<GazeDataProviderType>(GazeDataProviderType.values()));
        jComboBox_gazeDataProviderType.setActionCommand("targetSelectionTypeChanged");

        jLabel6.setText("Repetition Type:");

        jLabel8.setText("Number of Selections:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox_targetSelectionType, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_gazeDataProviderType, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinner_numSelections, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox_repetitionType, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_setProperties))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox_gazeDataProviderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_targetSelectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButton_setProperties))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_repetitionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jSpinner_numSelections, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getAccessibleContext().setAccessibleName("Test Run Properties");
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_setPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_setPropertiesActionPerformed
        BasicGazeTargetPropertiesPanel basicGazeTargetPropertiesPanel = new BasicGazeTargetPropertiesPanel();
        basicGazeTargetPropertiesPanel.setBasicGazeTargetProperties(_targetSelectionTypeProperties);
	int result = javax.swing.JOptionPane.showConfirmDialog(null, basicGazeTargetPropertiesPanel,
            "BasicGazeTarget Properties", javax.swing.JOptionPane.OK_CANCEL_OPTION);
	if (result == javax.swing.JOptionPane.OK_OPTION) {
            _targetSelectionTypeProperties = basicGazeTargetPropertiesPanel.getBasicGazeTargetProperties();
        }
    }//GEN-LAST:event_jButton_setPropertiesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_setProperties;
    private javax.swing.JComboBox<GazeDataProviderType> jComboBox_gazeDataProviderType;
    private javax.swing.JComboBox<RepetitionType> jComboBox_repetitionType;
    private javax.swing.JComboBox<TargetSelectionType> jComboBox_targetSelectionType;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSpinner jSpinner_numSelections;
    // End of variables declaration//GEN-END:variables
}

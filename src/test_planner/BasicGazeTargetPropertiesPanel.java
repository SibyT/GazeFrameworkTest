package test_planner;
import gaze_selection_library.SelectionTriggerType;
import javax.swing.JSplitPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout;
import javax.swing.JTabbedPane;
import javax.swing.JInternalFrame;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

/**
 * @author Thorsten Bövers
 * This class represents a JPanel which is used to set the properties of the BasicGazeTarget.
 */
public class BasicGazeTargetPropertiesPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 5564707545467814702L;
    
    /**
     * Standard constructor to create the form.
     */
    public BasicGazeTargetPropertiesPanel() {
        initComponents();
    }
    
    /**
     * Getter for the BasicGazeTargetProperties
     * @return the BasicGazeTargetProperties
     */
    public java.util.LinkedList<String> getBasicGazeTargetProperties() {
        java.util.LinkedList<String> _basicGazeTargetProperties = new java.util.LinkedList<String>();
        _basicGazeTargetProperties.add(getFeedbackCursor());
        _basicGazeTargetProperties.add(getNumberOfButtons());
        _basicGazeTargetProperties.add(getButtonHeightRatio());
        _basicGazeTargetProperties.add(getDeadSpace());
        _basicGazeTargetProperties.add(getSelectionTriggerType());
        switch (getSelectionTriggerType()) {
            case "DwellTime":
                _basicGazeTargetProperties.add(getDwellTime());
                _basicGazeTargetProperties.add(getAccuracy());
                break;
            case "Wink":
                _basicGazeTargetProperties.add(getDwellTime());
                break;
        }
        return _basicGazeTargetProperties;
    }
    
    /**
     * Setter for the BasicGazeTargetProperties
     * @param _basicGazeTargetProperties 
     */
    public void setBasicGazeTargetProperties(java.util.LinkedList<String> _basicGazeTargetProperties) {
        setFeedbackCursor(_basicGazeTargetProperties.get(0));
        setNumberOfButtons(_basicGazeTargetProperties.get(1));
        setButtonHeightRatio(_basicGazeTargetProperties.get(2));
        setDeadSpace(_basicGazeTargetProperties.get(3));
        setSelectionTriggerType(_basicGazeTargetProperties.get(4));
        switch (_basicGazeTargetProperties.get(4)) {
            case "DwellTime":
                setDwellTime(_basicGazeTargetProperties.get(5));
                setAccuracy(_basicGazeTargetProperties.get(6));
                break;
            case "Wink":
                setDwellTime(_basicGazeTargetProperties.get(5));
                break;
        }
    }
    
    /**
     * Getter for the FeedbackCursor
     * @return the FeedbackCursor
     */
    public String getFeedbackCursor() {
        if (jCheckBox_useFeedbackCursor.isSelected()) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * Setter for the FeedbackCursor
     * @param feedbackCursor 
     */
    public void setFeedbackCursor(String feedbackCursor) {
        if (feedbackCursor.equals("1")) {
            jCheckBox_useFeedbackCursor.setSelected(true);
        } else {
            jCheckBox_useFeedbackCursor.setSelected(false);
        }
    }
    
    /**
     * Getter for the NumberOfButtons
     * @return the NumberOfButtons
     */
    public String getNumberOfButtons() {
        return jSpinner_numberOfButtons.getValue().toString();
    }
    
    /**
     * Setter for the NumberOfButtons
     * @param numberOfButtons 
     */
    public void setNumberOfButtons(String numberOfButtons) {
        jSpinner_numberOfButtons.setValue(Integer.valueOf(numberOfButtons));
    } 
    
    /**
     * Getter for the ButtonHeightRatio
     * @return the ButtonHeightRatio
     */
    public String getButtonHeightRatio() {
        return jSpinner_buttonHeightRatio.getValue().toString();
    }
    
    /**
     * Setter for the ButtonHeightRatio
     * @param buttonHeightRatio 
     */
    public void setButtonHeightRatio(String buttonHeightRatio) {
        jSpinner_buttonHeightRatio.setValue(Integer.valueOf(buttonHeightRatio));
    } 
    
    /**
     * Getter for the DeadSpace
     * @return the DeadSpace
     */
    public String getDeadSpace() {
        return jSpinner_deadSpace.getValue().toString();
    }
    
    /**
     * Setter for the DeadSpace
     * @param deadSpace 
     */
    public void setDeadSpace(String deadSpace) {
        jSpinner_deadSpace.setValue(Integer.valueOf(deadSpace));
    } 
    
    /**
     * Getter for the SelectionTriggerType
     * @return the SelectionTriggerType
     */
    public String getSelectionTriggerType() {
        return jComboBox_selectionTriggerType.getSelectedItem().toString();
    }
    
    /**
     * Setter for the SelectionTriggerType
     * @param selectionTriggerType 
     */
    public void setSelectionTriggerType(String selectionTriggerType) {
        switch (selectionTriggerType) {
            case "KeyboardKeyC":
                jComboBox_selectionTriggerType.setSelectedItem(SelectionTriggerType.KeyboardKeyC);
                break;
            case "DwellTime":
                jComboBox_selectionTriggerType.setSelectedItem(SelectionTriggerType.DwellTime);
                break;
            case "Wink":
                jComboBox_selectionTriggerType.setSelectedItem(SelectionTriggerType.Wink);
                break;
            case "Speech":
                jComboBox_selectionTriggerType.setSelectedItem(SelectionTriggerType.Speech);
                break;
        }
    }
    
    /**
     * Getter for the DwellTime
     * @return the DwellTime
     */
    public String getDwellTime() {
        return jSpinner_dwellTime.getValue().toString();
    }
    
    /**
     * Setter for the DwellTime
     * @param dwellTime 
     */
    public void setDwellTime(String dwellTime) {
        jSpinner_dwellTime.setValue(Integer.valueOf(dwellTime));
    } 
    
    /**
     * Getter for the Accuracy
     * @return the Accuracy
     */
    public String getAccuracy() {
        return jSpinner_accuracy.getValue().toString();
    }
    
    /**
     * Setter for the Accuracy
     * @param accuracy 
     */
    public void setAccuracy(String accuracy) {
        jSpinner_accuracy.setValue(Integer.valueOf(accuracy));
    } 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jSpinner_numberOfButtons = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner_buttonHeightRatio = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinner_deadSpace = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinner_dwellTime = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinner_accuracy = new javax.swing.JSpinner();
        jComboBox_selectionTriggerType = new javax.swing.JComboBox<SelectionTriggerType>();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox_useFeedbackCursor = new javax.swing.JCheckBox();

        jSpinner_numberOfButtons.setModel(new javax.swing.SpinnerNumberModel(1, 1, 20, 1));

        jLabel1.setText("Number of Buttons:");

        jLabel2.setText("Button Height Ratio:");

        jSpinner_buttonHeightRatio.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel3.setText("Dead Space:");

        jSpinner_deadSpace.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel4.setText("Dwell Time (in Milliseconds):");

        jSpinner_dwellTime.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel5.setText("Accuracy:");

        jSpinner_accuracy.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jComboBox_selectionTriggerType.setModel(new javax.swing.DefaultComboBoxModel<SelectionTriggerType>(SelectionTriggerType.values()));
        jComboBox_selectionTriggerType.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_selectionTriggerTypeItemStateChanged(evt);
            }
        });

        jLabel6.setText("Selection Trigger Type:");
        setLayout(new FormLayout(new ColumnSpec[] {
        		FormFactory.UNRELATED_GAP_COLSPEC,
        		ColumnSpec.decode("111px"),
        		FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        		ColumnSpec.decode("16px"),
        		FormFactory.UNRELATED_GAP_COLSPEC,
        		ColumnSpec.decode("125px"),},
        	new RowSpec[] {
        		RowSpec.decode("114px"),
        		RowSpec.decode("23px"),
        		FormFactory.NARROW_LINE_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormFactory.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormFactory.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormFactory.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormFactory.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),
        		FormFactory.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("20px"),}));

        jCheckBox_useFeedbackCursor.setSelected(true);
        jCheckBox_useFeedbackCursor.setText("Use Feedback Cursor");
        add(jCheckBox_useFeedbackCursor, "2, 2, 3, 1, left, top");
        add(jLabel6, "2, 10, left, center");
        add(jComboBox_selectionTriggerType, "4, 10, 3, 1, fill, top");
        add(jLabel5, "2, 14, left, center");
        add(jSpinner_accuracy, "6, 14, left, top");
        add(jLabel4, "2, 12, 3, 1, left, center");
        add(jSpinner_dwellTime, "6, 12, left, top");
        add(jLabel1, "2, 4, left, center");
        add(jSpinner_numberOfButtons, "6, 4, left, top");
        add(jLabel3, "2, 8, left, center");
        add(jSpinner_deadSpace, "6, 8, left, top");
        add(jLabel2, "2, 6, left, center");
        add(jSpinner_buttonHeightRatio, "6, 6, left, top");
    }// </editor-fold>                        

    private void jComboBox_selectionTriggerTypeItemStateChanged(java.awt.event.ItemEvent evt) {                                                                
        switch (getSelectionTriggerType()) {
            case "KeyboardKeyC":
                jLabel4.setEnabled(false);
                jSpinner_dwellTime.setEnabled(false);
                jLabel5.setEnabled(false);
                jSpinner_accuracy.setEnabled(false);                
                break;
            case "DwellTime":
                jLabel4.setText("Dwell Time (in Milliseconds):");
                jLabel4.setEnabled(true);
                jSpinner_dwellTime.setEnabled(true);
                jLabel5.setEnabled(true);
                jSpinner_accuracy.setEnabled(true);  
                break;
            case "Wink":
                jLabel4.setText("Wink Time (in Milliseconds):");
                jLabel4.setEnabled(true);
                jSpinner_dwellTime.setEnabled(true);
                jLabel5.setEnabled(false);
                jSpinner_accuracy.setEnabled(false);  
                break;
            case "Speech":
                jLabel4.setEnabled(false);
                jSpinner_dwellTime.setEnabled(false);
                jLabel5.setEnabled(false);
                jSpinner_accuracy.setEnabled(false); 
                break;
        }
    }                                                               


    // Variables declaration - do not modify                     
    private javax.swing.JCheckBox jCheckBox_useFeedbackCursor;
    private javax.swing.JComboBox<SelectionTriggerType> jComboBox_selectionTriggerType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSpinner jSpinner_accuracy;
    private javax.swing.JSpinner jSpinner_buttonHeightRatio;
    private javax.swing.JSpinner jSpinner_deadSpace;
    private javax.swing.JSpinner jSpinner_dwellTime;
    private javax.swing.JSpinner jSpinner_numberOfButtons;
}
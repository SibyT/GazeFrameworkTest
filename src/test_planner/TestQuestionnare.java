package test_planner;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Label;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Component;

public class TestQuestionnare extends JFrame {
	private final JPanel contentPanel = new JPanel();

	public TestQuestionnare() {
		this.setVisible(false);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setTitle("Questionnare");
		getContentPane().setLayout(null);
		getContentPane().setSize(100, 200);
		
		JLabel lblPleaseRateThe = new JLabel("Please rate the below questionnare  for above test.");
		lblPleaseRateThe.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblPleaseRateThe.setBounds(10, 11, 434, 31);
		getContentPane().add(lblPleaseRateThe);
		
		JLabel lblTheTechniqueWas = new JLabel("The Technique was accurate");
		lblTheTechniqueWas.setBounds(10, 53, 385, 14);
		getContentPane().add(lblTheTechniqueWas);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(0, 0, 5, 1));
		spinner.setBounds(405, 50, 29, 20);
		getContentPane().add(spinner);
		
		JLabel lblNewLabel = new JLabel("The technique was less accurate when button was small");
		lblNewLabel.setBounds(10, 78, 391, 14);
		getContentPane().add(lblNewLabel);
		
		Label label = new Label("With this technique Items can be selected quickly.");
		label.setBounds(10, 101, 385, 22);
		getContentPane().add(label);
		
		Label label_1 = new Label("The technique was easy to use");
		label_1.setBounds(10, 129, 385, 22);
		getContentPane().add(label_1);
		
		Label label_2 = new Label("Overall satisfied with this technique");
		label_2.setBounds(10, 157, 385, 22);
		getContentPane().add(label_2);
		
		Label label_3 = new Label("This technique requires lot of physical effort");
		label_3.setBounds(10, 183, 385, 22);
		getContentPane().add(label_3);
		
		Label label_4 = new Label("This technique requires lot of mental effort");
		label_4.setBounds(10, 211, 385, 22);
		getContentPane().add(label_4);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setBounds(405, 75, 29, 20);
		getContentPane().add(spinner_1);
		
		JSpinner spinner_2 = new JSpinner();
		spinner_2.setBounds(405, 103, 29, 20);
		getContentPane().add(spinner_2);
		
		JSpinner spinner_3 = new JSpinner();
		spinner_3.setBounds(405, 159, 29, 20);
		getContentPane().add(spinner_3);
		
		JSpinner spinner_4 = new JSpinner();
		spinner_4.setBounds(405, 131, 29, 20);
		getContentPane().add(spinner_4);
		
		JSpinner spinner_5 = new JSpinner();
		spinner_5.setBounds(405, 185, 29, 20);
		getContentPane().add(spinner_5);
		
		JSpinner spinner_6 = new JSpinner();
		spinner_6.setBounds(405, 211, 29, 20);
		getContentPane().add(spinner_6);
		
		Button button = new Button("Finish");
		button.setBounds(364, 239, 70, 22);
		getContentPane().add(button);
		setBounds(100, 100, 450, 300);
		//getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	}
}

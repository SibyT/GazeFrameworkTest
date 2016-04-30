package test_planner;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
//import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import java.util.Date;
import java.util.Calendar;
import java.util.Timer;

import javax.swing.SpinnerNumberModel;
import java.awt.Button;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JProgressBar;

public class TestCaseUsabilityTime extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestCaseUsabilityTime frame = new TestCaseUsabilityTime();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestCaseUsabilityTime() {
		setTitle("Testcase for Usability With Time");
		final Stopwatch sw= new Stopwatch();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 344);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		final JFormattedTextField formattedTextField = new JFormattedTextField();
		formattedTextField.setBounds(234, 198, 190, 20);
		contentPane.add(formattedTextField);
		
		
		JLabel lblNewLabel = new JLabel("Elapsed Time in milliseconds");
		lblNewLabel.setBounds(23, 201, 171, 14);
		contentPane.add(lblNewLabel);
		Button button = new Button("5");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			sw.elapsedTime();
			formattedTextField.setValue(sw.elapsedTime());
			}
		});

		button.setBounds(191, 163, 70, 22);
		contentPane.add(button);
		
		JButton btnNewButton = new JButton("1");
		btnNewButton.setBounds(10, 49, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("2");
		btnNewButton_1.setBounds(335, 49, 89, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("3");
		btnNewButton_2.setBounds(10, 118, 89, 23);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("4");
		btnNewButton_3.setBounds(335, 118, 89, 23);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Next");
		btnNewButton_4.setBounds(335, 227, 89, 23);
		contentPane.add(btnNewButton_4);
		
		JTextPane txtpnPleaseSelectIn = new JTextPane();
		txtpnPleaseSelectIn.setText("Please Select in order of Buttons");
		txtpnPleaseSelectIn.setBounds(23, 11, 401, 20);
		contentPane.add(txtpnPleaseSelectIn);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setValue(75);
		progressBar.setIndeterminate(true);
		progressBar.setString("75%");
		progressBar.setBounds(37, 264, 369, 14);
		contentPane.add(progressBar);
		
		
	}
}

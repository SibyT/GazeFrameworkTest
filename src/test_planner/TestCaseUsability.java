package test_planner;

import gaze_selection_library.GazeTarget;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TestCaseUsability extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestCaseUsability frame = new TestCaseUsability();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void setGazeTarget(GazeTarget targetPanel) {
		// add GazeTarget to the panel
		this.add(targetPanel);
		//update the jPanel
		this.validate();
	}

	/**
	 * Create the frame.
	 */
	public TestCaseUsability() {
		setTitle("Check Usability for Different Objects");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JButton btnNewButton = new JButton("Select Button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setForeground(Color.GREEN);
			}
		});
		btnNewButton.setForeground(Color.RED);
		btnNewButton.setBounds(30, 37, 145, 23);
		contentPane.add(btnNewButton);
		
		final JRadioButton rdbtnNewRadioButton = new JRadioButton("Select radio button");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnNewRadioButton.setForeground(Color.GREEN);
			}
		});
		rdbtnNewRadioButton.setForeground(Color.RED);
		rdbtnNewRadioButton.setBounds(30, 77, 153, 23);
		contentPane.add(rdbtnNewRadioButton);
		
		final JCheckBox chckbxNewCheckBox = new JCheckBox("Select check box");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxNewCheckBox.setForeground(Color.GREEN);
			}
		});
		chckbxNewCheckBox.setForeground(Color.RED);
		chckbxNewCheckBox.setBounds(30, 105, 153, 23);
		contentPane.add(chckbxNewCheckBox);
		
		final JToggleButton tglbtnNewToggleButton = new JToggleButton("Select toggle button");
		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tglbtnNewToggleButton.setForeground(Color.GREEN);

			}
		});
		tglbtnNewToggleButton.setForeground(Color.RED);
		tglbtnNewToggleButton.setBounds(30, 150, 148, 23);
		contentPane.add(tglbtnNewToggleButton);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(59, 228, 329, 14);
		contentPane.add(progressBar);
		
		JButton btnNewButton_1 = new JButton("Next");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setBounds(335, 194, 89, 23);
		contentPane.add(btnNewButton_1);
	}
}

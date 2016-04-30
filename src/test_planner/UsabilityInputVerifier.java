package test_planner;

import javax.swing.InputVerifier;
import javax.swing.*;

public class UsabilityInputVerifier extends InputVerifier {

	@Override
	public boolean verify(JComponent input) {
		// TODO Auto-generated method stub
		String text = ((JTextField) input).getText().trim();
		if (text.isEmpty())
			return false;
		else
			return true;
	}

}

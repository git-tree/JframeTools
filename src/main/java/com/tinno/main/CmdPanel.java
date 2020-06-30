package com.tinno.main;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class CmdPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public CmdPanel() {
		setLayout(null);
		
		JLabel lblCmd = new JLabel("cmd");
		lblCmd.setFont(new Font("宋体", Font.PLAIN, 40));
		lblCmd.setBounds(157, 55, 187, 103);
		add(lblCmd);

	}

}

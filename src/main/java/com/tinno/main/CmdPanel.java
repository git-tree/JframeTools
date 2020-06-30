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
		
		JLabel lblCmd = new JLabel("cmd更新中..");
		lblCmd.setFont(new Font("宋体", Font.PLAIN, 40));
		lblCmd.setBounds(77, 70, 248, 103);
		add(lblCmd);

	}

}

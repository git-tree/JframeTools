package com.tinno.main;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class MonkeyTestPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public MonkeyTestPanel() {
		setLayout(null);
		
		JLabel lblMonkey = new JLabel("monkey...");
		lblMonkey.setFont(new Font("宋体", Font.PLAIN, 40));
		lblMonkey.setBounds(104, 104, 578, 111);
		add(lblMonkey);

	}
}

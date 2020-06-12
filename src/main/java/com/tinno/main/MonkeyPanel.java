package com.tinno.main;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class MonkeyPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public MonkeyPanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("monkey更新中..");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 38));
		lblNewLabel.setBounds(59, 108, 319, 107);
		add(lblNewLabel);

	}
}

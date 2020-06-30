package com.tinno.main;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class ExcelPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public ExcelPanel() {
		setLayout(null);
		
		JLabel lblExcel = new JLabel("Excel更新中...");
		lblExcel.setFont(new Font("宋体", Font.PLAIN, 40));
		lblExcel.setBounds(231, 141, 322, 122);
		add(lblExcel);

	}

}

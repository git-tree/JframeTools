package com.tinno.utils;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

public class JFrameutil {
	/**
	 * 弹出一个提示框
	 * @param msg
	 */
	public static void showdialog(String msg){
		JOptionPane.showMessageDialog(null, msg);
	}
	/**
	 * 让滚动条始终在下(显示最新信息)
	 */
	public static void txtShow_alwaysnew(JTextPane panel){
		// 让滚动条始终在下(显示最新信息)
		DefaultCaret caret = (DefaultCaret) panel.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

}

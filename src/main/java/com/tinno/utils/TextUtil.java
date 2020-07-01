package com.tinno.utils;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.tinno.enums.ColorEnum;

public class TextUtil {
	/**
	 * 自定义颜色
	 * @param text
	 * @param textColor
	 * @param txt_show_panel
	 * @param errorcolor
	 */
	public static void insertDocument(String text, Color textColor,JTextPane txt_show_panel,Color errorcolor)// 根据传入的颜色及文字，将文字插入文本域
	{
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground(set, textColor);// 设置文字颜色
		StyleConstants.setFontSize(set, 14);// 设置字体大小
		Document doc = txt_show_panel.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), "\n" + text, set);// 插入文字
		} catch (BadLocationException e) {
			txt_show_panel.setForeground(errorcolor);
			txt_show_panel.setText("出现了一点问题....");
			return;
		}
	}
	/**
	 * 巧克力颜色
	 * @param text
	 * @param txt_show_panel
	 */
	public static void insertDocument_cho(String text,JTextPane txt_show_panel)// 根据传入的颜色及文字，将文字插入文本域
	{
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground(set, ColorEnum.CHOCPLATECOLOR.getColor());// 设置文字颜色
		StyleConstants.setFontSize(set, 14);// 设置字体大小
		Document doc = txt_show_panel.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), "\n" + text, set);// 插入文字
		} catch (BadLocationException e) {
			txt_show_panel.setForeground(ColorEnum.ERRORCOLOR.getColor());
			txt_show_panel.setText("出现了一点问题....");
			return;
		}
	}
	/**
	 * info颜色
	 * @param text
	 * @param txt_show_panel
	 */
	public static void insertDocument_info(String text,JTextPane txt_show_panel)// 根据传入的颜色及文字，将文字插入文本域
	{
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground(set, ColorEnum.INFOCOLOR.getColor());// 设置文字颜色
		StyleConstants.setFontSize(set, 14);// 设置字体大小
		Document doc = txt_show_panel.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), "\n" + text, set);// 插入文字
		} catch (BadLocationException e) {
			txt_show_panel.setForeground(ColorEnum.ERRORCOLOR.getColor());
			txt_show_panel.setText("出现了一点问题....");
			return;
		}
	}
	/**
	 * 成功颜色
	 * @param text
	 * @param txt_show_panel
	 */
	public static void insertDocument_success(String text,JTextPane txt_show_panel)// 根据传入的颜色及文字，将文字插入文本域
	{
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground(set, ColorEnum.SUCCESSCOLOR.getColor());// 设置文字颜色
		StyleConstants.setFontSize(set, 14);// 设置字体大小
		Document doc = txt_show_panel.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), "\n" + text, set);// 插入文字
		} catch (BadLocationException e) {
			txt_show_panel.setForeground(ColorEnum.ERRORCOLOR.getColor());
			txt_show_panel.setText("出现了一点问题....");
			return;
		}
	}
}

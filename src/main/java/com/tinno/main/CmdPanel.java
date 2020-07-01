package com.tinno.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.tinno.enums.ColorEnum;
import com.tinno.utils.CmdUtil;
import com.tinno.utils.TextUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class CmdPanel extends JPanel {
	private String choicepath;
	private JTextField txt_screenOn_time;
	/**
	 * Create the panel.
	 * @throws PropertyVetoException 
	 */
	public CmdPanel() throws PropertyVetoException {
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 10, 902, 49);
		add(panel);
		panel.setLayout(null);

		final JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(10, 69, 902, 396);
		add(panel_1);
		panel_1.setLayout(null);
		
		final JInternalFrame frame_screen = new JInternalFrame("屏幕相关");
		frame_screen.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame_screen.setResizable(true);
		frame_screen.setClosable(true);
		frame_screen.setMaximizable(true);
		frame_screen.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame_screen.setBounds(0, 10, 892, 386);
		frame_screen.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 231, 306, 127);
		frame_screen.getContentPane().add(scrollPane);
		
		final JTextPane txt_show = new JTextPane();
		txt_show.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		scrollPane.setViewportView(txt_show);
		
		JButton btn_clear_log = new JButton("清空");
		btn_clear_log.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_clear_log.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt_show.setText("");
			}
		});
		scrollPane.setColumnHeaderView(btn_clear_log);
		
		final JButton btn_screen_alwaysOn = new JButton("屏幕常亮");
		btn_screen_alwaysOn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_screen_alwaysOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CmdUtil.setalways_screenon();
				TextUtil.insertDocument("设置成功,【"+btn_screen_alwaysOn.getText()+"】", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
			}
		});
		btn_screen_alwaysOn.setBounds(10, 90, 93, 23);
		frame_screen.getContentPane().add(btn_screen_alwaysOn);
		
		JButton btn_make_screen_on = new JButton("亮屏");
		btn_make_screen_on.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CmdUtil.makeScreenOn();
				TextUtil.insertDocument("设置成功,【点亮屏幕】", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
			}
		});
		btn_make_screen_on.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_make_screen_on.setBounds(10, 115, 93, 23);
		frame_screen.getContentPane().add(btn_make_screen_on);
		
		JButton btn_make_screen_off = new JButton("灭屏");
		btn_make_screen_off.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CmdUtil.makeScreenOff();
				TextUtil.insertDocument("设置成功,【息灭屏幕】", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
			}
		});
		btn_make_screen_off.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_make_screen_off.setBounds(10, 138, 93, 23);
		frame_screen.getContentPane().add(btn_make_screen_off);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u8BBE\u7F6E\u533A", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(10, 10, 477, 43);
		frame_screen.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JButton btn_choice_path = new JButton("选择地址");
		btn_choice_path.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择地址");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					choicepath=fileChooser.getSelectedFile().getPath().trim();
					System.out.println(choicepath);
				}
			}
		});
		btn_choice_path.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_choice_path.setBounds(59, 10, 93, 23);
		panel_2.add(btn_choice_path);
		
		JButton btn_choice_path_1 = new JButton("检查adb环境");
		btn_choice_path_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(CmdUtil.checkAdb()){
					TextUtil.insertDocument("adb环境OK", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
				}else{
					TextUtil.insertDocument("adb环境异常，请检查", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
				}
			}
		});
		btn_choice_path_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_choice_path_1.setBounds(160, 10, 110, 23);
		panel_2.add(btn_choice_path_1);
		
		txt_screenOn_time = new JTextField();
		txt_screenOn_time.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_screenOn_time.setBounds(96, 58, 93, 21);
//		txt_screenOn_time.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				char ch = e.getKeyChar();
//				if (ch < '0' || ch > '9')
//					e.consume();
//			}
//		});
		frame_screen.getContentPane().add(txt_screenOn_time);
		txt_screenOn_time.setColumns(10);
		
		JButton btn_make_screen_off_1 = new JButton("设置");
		btn_make_screen_off_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_make_screen_off_1.setBounds(199, 57, 57, 23);
		frame_screen.getContentPane().add(btn_make_screen_off_1);
		
		JLabel lblNewLabel = new JLabel("亮屏时间(min)");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 61, 78, 15);
		frame_screen.getContentPane().add(lblNewLabel);
		frame_screen.setVisible(true);
		panel_1.add(frame_screen);
		
		JButton btn_about_screen = new JButton("屏幕相关");
		btn_about_screen.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_about_screen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame_screen.show();
			}
		});
		btn_about_screen.setBounds(10, 10, 93, 23);
		panel.add(btn_about_screen);
	}
}

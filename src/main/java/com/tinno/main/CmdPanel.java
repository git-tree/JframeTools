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
import com.tinno.utils.JFrameutil;
import com.tinno.utils.TextUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.SystemColor;

public class CmdPanel extends JPanel {
	private String choicepath;
	private JTextField txt_screenOn_time;
	private  JTextPane txt_show_battery;
	private JTextField txt_battery;
	private  JPanel panel_frames;
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

		panel_frames= new JPanel();
		panel_frames.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_frames.setBounds(10, 69, 902, 396);
		add(panel_frames);
		panel_frames.setLayout(null);
		
		final JInternalFrame frame_screen = new JInternalFrame("屏幕相关");
		frame_screen.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame_screen.setResizable(true);
		frame_screen.setClosable(true);
		frame_screen.setMaximizable(true);
		frame_screen.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame_screen.setBounds(10, 10, 182, 136);
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
		btn_make_screen_off_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String getstr=txt_screenOn_time.getText().trim();
				double time=0.0;
				if("".equals(getstr)){
					JFrameutil.showdialog("请输入时间");
					return;
				}
				try {
					time=Double.parseDouble(getstr);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					JFrameutil.showdialog("请输入正确数字");
					return;
				}
				try {
					CmdUtil.setscreen_off_timeout(time);
					TextUtil.insertDocument("设置时间成功", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					TextUtil.insertDocument("设置时间失败", ColorEnum.ERRORCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
				}
				
			}
		});
		btn_make_screen_off_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_make_screen_off_1.setBounds(199, 57, 57, 23);
		frame_screen.getContentPane().add(btn_make_screen_off_1);
		
		JLabel lblNewLabel = new JLabel("亮屏时间(min)");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 61, 78, 15);
		frame_screen.getContentPane().add(lblNewLabel);
		frame_screen.setVisible(true);
		panel_frames.add(frame_screen);
		
		final JInternalFrame frame_battery = new JInternalFrame("电池");
		frame_battery.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame_battery.setResizable(true);
		frame_battery.setMaximizable(true);
		frame_battery.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame_battery.setClosable(true);
		frame_battery.setBounds(202, 10, 182, 136);
		panel_frames.add(frame_battery);
		frame_battery.getContentPane().setLayout(null);
		
		JButton btn_unplug = new JButton("模拟插线不充电");
		btn_unplug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CmdUtil.setBatteryUnplug();
				TextUtil.insertDocument_success("模拟成功", txt_show_battery);
			}
		});
		btn_unplug.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_unplug.setBounds(135, 10, 124, 23);
		frame_battery.getContentPane().add(btn_unplug);
		
		JButton btn_reset_battery = new JButton("重置电池设置");
		btn_reset_battery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!CmdUtil.checkDevice_isconn()){
					TextUtil.insertDocument("没连接设备", ColorEnum.ERRORCOLOR.getColor(),txt_show_battery, ColorEnum.ERRORCOLOR.getColor());
					return;
				}
				CmdUtil.resetBattery();
				TextUtil.insertDocument_success("重置成功", txt_show_battery);
			}
		});
		btn_reset_battery.setBackground(SystemColor.activeCaption);
		btn_reset_battery.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_reset_battery.setBounds(10, 10, 115, 23);
		frame_battery.getContentPane().add(btn_reset_battery);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 193, 288, 145);
		frame_battery.getContentPane().add(scrollPane_1);
		
		txt_show_battery= new JTextPane();
		txt_show_battery.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		scrollPane_1.setViewportView(txt_show_battery);
		
		JButton btn_clear_battery = new JButton("清空");
		btn_clear_battery.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_clear_battery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt_show_battery.setText("");
			}
		});
		scrollPane_1.setColumnHeaderView(btn_clear_battery);
		
		JLabel lblNewLabel_1 = new JLabel("输入电量:");
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(10, 54, 75, 15);
		frame_battery.getContentPane().add(lblNewLabel_1);
		
		txt_battery = new JTextField();
		txt_battery.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_battery.setBounds(74, 51, 66, 21);
		txt_battery.addKeyListener(new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			char ch = e.getKeyChar();
			if (ch < '0' || ch > '9')
				e.consume();
		}
	});
		frame_battery.getContentPane().add(txt_battery);
		txt_battery.setColumns(10);
		
		JButton btn_setbattery_level = new JButton("设置");
		btn_setbattery_level.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String battery_level=txt_battery.getText();
				if("".equals(battery_level)){
					JFrameutil.showdialog("请输入数值");
					return;
				}
				if(Integer.parseInt(battery_level)>100){
					JFrameutil.showdialog("请输入小于等于100的电量");
					return;
				}
				CmdUtil.setbatteryLevel(Integer.parseInt(battery_level));
				TextUtil.insertDocument_success("设置电量成功!", txt_show_battery);
			}
		});
		btn_setbattery_level.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_setbattery_level.setBounds(145, 50, 87, 23);
		frame_battery.getContentPane().add(btn_setbattery_level);
		frame_battery.setVisible(true);
		
		JButton btn_about_screen = new JButton("屏幕相关");
		btn_about_screen.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_about_screen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame_screen.show();
			}
		});
		btn_about_screen.setBounds(10, 10, 93, 23);
		panel.add(btn_about_screen);
		
		JButton btn_about_bttery = new JButton("电池相关");
		btn_about_bttery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame_battery.show();
			}
		});
		btn_about_bttery.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_about_bttery.setBounds(113, 10, 93, 23);
		panel.add(btn_about_bttery);
	}
}

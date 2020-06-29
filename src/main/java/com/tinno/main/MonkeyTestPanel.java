package com.tinno.main;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import com.tinno.enums.ColorEnum;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.security.auth.callback.ChoiceCallback;
import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import com.tinno.enums.PackageTypeEnum;
import com.tinno.pojo.MonkeyString;
import com.tinno.utils.CmdUtil;
import com.tinno.utils.TextUtil;

import javax.swing.JTextField;

public class MonkeyTestPanel extends JPanel {
	private String cmdcommand="adb shell pm list packages ";
	private String [] cmd_reslut;
	private DefaultListModel model=new DefaultListModel<>();
	private JTextField txt_testcount;
	private JTextField txt_throttle;
	private JTextField txt_seed;
	private JTextField txt_touch;
	private JTextField txt_slip_screen;
	private JTextField txt_ball;
	private JTextField txt_round;
	private JTextField txt_sys_key;
	private JTextField txt_keyborad_round;
	private JTextField txt_nav;
	private JTextField txt_main_nav;
	private JTextField txt_app_switch;
	private JTextField txt_other;
	private List<String> pkg;
	private String level;
	private long testcount;
	private long event_space;
	private long seed;
	private String crash_goon="";
	private String  anr_goon="";
	private String security_goon="";
	private String exception_stay="";
	private String lising_code="";
	private String generate_report="";
	private long touchPersent;
	private long motionPersent;
	private long trackballPersent;
	private long navPersent;
	private long turnonPersent;
	private long mainnavPersent;
	private long systemkeyPersent;
	private long appswitchPersent;
	private long keyboardPersent;
	private long otherPersent;
	private MonkeyString monkey;
	/**
	 * Create the panel.
	 */
	public MonkeyTestPanel() {
		setLayout(null);
		final MyJcheckBox cell = new MyJcheckBox();
		
		JPanel panel_pkg = new JPanel();
		panel_pkg.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u9009\u62E9\u8981\u6D4B\u8BD5\u7684\u5305", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_pkg.setBounds(10, 10, 339, 245);
		add(panel_pkg);
		panel_pkg.setLayout(null);
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 48, 319, 187);
		panel_pkg.add(scrollPane_1);
		
		final JCheckBox checkbox_all = new JCheckBox("全选/取消");
		
		
		checkbox_all.setBackground(new Color(192, 192, 192));
		checkbox_all.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		scrollPane_1.setColumnHeaderView(checkbox_all);
		final JList list = new JList();
		scrollPane_1.setViewportView(list);
		list.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		list.setModel(model);
		list.setCellRenderer(cell);
		
		//list支持多选
		list.setSelectionModel(new DefaultListSelectionModel() {
			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					super.removeSelectionInterval(index0, index1);
				} else {
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		
		
		final JComboBox combox_select_pkg = new JComboBox();
		combox_select_pkg.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		combox_select_pkg.setModel(new DefaultComboBoxModel(PackageTypeEnum.values()));
		combox_select_pkg.setBounds(10, 17, 151, 21);
		panel_pkg.add(combox_select_pkg);
		
		JButton btn_refresh_pkg = new JButton("查看包");
		btn_refresh_pkg.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_refresh_pkg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				switch (combox_select_pkg.getSelectedItem().toString()) {
				case "全部":
					
					cmd_reslut=CmdUtil.excuteCMDCommand_str(cmdcommand).replaceAll("package:", "").split("\n");
					model.removeAllElements();
					for (int i = 0; i < cmd_reslut.length; i++) {
						model.addElement(cmd_reslut[i]);
					}
					break;
				case "系统":
					cmd_reslut=CmdUtil.excuteCMDCommand_str(cmdcommand+"-s").replaceAll("package:", "").split("\n");
					model.removeAllElements();
					for (int i = 0; i < cmd_reslut.length; i++) {
						model.addElement(cmd_reslut[i]);
					}
					break;
				case "三方":
					cmd_reslut=CmdUtil.excuteCMDCommand_str(cmdcommand+"-3").replaceAll("package:", "").split("\n");;
					model.removeAllElements();
					for (int i = 0; i < cmd_reslut.length; i++) {
						model.addElement(cmd_reslut[i]);
					}
					break;
				case "启用":
					cmd_reslut=CmdUtil.excuteCMDCommand_str(cmdcommand+"-e").replaceAll("package:", "").split("\n");;
					model.removeAllElements();
					for (int i = 0; i < cmd_reslut.length; i++) {
						model.addElement(cmd_reslut[i]);
					}
					break;
				case "禁用":
					cmd_reslut=CmdUtil.excuteCMDCommand_str(cmdcommand+"-d").replaceAll("package:", "").split("\n");;
					model.removeAllElements();
					for (int i = 0; i < cmd_reslut.length; i++) {
						model.addElement(cmd_reslut[i]);
					}
					break;

				default:
					break;
				}
			}
		});
		btn_refresh_pkg.setBounds(171, 15, 69, 23);
		panel_pkg.add(btn_refresh_pkg);
		
		JButton btn_choice_pkg = new JButton("选中");
		btn_choice_pkg.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_choice_pkg.setBounds(250, 15, 79, 23);
		panel_pkg.add(btn_choice_pkg);
		
		JPanel panel_log = new JPanel();
		panel_log.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "log", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_log.setBounds(10, 327, 910, 151);
		add(panel_log);
		panel_log.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 40, 890, 107);
		panel_log.add(scrollPane);
		
		final JTextPane txt_show = new JTextPane();
		txt_show.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		scrollPane.setViewportView(txt_show);
		
		JButton btn_clear_log = new JButton("清空");
		btn_clear_log.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_clear_log.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt_show.setText("");
			}
		});
		btn_clear_log.setBounds(819, 10, 70, 23);
		panel_log.add(btn_clear_log);
		
		JPanel panel_base_setting = new JPanel();
		panel_base_setting.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u53C2\u6570\u914D\u7F6E", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_base_setting.setBounds(356, 10, 339, 245);
		add(panel_base_setting);
		panel_base_setting.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("日志详细等级:");
		lblNewLabel.setBounds(13, 19, 75, 15);
		panel_base_setting.add(lblNewLabel);
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		final JComboBox combox_level = new JComboBox();
		combox_level.setBounds(105, 16, 56, 21);
		panel_base_setting.add(combox_level);
		combox_level.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		combox_level.setModel(new DefaultComboBoxModel(new String[] {"低", "中", "高"}));
		combox_level.setSelectedIndex(1);
		
		JLabel lblNewLabel_1 = new JLabel("测试小时:");
		lblNewLabel_1.setBounds(23, 53, 54, 15);
		panel_base_setting.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		txt_testcount = new JTextField();
		txt_testcount.setText("8");
		txt_testcount.setBounds(92, 47, 75, 21);
		panel_base_setting.add(txt_testcount);
		txt_testcount.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_testcount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_testcount.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("事件间隔(ms):");
		lblNewLabel_2.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(171, 19, 88, 15);
		panel_base_setting.add(lblNewLabel_2);
		
		txt_throttle = new JTextField();
		txt_throttle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_throttle.setText("200");
		txt_throttle.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_throttle.setColumns(10);
		txt_throttle.setBounds(245, 16, 64, 21);
		panel_base_setting.add(txt_throttle);
		
		JLabel lblNewLabel_3 = new JLabel("种子值:");
		lblNewLabel_3.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(176, 53, 54, 15);
		panel_base_setting.add(lblNewLabel_3);
		
		txt_seed = new JTextField();
		txt_seed.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_seed.setText("200");
		txt_seed.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_seed.setColumns(10);
		txt_seed.setBounds(245, 47, 64, 21);
		panel_base_setting.add(txt_seed);
		
		final JCheckBox check_ignore_crashes = new JCheckBox("出现crash继续");
		check_ignore_crashes.setSelected(true);
		check_ignore_crashes.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		check_ignore_crashes.setBounds(13, 76, 121, 23);
		panel_base_setting.add(check_ignore_crashes);
		
		final JCheckBox check_ignore_anr = new JCheckBox("出现anr继续");
		check_ignore_anr.setSelected(true);
		check_ignore_anr.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		check_ignore_anr.setBounds(176, 74, 103, 23);
		
		
		panel_base_setting.add(check_ignore_anr);
		
		final JCheckBox check_stay_exception = new JCheckBox("出现异常停留在异常状态");
		check_stay_exception.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		check_stay_exception.setBounds(176, 99, 157, 23);
		panel_base_setting.add(check_stay_exception);
		
		final JCheckBox check_monitor_native_crashes = new JCheckBox("监视崩溃代码");
		check_monitor_native_crashes.setSelected(true);
		check_monitor_native_crashes.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		check_monitor_native_crashes.setBounds(13, 101, 121, 23);
		panel_base_setting.add(check_monitor_native_crashes);
		
		final JCheckBox check_creat_report = new JCheckBox("生成report");
		check_creat_report.setSelected(true);
		check_creat_report.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		check_creat_report.setBounds(13, 126, 121, 23);
		panel_base_setting.add(check_creat_report);
		
		JPanel panel_event = new JPanel();
		panel_event.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u4E8B\u4EF6\u6BD4\u4F8B(%)", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_event.setBounds(705, 21, 215, 234);
		add(panel_event);
		panel_event.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("触摸");
		lblNewLabel_4.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(10, 26, 45, 15);
		panel_event.add(lblNewLabel_4);
		
		txt_touch = new JTextField();
		txt_touch.setText("10");
		txt_touch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_touch.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_touch.setColumns(10);
		txt_touch.setBounds(65, 21, 33, 21);
		panel_event.add(txt_touch);
		
		txt_slip_screen = new JTextField();
		txt_slip_screen.setText("10");
		txt_slip_screen.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_slip_screen.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_slip_screen.setColumns(10);
		txt_slip_screen.setBounds(160, 21, 33, 21);
		panel_event.add(txt_slip_screen);
		
		JLabel lblNewLabel_4_1 = new JLabel("滑屏");
		lblNewLabel_4_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4_1.setBounds(108, 26, 54, 15);
		panel_event.add(lblNewLabel_4_1);
		
		JLabel lblNewLabel_4_1_1 = new JLabel("轨迹球");
		lblNewLabel_4_1_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4_1_1.setBounds(10, 67, 54, 15);
		panel_event.add(lblNewLabel_4_1_1);
		
		JLabel lblNewLabel_4_1_2 = new JLabel("旋转");
		lblNewLabel_4_1_2.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4_1_2.setBounds(10, 108, 54, 15);
		panel_event.add(lblNewLabel_4_1_2);
		
		JLabel lblNewLabel_4_1_3 = new JLabel("导航");
		lblNewLabel_4_1_3.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4_1_3.setBounds(108, 67, 54, 15);
		panel_event.add(lblNewLabel_4_1_3);
		
		JLabel lblNewLabel_4_1_4 = new JLabel("主导航");
		lblNewLabel_4_1_4.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4_1_4.setBounds(108, 108, 54, 15);
		panel_event.add(lblNewLabel_4_1_4);
		
		JLabel lblNewLabel_4_1_4_1 = new JLabel("系统按键");
		lblNewLabel_4_1_4_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4_1_4_1.setBounds(10, 149, 54, 15);
		panel_event.add(lblNewLabel_4_1_4_1);
		
		JLabel lblNewLabel_4_1_4_2 = new JLabel("APP切换");
		lblNewLabel_4_1_4_2.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4_1_4_2.setBounds(108, 149, 54, 15);
		panel_event.add(lblNewLabel_4_1_4_2);
		
		JLabel lblNewLabel_4_1_4_1_1 = new JLabel("键盘翻转");
		lblNewLabel_4_1_4_1_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4_1_4_1_1.setBounds(10, 190, 54, 15);
		panel_event.add(lblNewLabel_4_1_4_1_1);
		
		JLabel lblNewLabel_4_1_4_1_2 = new JLabel("其他");
		lblNewLabel_4_1_4_1_2.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4_1_4_1_2.setBounds(108, 190, 54, 15);
		panel_event.add(lblNewLabel_4_1_4_1_2);
		
		txt_ball = new JTextField();
		txt_ball.setText("10");
		txt_ball.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_ball.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_ball.setColumns(10);
		txt_ball.setBounds(65, 63, 33, 21);
		panel_event.add(txt_ball);
		
		txt_round = new JTextField();
		txt_round.setText("10");
		txt_round.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_round.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_round.setColumns(10);
		txt_round.setBounds(65, 105, 33, 21);
		panel_event.add(txt_round);
		
		txt_sys_key = new JTextField();
		txt_sys_key.setText("10");
		txt_sys_key.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_sys_key.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_sys_key.setColumns(10);
		txt_sys_key.setBounds(65, 147, 33, 21);
		panel_event.add(txt_sys_key);
		
		txt_keyborad_round = new JTextField();
		txt_keyborad_round.setText("10");
		txt_keyborad_round.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_keyborad_round.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_keyborad_round.setColumns(10);
		txt_keyborad_round.setBounds(65, 189, 33, 21);
		panel_event.add(txt_keyborad_round);
		
		txt_nav = new JTextField();
		txt_nav.setText("10");
		txt_nav.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_nav.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_nav.setColumns(10);
		txt_nav.setBounds(160, 63, 33, 21);
		panel_event.add(txt_nav);
		
		txt_main_nav = new JTextField();
		txt_main_nav.setText("10");
		txt_main_nav.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_main_nav.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_main_nav.setColumns(10);
		txt_main_nav.setBounds(160, 105, 33, 21);
		panel_event.add(txt_main_nav);
		
		txt_app_switch = new JTextField();
		txt_app_switch.setText("10");
		txt_app_switch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_app_switch.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_app_switch.setColumns(10);
		txt_app_switch.setBounds(160, 147, 33, 21);
		panel_event.add(txt_app_switch);
		
		txt_other = new JTextField();
		txt_other.setText("10");
		txt_other.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_other.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_other.setColumns(10);
		txt_other.setBounds(160, 189, 33, 21);
		panel_event.add(txt_other);
		
		JButton btnNewButton = new JButton("开始");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//获取选中包名
				pkg=list.getSelectedValuesList();
				if(pkg.size()==0){
					showdialog("请选择测试包名");
					return;
				}
				for (int i = 0; i < pkg.size(); i++) {
					if(pkg.get(i).toString().equals("com.mediatek.mtklogger")){
						pkg.remove(i);
					}
				}
				System.out.println(pkg.toString());
				//获取日志等级
				level=combox_level.getSelectedItem().toString();
				if(level.equals("低")){
					level=" -v ";
				}else if(level.equals("中")){
					level=" -v -v ";
				}else{
					level=" -v -v -v ";
				}
				//获取事件间隔
				event_space=Integer.parseInt(txt_throttle.getText());
				//获取次数
				if("".equals(txt_testcount.getText())){
					JOptionPane.showMessageDialog(null, "请输入次数");
					return;
				}
				if(event_space<200||event_space>500){
					showdialog("事件间隔请输入200-500之间的数字");
					return;
				}
				testcount=Integer.parseInt(txt_testcount.getText())*60*60*1000/event_space;
				//获取seed
				seed=Integer.parseInt(txt_seed.getText());
				//获取是否crash继续
				if(check_ignore_crashes.isSelected()){
					crash_goon="--ignore-crashes";
				}
				//获取是否anr继续
				if(check_ignore_anr.isSelected()){
					anr_goon="--ignore-timeouts";
				}
				//获取异常停留界面
				if(check_stay_exception.isSelected()){
					exception_stay="--kill-process-after-error";
				}
				//获取监视代码
				if(check_monitor_native_crashes.isSelected()){
					lising_code="--monitor-native-crashes";
				}
				//获取report
				if(check_creat_report.isSelected()){
					generate_report="--hprof";
				}
				//获取触摸
				if(!"".equals(txt_touch.getText())){
					touchPersent=Integer.parseInt(txt_touch.getText());
				}
				//获取滑屏
				if(!"".equals(txt_slip_screen.getText())){
					motionPersent=Integer.parseInt(txt_slip_screen.getText());
				}
				//获取轨迹球事件
				if(!"".equals(txt_ball.getText())){
					trackballPersent=Integer.parseInt(txt_ball.getText());
				}
				//获取导航
				if(!"".equals(txt_nav.getText())){
					navPersent=Integer.parseInt(txt_nav.getText());
				}
				//获取旋转
				if(!"".equals(txt_round.getText())){
					turnonPersent=Integer.parseInt(txt_round.getText());
				}
				//获取主导航
				if(!"".equals(txt_main_nav.getText())){
					mainnavPersent=Integer.parseInt(txt_main_nav.getText());
				}
				//获取系统按键
				if(!"".equals(txt_sys_key.getText())){
					systemkeyPersent=Integer.parseInt(txt_sys_key.getText());
				}
				//获取app切换
				if(!"".equals(txt_app_switch.getText())){
					appswitchPersent=Integer.parseInt(txt_app_switch.getText());
				}
				//获取键盘翻转
				if(!"".equals(txt_keyborad_round.getText())){
					keyboardPersent=Integer.parseInt(txt_keyborad_round.getText());
				}
				//获取其他
				if(!"".equals(txt_other.getText())){
					otherPersent=Integer.parseInt(txt_other.getText());
				}
				
				monkey=new MonkeyString(pkg, level, testcount, event_space, seed, crash_goon, anr_goon, exception_stay, lising_code, generate_report, touchPersent, motionPersent, trackballPersent, navPersent, turnonPersent, mainnavPersent, systemkeyPersent, appswitchPersent, keyboardPersent, otherPersent);
				monkey.setPkg(pkg);
				System.out.println("monkey的实体参数为："+monkey.toString());
				//生成monkey命令
				final StringBuilder sb =new StringBuilder();
				sb.append("adb shell monkey ");
				//加上包名
				for(int i =0;i<monkey.getPkg().size();i++){
					sb.append("-p "+pkg.get(i).toString()+" ");
				}
				//加上配置
				//seed
				sb.append("-s "+monkey.getSeed()+" ");
				//事件间隔
				sb.append("--throttle "+monkey.getEvent_space()+" ");
				//crash
				sb.append(monkey.getCrash_goon()+" ");
				//anr
				sb.append(monkey.getAnr_goon()+" ");
				//许可
//				sb.append(monkey.getSecurity_goon()+" ");
				//异常停留
				sb.append(monkey.getException_stay()+" ");
				//监视代码
				sb.append(monkey.getLising_code()+" ");
				//report
				sb.append(monkey.getGenerate_report()+" ");
				//事件比例
				//触摸
				sb.append("--pct-touch "+monkey.getTouchPersent()+" ");
				//滑屏
				sb.append("--pct-motion "+monkey.getMotionPersent()+" ");
				//轨迹球
				sb.append("--pct-trackball "+monkey.getTrackballPersent()+" ");
				//导航
				sb.append("--pct-nav "+monkey.getNavPersent()+" ");
				//旋转
				sb.append("--pct-rotation "+monkey.getTurnonPersent()+" ");
				//主导航
				sb.append("--pct-majornav "+monkey.getMainnavPersent()+" ");
				//系统按键
				sb.append("--pct-syskeys "+monkey.getSystemkeyPersent()+" ");
				//app切换
				sb.append("--pct-appswitch "+monkey.getAppswitchPersent()+" ");
				//键盘翻转
				sb.append("--pct-flip "+monkey.getKeyboardPersent()+" ");
				//其他
				sb.append("--pct-anyevent "+monkey.getOtherPersent()+" ");
				//日志
				sb.append(monkey.getLevel());
				//次数
				sb.append(testcount);
				System.out.println("monkey命令为:"+sb);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						TextUtil.insertDocument("monkey命令为:"+sb, ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
						CmdUtil.excuteCMDCommand_str(sb.toString(),false);
					}
				}).start();
				
			}
		});
		btnNewButton.setBounds(64, 294, 93, 23);
		add(btnNewButton);
		//全选按钮点击事件
		checkbox_all.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(checkbox_all.isSelected()){
					list.clearSelection();
					//全选
					for (int i = 0; i < model.size(); i++) {
						list.setSelectedIndex(i);
					}
				}else{
					//取消全选
					list.clearSelection();
				}
			}
		});
	}
private void showdialog(String str){
	JOptionPane.showMessageDialog(null, str);
}
//复选框
public class MyJcheckBox extends JCheckBox implements ListCellRenderer {
	
	public MyJcheckBox() {
		super();
	}
 
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		this.setText(value.toString());
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		this.setSelected(isSelected);
		return this;
	}
}
}

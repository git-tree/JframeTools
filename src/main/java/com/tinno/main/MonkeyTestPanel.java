package com.tinno.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import com.tinno.enums.ColorEnum;
import com.tinno.enums.PackageTypeEnum;
import com.tinno.pojo.MonkeyString;
import com.tinno.utils.CmdUtil;
import com.tinno.utils.TextUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

import javax.swing.SwingConstants;

public class MonkeyTestPanel extends JPanel {
	private String cmdcommand="adb shell pm list packages ";
	private String [] cmd_reslut;
	private DefaultListModel model=new DefaultListModel<>();
	private  JTextPane txt_show ;
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
	private List<String> black_pkg=new ArrayList<>();
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
	private JTextField txt_black_pkg;
	private String choicepath;
	private String choice_logpath;
	private String logfilename="";
	private final String date_formate_role="yyyyMMddHHmmss";
	private String off_log_name;
	private JCheckBox check_ignore_anr;
	private String monkey_dir_name;
	/**
	 * Create the panel.
	 */
	public MonkeyTestPanel() {
		setLayout(null);
		final MyJcheckBox cell = new MyJcheckBox();
		
		JPanel panel_pkg = new JPanel();
		panel_pkg.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u9009\u62E9\u8981\u6D4B\u8BD5\u7684\u5305", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_pkg.setBounds(10, 10, 339, 276);
		add(panel_pkg);
		panel_pkg.setLayout(null);
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 79, 319, 187);
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
		combox_select_pkg.setBounds(10, 17, 113, 21);
		panel_pkg.add(combox_select_pkg);
		
		JButton btn_refresh_pkg = new JButton("查看");
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
		btn_refresh_pkg.setBounds(133, 16, 69, 23);
		panel_pkg.add(btn_refresh_pkg);
		
		JButton btn_import_black_pkg = new JButton("导入黑名单");
		btn_import_black_pkg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择黑名单");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					choicepath=fileChooser.getSelectedFile().getPath().trim();
					if(!choicepath.endsWith(".txt")){
						TextUtil.insertDocument("请选择txt格式文档", ColorEnum.INFOCOLOR.getColor(),txt_show ,ColorEnum.ERRORCOLOR.getColor());
						showdialog("请选择txt格式文档");
						choicepath="";
						return;
					}
					FileReader fileReader = new FileReader(FileUtil.file(choicepath),"UTF-8");
					for (int i = 0; i < fileReader.readLines().size(); i++) {
						if(!black_pkg.contains(fileReader.readLines().get(i).toString())){
							black_pkg.add(fileReader.readLines().get(i).toString());
						}
					}
					TextUtil.insertDocument("当前黑名单为:", ColorEnum.INFOCOLOR.getColor(),txt_show ,ColorEnum.ERRORCOLOR.getColor());
					for (int i = 0; i < black_pkg.size(); i++) {
						TextUtil.insertDocument(black_pkg.get(i), ColorEnum.CHOCPLATECOLOR.getColor(),txt_show ,ColorEnum.ERRORCOLOR.getColor());
					}
					showdialog("添加成功!");
				}
			}
		});
		btn_import_black_pkg.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_import_black_pkg.setBounds(228, 16, 101, 23);
		panel_pkg.add(btn_import_black_pkg);
		
		txt_black_pkg = new JTextField();
		txt_black_pkg.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_black_pkg.setColumns(10);
		txt_black_pkg.setBounds(10, 48, 101, 21);
		panel_pkg.add(txt_black_pkg);
		
		JButton btn_add_blackPkg = new JButton("添加黑名单");
		btn_add_blackPkg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(black_pkg==null){
					black_pkg=new ArrayList<>();
				}
				if("".equals(txt_black_pkg.getText().toString().trim())){
					showdialog("请输入要添加的包名");
					return;
				}
				if(black_pkg.contains(txt_black_pkg.getText().toString().trim())){
					showdialog("已存在此黑名单,请勿重复添加!");
					return;
				}
				black_pkg.add(txt_black_pkg.getText().toString().trim());
				showdialog("添加成功");
				TextUtil.insertDocument("当前黑名单为:", ColorEnum.INFOCOLOR.getColor(),txt_show ,ColorEnum.ERRORCOLOR.getColor());
				for (int i = 0; i < black_pkg.size(); i++) {
					TextUtil.insertDocument(black_pkg.get(i), ColorEnum.CHOCPLATECOLOR.getColor(),txt_show ,ColorEnum.ERRORCOLOR.getColor());
				}
			}
		});
		btn_add_blackPkg.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_add_blackPkg.setBounds(117, 47, 101, 23);
		panel_pkg.add(btn_add_blackPkg);
		
		JButton btn_add_blackPkg_1 = new JButton("清空黑名单");
		btn_add_blackPkg_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(black_pkg==null ||black_pkg.size()==0){
					showdialog("没有黑名单了。");
					return;
				}else{
					black_pkg.clear();
					showdialog("清空完成");
					TextUtil.insertDocument("当前黑名单为空", ColorEnum.SUCCESSCOLOR.getColor(),txt_show ,ColorEnum.ERRORCOLOR.getColor());
				}
			}
		});
		btn_add_blackPkg_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_add_blackPkg_1.setBounds(228, 46, 101, 23);
		panel_pkg.add(btn_add_blackPkg_1);
		
		JPanel panel_log = new JPanel();
		panel_log.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "log", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_log.setBounds(10, 327, 910, 151);
		add(panel_log);
		panel_log.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 40, 890, 107);
		panel_log.add(scrollPane);
		
		txt_show= new JTextPane();
		txt_show.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		// 让滚动条始终在下(显示最新信息)
		DefaultCaret caret = (DefaultCaret) txt_show.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(txt_show);
		
		JButton btn_clear_log = new JButton("清空日志");
		btn_clear_log.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_clear_log.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txt_show.setText("");
			}
		});
		btn_clear_log.setBounds(795, 10, 94, 23);
		panel_log.add(btn_clear_log);
		
		JPanel panel_base_setting = new JPanel();
		panel_base_setting.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u53C2\u6570\u914D\u7F6E", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_base_setting.setBounds(356, 10, 339, 276);
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
		combox_level.setSelectedIndex(2);
		
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
		
		check_ignore_anr = new JCheckBox("出现anr继续");
		check_ignore_anr.setSelected(true);
		check_ignore_anr.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		check_ignore_anr.setBounds(176, 74, 103, 23);
		
		
		panel_base_setting.add(check_ignore_anr);
		
		final JCheckBox check_stay_exception = new JCheckBox("出现异常直接停留");
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
		
		JButton btn_reset_normal_config = new JButton("恢复默认配置");
		btn_reset_normal_config.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				combox_level.setSelectedIndex(2);
				txt_throttle.setText("200");
				txt_testcount.setText("8");
				txt_seed.setText("200");
				check_ignore_crashes.setSelected(true);
				check_ignore_anr.setSelected(true);
				check_monitor_native_crashes.setSelected(true);
				check_stay_exception.setSelected(false);
				check_creat_report.setSelected(true);
			}
		});
		btn_reset_normal_config.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_reset_normal_config.setBounds(105, 243, 125, 23);
		panel_base_setting.add(btn_reset_normal_config);
		
		JPanel panel_event = new JPanel();
		panel_event.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u4E8B\u4EF6\u6BD4\u4F8B(%)", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_event.setBounds(705, 10, 215, 276);
		add(panel_event);
		panel_event.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("触摸");
		lblNewLabel_4.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(10, 26, 45, 15);
		panel_event.add(lblNewLabel_4);
		
		txt_touch = new JTextField();
		txt_touch.setText("15");
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
		txt_ball.setText("2");
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
		txt_round.setText("5");
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
		txt_sys_key.setText("2");
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
		txt_keyborad_round.setText("1");
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
		txt_nav.setText("25");
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
		txt_main_nav.setText("15");
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
		txt_app_switch.setText("5");
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
		txt_other.setText("15");
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
		
		JButton btn_reset_normal_persent = new JButton("恢复默认比例");
		btn_reset_normal_persent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//设置触摸
					txt_touch.setText("15");
				//设置滑屏
					txt_slip_screen.setText("10");
				//设置轨迹球事件
					txt_ball.setText("2");
				//设置导航
					txt_nav.setText("25");
				//设置旋转
					txt_round.setText("5");
				//设置主导航
					txt_main_nav.setText("15");
				//设置系统按键
					txt_sys_key.setText("2");
				//设置app切换
					txt_app_switch.setText("5");
				//设置键盘翻转
					txt_keyborad_round.setText("1");
				//设置其他
					txt_other.setText("15");
			}
		});
		btn_reset_normal_persent.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_reset_normal_persent.setBounds(49, 243, 125, 23);
		panel_event.add(btn_reset_normal_persent);
		
		JButton btn_startmonkey = new JButton("在线monkey测试");
		btn_startmonkey.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_startmonkey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//获取选中包名
				pkg=list.getSelectedValuesList();
				if(pkg.size()==0){
					showdialog("请选择测试包名");
					return;
				}
				if(choice_logpath==null||"".equals(choice_logpath)){
					showdialog("请选log存放路径");
					return;
				}
				String date_now=DateUtil.format(new Date(), date_formate_role);
				//创建文件夹
				monkey_dir_name=FileUtil.mkdir(choice_logpath+"monkey_"+date_now).getAbsolutePath();//eg:e:/monkey_202005310624
				//log txt 文件名
//				logfilename="monkeyLog_"+date_now+".txt";
//				File logfile=FileUtil.file(choice_logpath+logfilename);
//				if(logfile.exists()){
//					logfile.delete();
//				}
				//去除logging
				for (int i = 0; i < pkg.size(); i++) {
					if(pkg.get(i).toString().equals("com.mediatek.mtklogger")){
						pkg.remove(i);
					}
				}
				//去除黑名单的包名
				HashSet hs1 = new HashSet(pkg);
		        HashSet hs2 = new HashSet(black_pkg);
		        hs1.removeAll(hs2);
		        List<String> listC = new ArrayList<String>();
		        listC.addAll(hs1);
		        pkg=listC;
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
				long totalPersent=touchPersent+motionPersent+trackballPersent+navPersent+turnonPersent+mainnavPersent+systemkeyPersent+appswitchPersent+keyboardPersent+otherPersent;
				if(totalPersent>100){
					showdialog("事件总比例不能超过100%");
					return;
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
				//路径，文件夹_日期/info.txt  和 error.txt  如e:/monkey_20200530/info.txt,e:/monkey_20200530/error.txt
				sb.append(" 2>"+monkey_dir_name+"/monkey_error.txt 1>"+monkey_dir_name+"/monkey_info.txt");
				System.out.println(sb.toString());
				TextUtil.insertDocument("正在启动monkey...", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//屏幕常亮
						CmdUtil.setalways_screenon();
						CmdUtil.excuteCMDCommand_str(sb.toString(),false);
					}
				}).start();
				
				new Thread(new  Runnable() {
					public void run() {
						try {
							TextUtil.insertDocument("初始化...", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
							Thread.sleep(4000);
							TextUtil.insertDocument("初始化完成!", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
							String result=CmdUtil.excuteCMDCommand_str("adb shell ps -A|findstr monkey");
							if(!"".equals(result)){
								TextUtil.insertDocument("启动monkey成功!", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								TextUtil.insertDocument("测试时间"+txt_testcount.getText()+"小时...", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								Thread.sleep(800);
								TextUtil.insertDocument("日志等级"+combox_level.getSelectedItem().toString()+"...", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								Thread.sleep(800);
								TextUtil.insertDocument("测试应用", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								for (int i = 0; i < pkg.size(); i++) {
									TextUtil.insertDocument(pkg.get(i).toString(), ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
									Thread.sleep(100);
								}
								Thread.sleep(800);
								TextUtil.insertDocument("黑名单应用", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								for (int j = 0; j < black_pkg.size(); j++) {
									TextUtil.insertDocument(black_pkg.get(j).toString(), ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
									Thread.sleep(100);
								}
								return;
							}else{
								TextUtil.insertDocument("启动monkey失败，可能选择的包不支持!", ColorEnum.ERRORCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								return;
							}
						} catch (InterruptedException e) {
							return;
						}
					}
				}).start();
				
			}
		});
		btn_startmonkey.setBounds(161, 294, 137, 23);
		add(btn_startmonkey);
		
		final JButton btn_stopMonkey = new JButton("停止monkey测试");
		btn_stopMonkey.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_stopMonkey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String res=CmdUtil.excuteCMDCommand_str("adb shell ps -A|findstr monkey");
						if(res.length()==0){
							TextUtil.insertDocument("无monkey任务..", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
							return;
						}
						String [] resarr=res.split("\n");
						TextUtil.insertDocument("正在停止..", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
						btn_stopMonkey.setEnabled(false);
						try {
							for (int i = 0; i < resarr.length; i++) {
								System.out.println(resarr[i]+i);
								for(int j=0;j<resarr[i].split(" ").length;j++){
									CmdUtil.excuteCMDCommand_str("adb shell kill -9 "+resarr[i].split(" ")[j]);
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							TextUtil.insertDocument("停止失败！", ColorEnum.ERRORCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
							btn_stopMonkey.setEnabled(true);
						}
						btn_stopMonkey.setEnabled(true);
						TextUtil.insertDocument("停止成功！", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
					}
					
				}).start();
			}
		});
		btn_stopMonkey.setBounds(705, 294, 126, 23);
		add(btn_stopMonkey);
		
		JButton btn_monkeyoffline = new JButton("离线monkey测试");
		btn_monkeyoffline.setHorizontalAlignment(SwingConstants.LEFT);
		btn_monkeyoffline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				#!/usr/bin/env bash
				//获取选中包名
				pkg=list.getSelectedValuesList();
				if(pkg.size()==0){
					showdialog("请选择测试包名");
					return;
				}
//				if(choice_logpath==null||"".equals(choice_logpath)){
//					showdialog("请选log存放路径");
//					return;
//				}
				//log txt 文件
				String date_now=DateUtil.format(new Date(), date_formate_role);
				logfilename="monkeyLog_"+date_now+".txt";
				File logfile=FileUtil.file(choice_logpath+logfilename);
				if(logfile.exists()){
					logfile.delete();
				}
				//创建文件夹
				CmdUtil.excuteCMDCommand_str("adb shell mkdir /sdcard/monkey_"+date_now);//eg:e:/monkey_202005310624
				monkey_dir_name="/sdcard/monkey_"+date_now;
				System.out.println(monkey_dir_name);
				//去除logging
				for (int i = 0; i < pkg.size(); i++) {
					if(pkg.get(i).toString().equals("com.mediatek.mtklogger")){
						pkg.remove(i);
					}
				}
				System.out.println(monkey_dir_name);
				//去除黑名单的包名
				HashSet hs1 = new HashSet(pkg);
		        HashSet hs2 = new HashSet(black_pkg);
		        hs1.removeAll(hs2);
		        List<String> listC = new ArrayList<String>();
		        listC.addAll(hs1);
		        pkg=listC;
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
				long totalPersent=touchPersent+motionPersent+trackballPersent+navPersent+turnonPersent+mainnavPersent+systemkeyPersent+appswitchPersent+keyboardPersent+otherPersent;
				if(totalPersent>100){
					showdialog("事件总比例不能超过100%");
					return;
				}
				monkey=new MonkeyString(pkg, level, testcount, event_space, seed, crash_goon, anr_goon, exception_stay, lising_code, generate_report, touchPersent, motionPersent, trackballPersent, navPersent, turnonPersent, mainnavPersent, systemkeyPersent, appswitchPersent, keyboardPersent, otherPersent);
				monkey.setPkg(pkg);
				System.out.println("monkey的实体参数为："+monkey.toString());
				//生成monkey命令
				final StringBuilder sb =new StringBuilder();
				sb.append(" monkey ");
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
				//日志路径
				sb.append(" 2>"+monkey_dir_name+"/monkey_error.txt 1>"+monkey_dir_name+"/monkey_info.txt");
				
				System.out.println(sb.toString());
				//运行时的路径
				final File f = new File(this.getClass().getResource("/").getPath());
				File shfile=FileUtil.file(f+"/monkey.sh");
				if(shfile.exists()){
					shfile.delete();
				}
				FileWriter fw=new FileWriter(FileUtil.file(f+"/monkey.sh"));
				fw.append("#!/usr/bin/env bash \n");
				fw.append(sb.toString());
				TextUtil.insertDocument("正在启动monkey...", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							CmdUtil.excuteCMDCommand_str("adb shell rm -rf /data/local/tmp/monkey.sh");
							CmdUtil.excuteCMDCommand_str("adb push "+f+"/monkey.sh /data/local/tmp/");
							CmdUtil.excuteCMDCommand_str("adb shell sh /data/local/tmp/monkey.sh&");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				new Thread(new  Runnable() {
					public void run() {
						try {
							TextUtil.insertDocument("初始化...", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
							Thread.sleep(5000);
							TextUtil.insertDocument("初始化完成!", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
							String result=CmdUtil.excuteCMDCommand_str("adb shell ps -A|findstr monkey");
							if(!"".equals(result)){
								TextUtil.insertDocument("启动monkey成功!", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								TextUtil.insertDocument("测试时间"+txt_testcount.getText()+"小时...", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								Thread.sleep(800);
								TextUtil.insertDocument("日志等级"+combox_level.getSelectedItem().toString()+"...", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								Thread.sleep(800);
								TextUtil.insertDocument("monkeylog文件路径:【"+monkey_dir_name+"】", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								Thread.sleep(800);
								TextUtil.insertDocument("测试应用", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								for (int i = 0; i < pkg.size(); i++) {
									TextUtil.insertDocument(pkg.get(i).toString(), ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
									Thread.sleep(100);
								}
								Thread.sleep(800);
								TextUtil.insertDocument("黑名单应用", ColorEnum.CHOCPLATECOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								for (int j = 0; j < black_pkg.size(); j++) {
									TextUtil.insertDocument(black_pkg.get(j).toString(), ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
									Thread.sleep(100);
								}
								return;
							}else{
								TextUtil.insertDocument("启动monkey失败，可能选择的包不支持!", ColorEnum.ERRORCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
								return;
							}
						} catch (InterruptedException e) {
							return;
						}
					}
				}).start();
			}
		});
		btn_monkeyoffline.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_monkeyoffline.setBounds(366, 294, 126, 23);
		add(btn_monkeyoffline);
		
		JButton btn_choice_log_path = new JButton("选择log保存路径");
		btn_choice_log_path.setBounds(20, 294, 131, 23);
		add(btn_choice_log_path);
		btn_choice_log_path.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择文件夹");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					choice_logpath=fileChooser.getSelectedFile().getPath().trim();
					System.out.println(choice_logpath);
					TextUtil.insertDocument("选择的路径为:"+choice_logpath, ColorEnum.INFOCOLOR.getColor(),txt_show ,ColorEnum.ERRORCOLOR.getColor());
				}
			}
		});
		btn_choice_log_path.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		JButton btn_search_offLog = new JButton("离线monkey日志");
		btn_search_offLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(monkey_dir_name==null ||"".equals(monkey_dir_name)){
					showdialog("查看错误,可能无log文件，请手动去sdcard下查看。");
					return;
				}
				TextUtil.insertDocument("离线log文件夹路径:【"+monkey_dir_name+"】\n"
						+ "因为每个人电脑权限可能不一样，可能导出失败，请手动导出.", ColorEnum.SUCCESSCOLOR.getColor(), txt_show, ColorEnum.ERRORCOLOR.getColor());
			}
		});
		btn_search_offLog.setHorizontalAlignment(SwingConstants.LEFT);
		btn_search_offLog.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_search_offLog.setBounds(495, 294, 126, 23);
		add(btn_search_offLog);
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

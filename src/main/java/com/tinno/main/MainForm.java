package com.tinno.main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jb2011.lnf.beautyeye.BeautyEyeLookAndFeelWin;
import org.jb2011.lnf.beautyeye.ch2_tab.BETabbedPaneUI;
import org.jb2011.lnf.beautyeye.ch6_textcoms.BEEditorPaneUI;

import com.tinno.pojo.Apk;
import com.tinno.utils.CmdUtil;
import com.tinno.utils.DateUtil;
import com.tinno.utils.FileUtil;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.JTree;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import java.awt.Button;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainForm extends JFrame {
	private JTextField txt_filepath;
	private JTextField txt_num;
	private JRadioButton rd_install;
	private JRadioButton rd_uninstall;
	private JRadioButton rd_install_uninstall;
	private JScrollPane jsp;
	private JPanel panel_top;
	private JTextPane txt_show;
	private final JButton btn_start;
	private final JButton btn_stop;
	// 当前项目路径
	private String courseFile;
	// 选择的文件夹路径或者文件路径
	private String choicepath;
	private Color errorcolor = Color.RED;// 红色
	private Color infocolor = Color.BLUE;// 蓝色
	private Color okcolor = new Color(0, 128, 0);// 绿色
	private Color chocolate = new Color(210, 105, 30);// 巧克力
	private Map<String, Apk> apkinfos = null;
	private String choice_radiobtn;// 选择的单选按钮
	private boolean iscoldInstall;// 冷启动
	private boolean isOpen_afterinstall;// 是否打开
	private int choice_num;// 输入的次数
	private volatile boolean isstop = false;// 是否停止
	private boolean isinstalling = false;// 是否查询所有apk文件OK
	private boolean isinstallok = false;// 是否安装成功
	private boolean isuninstallok = false;// 是否卸载成功
	private boolean isapkOpenok = false;// 是否启动成功
	private boolean islistfileok = false;// 是否查询所有apk文件OK
	private Thread listFile_thread;
	private Thread install_thread;
	private Thread uninstall_thread;
	private Thread install_uninstall_thread;
	private int count = 1;
	private Date starttime;
	private Date stoptime;
	private long onece_second = 5 * 1000;// 5s

	private JTabbedPane jTabbedpane = new JTabbedPane();// 存放选项卡的组件
	private String[] tabNames = { "应用相关", "小工具","Monkey","Excel","cmd集合" };
	private JCheckBox check_cold_install;
	private JCheckBox check_open_after_install;
	private final String ISTALL_PKG = "com.github.uiautomator";
	private JMenuBar menuBar;
	private JMenuItem menu_about;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.put("RootPane.setupButtonVisible",false);
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
				} catch (Exception e) {
					// TODO exception
				}
				try {
					if (CmdUtil.checkAdb() && CmdUtil.checkDevice_isconn()) {
						MainForm frame = new MainForm();
						// 居中显示
						frame.setLocationRelativeTo(null);
						// logo
						Image frame_icon = Toolkit.getDefaultToolkit()
								.getImage(this.getClass().getResource("logo.png"));
						frame.setIconImage(frame_icon);
						frame.setVisible(true);
						CmdUtil.makeScreenOn();
					} else {
						JOptionPane.showMessageDialog(null, "请检查设备是否打开USB调试,adb环境是否OK。", "提示",
								JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public MainForm() throws IOException {
		setResizable(false);
		setTitle("工具tools_V2.0");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				int op = JOptionPane.showConfirmDialog(MainForm.this, "你确定要退出吗?", "提示", JOptionPane.OK_CANCEL_OPTION);
				// System.out.println(op);
				if (op == JOptionPane.OK_OPTION) {
					System.exit(0);
				} else {
					return;
				}
			}
		});
		setBounds(100, 100, 974, 640);
		getContentPane().setLayout(null);

		/*
		 * tab面板
		 * 
		 * 
		 * 
		 * 
		 */
		int i = 0;
		JPanel jpanelFirst = new JPanel();
		jTabbedpane.setBackground(Color.WHITE);
		jTabbedpane.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		// jTabbedpane.addTab(tabNames[i++],icon,creatComponent(),"first");//加入第一个页面
		jTabbedpane.addTab(tabNames[i++], null, jpanelFirst, "爱屁屁");
		jTabbedpane.setMnemonicAt(0, KeyEvent.VK_0);// 设置第一个位置的快捷键为0
		// 第二个标签下的JPanel
		JPanel monkeyPanel = new MonkeyPanel();
		jTabbedpane.addTab(tabNames[i++], null, monkeyPanel, "|•ˇ₃ˇ•。)");
		//第三个面板，monkey测试
		JPanel monkeytest=new MonkeyTestPanel();
		monkeyPanel.setBounds(100, 100, 974, 640);
		jTabbedpane.addTab(tabNames[i++], null, monkeytest, "monkey测试");
		//第四个ExcelPanel
		JPanel excelPanel=new ExcelPanel();
		jTabbedpane.addTab(tabNames[i++], null, excelPanel, "Excel操作~");
		//第五个cmdPanel
		JPanel cmdPanel=new CmdPanel();
		jTabbedpane.addTab(tabNames[i++], null, cmdPanel, "cmd相关操作~");
		
		jTabbedpane.setMnemonicAt(1, KeyEvent.VK_1);// 设置快捷键为1
		getContentPane().add(jTabbedpane);
		getContentPane().setLayout(new GridLayout(1, 1));
		jpanelFirst.setLayout(null);

		jsp = new JScrollPane();
		jsp.setBounds(10, 217, 905, 259);
		jsp.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		// 把滚动条添加到容器里面
		jpanelFirst.add(jsp);

		// 文件路径
		File directory = new File("");// 参数为空
		courseFile = directory.getCanonicalPath();
		// 边框
		Border border_buttom = BorderFactory.createBevelBorder(1);
		Border border_msg = BorderFactory.createTitledBorder(border_buttom, "信息输出");
		jsp.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "信息输出", TitledBorder.LEFT,
				TitledBorder.TOP, null, new Color(0, 0, 0)));

		txt_show = new JTextPane();
		jsp.setViewportView(txt_show);
		// 让滚动条始终在下(显示最新信息)
		DefaultCaret caret = (DefaultCaret) txt_show.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		panel_top = new JPanel();
		panel_top.setBounds(10, 23, 905, 45);
		panel_top.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_top.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "app选择",
				TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		jpanelFirst.add(panel_top);
		panel_top.setLayout(null);

		final JButton btn_choicefile = new JButton("选择文件夹");
		btn_choicefile.setBounds(685, 13, 142, 23);
		panel_top.add(btn_choicefile);
		btn_choicefile.setFont(new Font("微软雅黑", Font.PLAIN, 12));

		txt_filepath = new JTextField();
		txt_filepath.setBounds(119, 14, 556, 21);
		panel_top.add(txt_filepath);
		txt_filepath.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		txt_filepath.setForeground(new Color(0, 0, 0));
		txt_filepath.setText(courseFile);

		JLabel lbl_tips = new JLabel("文件路径:");
		lbl_tips.setBounds(33, 17, 65, 15);
		panel_top.add(lbl_tips);
		lbl_tips.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		// 选择文件点击事件
		btn_choicefile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 选择文件
				// System.out.println(courseFile);
				JFileChooser fileChooser = new JFileChooser(courseFile);
				fileChooser.setDialogTitle("选择文件夹");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					choicepath = fileChooser.getSelectedFile().getPath();
					// System.out.println("选择的路径: " + choicepath);
					txt_show.setText("");
					insertDocument("选择的文件夹路径是:" + choicepath, chocolate);
					txt_filepath.setText(choicepath);
					courseFile = choicepath;
					// 获取路径下的apk,而且获取包名
					// 获取选择的文件夹路径，遍历得到apk文件
					if ("".equals(choicepath)) {
						JOptionPane.showMessageDialog(null, "未选择路径，或者路径为空!", "提示", JOptionPane.ERROR_MESSAGE);
						insertDocument("未选择路径，或者路径为空,操作取消!", errorcolor);
						return;
					} else if (choicepath == null || "".equals(choicepath)) {
						insertDocument("请选择文件夹!", errorcolor);
						return;
					} else {
						final File file = new File(choicepath);
						if (!file.isDirectory()) {
							// 不是文件夹
							JOptionPane.showMessageDialog(null, "你选择的不是文件夹!", "提示", JOptionPane.WARNING_MESSAGE);
							return;
						}
						apkinfos = new HashMap<>();
						insertDocument("正在查询路径下apk,请稍后...", chocolate);
						listfileing();
						listFile_thread = new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									// Thread.sleep(2000);
									btn_choicefile.setEnabled(false);
									FileUtil.getApkInfos(file, apkinfos);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									btn_choicefile.setEnabled(true);
									listfileed();
								}
								if (apkinfos == null || apkinfos.size() == 0) {
									insertDocument("文件夹下没有找到apk文件...", errorcolor);
									btn_choicefile.setEnabled(true);
									listfileed();
									return;
								} else {
									// 获取apk信息
									btn_choicefile.setEnabled(true);
									insertDocument("你选择的文件夹下遍历得到以下" + apkinfos.size() + "个apk信息:", okcolor);
									for (Map.Entry<String, Apk> en : apkinfos.entrySet()) {
										insertDocument("" + en.getKey() + "(" + en.getValue().getLaBle() + ")",
												infocolor);
									}
									listfileed();
								}
							}
						});
						listFile_thread.start();
					}
				}

			}
		});
		Border border_top = BorderFactory.createBevelBorder(2);

		JPanel panel_setting = new JPanel();
		panel_setting.setBounds(10, 91, 911, 90);
		panel_setting.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_setting.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u8BBE\u7F6E\u533A", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		jpanelFirst.add(panel_setting);

		// 按钮组，让单选按钮只能选择一个
		final ButtonGroup buttonGroup = new ButtonGroup();
		panel_setting.setLayout(null);
		JPanel panel_install_uninstall = new JPanel();
		panel_install_uninstall.setBounds(10, 22, 469, 58);
		panel_setting.add(panel_install_uninstall);
		panel_install_uninstall.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		rd_install = new JRadioButton("安装");
		rd_install.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		rd_install.setSelected(true);
		panel_install_uninstall.add(rd_install);

		rd_uninstall = new JRadioButton("卸载");
		rd_uninstall.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_install_uninstall.add(rd_uninstall);

		rd_install_uninstall = new JRadioButton("安装并卸载(性能)");
		rd_install_uninstall.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_install_uninstall.add(rd_install_uninstall);

		buttonGroup.add(rd_install);
		buttonGroup.add(rd_uninstall);
		buttonGroup.add(rd_install_uninstall);

		check_cold_install = new JCheckBox("冷安装");
		check_cold_install.setSelected(true);
		check_cold_install.setToolTipText("如果发现设备上已经安装了准备安装的应用，先卸载然后安装。");
		check_cold_install.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_install_uninstall.add(check_cold_install);

		check_open_after_install = new JCheckBox("安装后打开");
		check_open_after_install.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				// 选中后判断是否安装自动点击软件
				if (check_open_after_install.isSelected()) {
					// 如果安装了
					if (CmdUtil.check_isappInstalled(ISTALL_PKG)) {
						return;
					} else {
						// 安装
						installautoapk();
					}
				}
			}
		});
		check_open_after_install.setToolTipText("性能测试中，全部安装完成后打开应用，然后执行后续操作。");
		check_open_after_install.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_install_uninstall.add(check_open_after_install);

		JPanel panel_btn = new JPanel();
		panel_btn.setBounds(489, 22, 412, 58);
		panel_setting.add(panel_btn);
		panel_btn.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_btn.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel = new JLabel("输入次数:");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_btn.add(lblNewLabel);

		txt_num = new JTextField();
		// 限制输入只能是数字
		txt_num.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				if (ch < '0' || ch > '9')
					e.consume();
			}
		});
		txt_num.setText("");
		panel_btn.add(txt_num);
		txt_num.setColumns(10);

		btn_start = new JButton("开始");

		btn_start.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_btn.add(btn_start);

		btn_stop = new JButton("停止");
		btn_stop.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_btn.add(btn_stop);

		JButton btn_clear = new JButton("清空显示信息");
		btn_clear.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel_btn.add(btn_clear);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("设置");
		menuBar.add(mnNewMenu);
		
		menu_about = new JMenuItem("关于");
		menu_about.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String message="天珑物联网_自动化测试\n"
						+ "By_shusen(2020_05)";
				JOptionPane.showMessageDialog(null, message);
			}
		});
		mnNewMenu.add(menu_about);
		// 点击开始按钮事件
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// choicepath="";
				// 获取设置区域的选择信息
				// 获取选中按钮
				Enumeration<AbstractButton> rds = buttonGroup.getElements();
				while (rds.hasMoreElements()) {
					AbstractButton modelbtn = rds.nextElement();
					if (modelbtn.isSelected()) {
						// System.out.println(modelbtn.getText());
						choice_radiobtn = modelbtn.getText();
						break;
					}
				}
				// 获取冷安装，打开复选框的值
				iscoldInstall = check_cold_install.isSelected();
				isOpen_afterinstall = check_open_after_install.isSelected();
				if (isOpen_afterinstall) {
					// 选中却没安装
					if (!CmdUtil.check_isappInstalled(ISTALL_PKG)) {
						installautoapk();
						return;
					}
				}
				/*
				 * if(true){ return; }
				 */

				// 开始
				switch (choice_radiobtn) {
				case "安装":
					if (apkinfos == null) {
						insertDocument("亲,我不知道安装啥,你是不是没选择存放apk的文件夹-_-!", errorcolor);
						return;
					}
					if (apkinfos.isEmpty()) {
						insertDocument("没找到apk...无法安装。", errorcolor);
						return;
					}
					// System.out.println(apkinfos.isEmpty());
					// 安装遍历得到的apk信息,

					install_thread = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							btn_start_installing();
							try {
								// while (isstop) {
								// 安装前卸载选中
								if (iscoldInstall) {
									insertDocument("你选择了冷安装,先卸载设备上的软件...", errorcolor);
									for (Entry<String, Apk> en : apkinfos.entrySet()) {
										if (!isstop) {
											// System.out.println("停止ok");
											return;
										}
										insertDocument("正在卸载" + en.getValue().getLaBle() + "...", chocolate);
										isuninstallok = CmdUtil
												.excuteCMDCommand("adb uninstall " + en.getValue().getPkgName());
										isapk_uninstallok(isuninstallok, en.getValue().getLaBle());
									}
								}
								// 开始安装
								starttime = new Date();
								for (Map.Entry<String, Apk> en : apkinfos.entrySet()) {
									if (!isstop) {
										// System.out.println("停止");
										return;
									}
									insertDocument("正在安装" + en.getValue().getLaBle() + "...", chocolate);
									isinstallok = CmdUtil
											.excuteCMDCommand("adb install " + en.getValue().getApkLocation());
									isapk_installok(isinstallok, en.getValue().getLaBle());
								}
								stoptime = new Date();
								btn_start_reset();
								insertDocument("安装操作完成,耗时" + DateUtil.TimeConsume(starttime, stoptime) + "\t√",
										okcolor);
								// }
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								btn_start_reset();
							} finally {
								btn_start_reset();
							}
						}
					});
					install_thread.start();
					break;
				case "卸载":
					if (apkinfos == null) {
						insertDocument("亲,我不知道卸载啥,你是不是还没安装-_-!", errorcolor);
						return;
					}
					uninstall_thread = new Thread(new Runnable() {
						@Override
						public void run() {
							btn_start_uninstalling();
							try {
								// while (isstop) {
								starttime = new Date();
								for (Entry<String, Apk> en : apkinfos.entrySet()) {
									if (!isstop) {
										// System.out.println("停止ok");
										return;
									}
									insertDocument("正在卸载" + en.getValue().getLaBle() + "...", chocolate);
									isuninstallok = CmdUtil
											.excuteCMDCommand("adb uninstall " + en.getValue().getPkgName());
									isapk_uninstallok(isuninstallok, en.getValue().getLaBle());
								}
								stoptime = new Date();
								btn_start_reset();
								insertDocument("卸载操作完成,耗时" + DateUtil.TimeConsume(starttime, stoptime) + "\t√",
										okcolor);
								// }
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								btn_start_reset();
							} finally {
								btn_start_reset();
							}
						}
					});
					uninstall_thread.start();

					break;
				case "安装并卸载(性能)":
					// 获取次数
					try {
						choice_num = Integer.parseInt(txt_num.getText());
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "请输入次数", "提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if (apkinfos == null) {
						insertDocument("亲,我不知道安装啥,你是不是没选择存放apk的文件夹-_-!", errorcolor);
						return;
					}
					if (apkinfos.isEmpty()) {
						insertDocument("没找到apk...无法安装。", errorcolor);
						return;
					}
					if (count != 1) {
						count = 1;
					}
					install_uninstall_thread = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							btn_start_insAndunins();
							try {
								// 安装前卸载选中
								if (iscoldInstall) {
									insertDocument("你选择了冷安装,安装前先卸载...", errorcolor);
									for (Entry<String, Apk> en : apkinfos.entrySet()) {
										if (!isstop) {
											// System.out.println("停止ok");
											return;
										}
										insertDocument("正在卸载" + en.getValue().getLaBle() + "...", chocolate);
										isuninstallok = CmdUtil
												.excuteCMDCommand("adb uninstall " + en.getValue().getPkgName());
										isapk_uninstallok(isuninstallok, en.getValue().getLaBle());
									}
								}
								starttime = new Date();
								while (isstop) {
									if (choice_num <= 0) {
										stoptime = new Date();
										btn_start_reset();
										if (isOpen_afterinstall) {// 如果选择了打开就计算时差
											// System.out.println("算时差");
											long losetime = apkinfos.size() * onece_second;
											// System.out.println(losetime);
											insertDocument("测试完成,耗时"
													+ DateUtil.TimeConsume_havelose(starttime, stoptime, losetime)
													+ "\t√", okcolor);
											JOptionPane
													.showMessageDialog(null,
															"测试完成,耗时" + DateUtil.TimeConsume_havelose(starttime,
																	stoptime, losetime),
															"提示", JOptionPane.DEFAULT_OPTION);
										} else {
											insertDocument(
													"测试完成,耗时" + DateUtil.TimeConsume(starttime, stoptime) + "\t√",
													okcolor);
											JOptionPane.showMessageDialog(null,
													"测试完成,耗时" + DateUtil.TimeConsume(starttime, stoptime), "提示",
													JOptionPane.DEFAULT_OPTION);
										}
										return;
									}
									// System.out.println(count+","+choice_num);
									insertDocument("开始第" + count + "轮安装卸载...", errorcolor);

									for (Entry<String, Apk> en : apkinfos.entrySet()) {
										if (!isstop) {
											// System.out.println("停止");
											return;
										}
										insertDocument("正在安装" + en.getValue().getLaBle() + "...", chocolate);
										isinstallok = CmdUtil
												.excuteCMDCommand("adb install " + en.getValue().getApkLocation());
										isapk_ins_uninstallok(true, isinstallok, en.getValue().getLaBle());
									}
									if (isOpen_afterinstall) {
										insertDocument("===安装完成，你选择了打开，准备打开apk...===", errorcolor);
										for (Entry<String, Apk> en : apkinfos.entrySet()) {
											if (!isstop) {
												// System.out.println("停止");
												return;
											}
											insertDocument("正在打开" + en.getValue().getLaBle() + "...", chocolate);
											isapkOpenok = CmdUtil.startAppBystartBypkg(en.getValue().getPkgName());
											Thread.sleep(onece_second);
											isapkOpenedok(isapkOpenok, en.getValue().getLaBle());
										}
									}
									insertDocument("全部打开完成,开始卸载...", chocolate);
									for (Entry<String, Apk> en : apkinfos.entrySet()) {
										if (!isstop) {
											// System.out.println("停止");
											return;
										}
										insertDocument("正在卸载" + en.getValue().getLaBle() + "...", chocolate);
										isuninstallok = CmdUtil
												.excuteCMDCommand("adb uninstall " + en.getValue().getPkgName());
										isapk_ins_uninstallok(false, isinstallok, en.getValue().getLaBle());
									}
									insertDocument("第" + count + "轮测试结束", okcolor);
									count++;
									choice_num--;
								}
							} catch (Exception e) {
								e.printStackTrace();
								btn_start_reset();
							} finally {
								btn_start_reset();
							}
						}
					});
					install_uninstall_thread.start();
					break;
				default:
					break;
				}
			}
		});

		// 暂停按钮点击事件
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isstop && install_thread == null && uninstall_thread == null && install_uninstall_thread == null) {
					insertDocument("*****没有正在操作的任务*****", chocolate);
					return;
				}
				if (install_thread != null) {
					install_thread = null;
				}
				if (uninstall_thread != null) {
					uninstall_thread = null;
				}
				if (install_uninstall_thread != null) {
					install_uninstall_thread = null;
				}
				btn_start_reset();
				insertDocument("*****操作成功停止*****", errorcolor);
			}
		});
		// 清空按钮点击事件
		btn_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// getlocaluiapk();
				// CmdUtil.startAppBystartBypkg("com.qiyi.video");
				if ("".equals(txt_show.getText())) {
					return;
				}
				txt_show.setText("");
			}
		});

	}

	// 是否安装或卸载OK
	private void isapk_installok(boolean result, String show) {
		if (isinstallok) {
			insertDocument("安装" + show + "完成^_^", chocolate);
		} else {
			insertDocument("安装" + show + "失败-_-", chocolate);
		}
	}

	// 是否卸载OK
	private void isapk_uninstallok(boolean result, String show) {
		if (isinstallok) {
			insertDocument("卸载" + show + "完成^_^", chocolate);
		} else {
			insertDocument("卸载" + show + "失败-_-", chocolate);
		}
	}

	// 是否打开OK
	private void isapkOpenedok(boolean result, String show) {
		if (isinstallok) {
			insertDocument("打开" + show + "完成^_^", chocolate);
		} else {
			insertDocument("打开" + show + "失败-_-", chocolate);
		}
	}

	// 是否安装或卸载OK
	private void isapk_ins_uninstallok(boolean insOrunins/* true安装，false卸载 */, boolean result, String show) {
		;
		switch (insOrunins ? "install" : "uninstall") {
		case "install":
			if (isinstallok) {
				insertDocument("安装" + show + "完成^_^", chocolate);
			} else {
				insertDocument("安装" + show + "失败-_-", chocolate);
			}
			break;
		case "uninstall":
			if (isinstallok) {
				insertDocument("卸载" + show + "完成^_^", chocolate);
			} else {
				insertDocument("卸载" + show + "失败-_-", chocolate);
			}
			break;

		default:
			break;
		}

	}

	private void btn_start_reset() {
		btn_start.setText("开始");
		btn_start.setEnabled(true);
		isstop = false;
	}

	private void btn_start_installing() {
		btn_start.setText("正在安装");
		btn_start.setEnabled(false);
		isstop = true;
	}

	private void btn_start_insAndunins() {
		btn_start.setText("正在努力安装卸载");
		btn_start.setEnabled(false);
		isstop = true;
	}

	private void btn_start_uninstalling() {
		btn_start.setText("正在卸载");
		btn_start.setEnabled(false);
		isstop = true;
	}

	private void listfileing() {
		btn_start.setEnabled(false);
		btn_stop.setEnabled(false);
	}

	private void listfileed() {
		btn_start.setEnabled(true);
		btn_stop.setEnabled(true);
	}

	private void installautoapk() {
		// 安装
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					File f = new File(this.getClass().getResource("/").getPath());
					if (CmdUtil.excuteCMDCommand("adb install " + f + "/uiautomator.apk")) {
						Thread.sleep(500);
						CmdUtil.startAppBystartBypkg(ISTALL_PKG);
						Thread.sleep(1000);
						JOptionPane.showMessageDialog(null, "请按照设备提示安装软件,并打开监听exe!", "提示",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private String getlocaluiapk() {
		Map<String, Apk> map = new HashMap<>();
		File f = new File("");
		String apklocation = "";
		try {
			File f1 = new File(f.getCanonicalPath());
			FileUtil.getApkInfos(f1, map);
			/*
			 * if(map.isEmpty()||map.size()<=0){ System.out.println("empty");
			 * return ""; }
			 */
			for (Entry<String, Apk> en : map.entrySet()) {
				if (ISTALL_PKG.equals(en.getValue().getApkLocation())) {
					apklocation = en.getValue().getApkLocation();
					System.out.println("路径:" + en.getValue().getApkLocation());
					return apklocation;
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}
		return apklocation;
	}

	public void insertDocument(String text, Color textColor)// 根据传入的颜色及文字，将文字插入文本域
	{
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground(set, textColor);// 设置文字颜色
		StyleConstants.setFontSize(set, 14);// 设置字体大小
		Document doc = txt_show.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), "\n" + text, set);// 插入文字
		} catch (BadLocationException e) {
			txt_show.setForeground(errorcolor);
			txt_show.setText("出现了一点问题....");
			return;
		}
	}
}

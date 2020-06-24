package com.tinno.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import com.tinno.utils.TextUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.swing.ScreenUtil;
import cn.hutool.core.swing.clipboard.ClipboardUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.system.OsInfo;
import cn.hutool.system.UserInfo;

public class MonkeyPanel extends JPanel {
	private JTextField txt_InputQrMsg;
	private String choice_path;//选择的 路径
	private String save_path;//存放 路径,仅用在图片中，后面不用 这个变量
	private JTextField txt_input_word;
	private Thread split_word_thread;
	private String resultStr;//分词结果
	private final String date_formate_role="yyyyMMddHHmmss";
	private File begin_convert_img;
	private String img_start_choice_path;
	private String com_after;
	private JTextField txt_web_url;
	private Color errorcolor = Color.RED;// 红色
	private Color infocolor = Color.BLUE;// 蓝色
	private Color okcolor = new Color(0, 128, 0);// 绿色
	private Color chocolate = new Color(210, 105, 30);// 巧克力
	/**
	 * Create the panel.
	 */
	public MonkeyPanel() {
		setLayout(null);
		
		JPanel panel_Qr = new JPanel();
		panel_Qr.setBackground(UIManager.getColor("Button.background"));
		panel_Qr.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)), "\u4E8C\u7EF4\u7801", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Qr.setBounds(34, 10, 846, 63);
		add(panel_Qr);
		panel_Qr.setLayout(null);
		
		JButton btn_creatQrCode = new JButton("生成二维码");
		btn_creatQrCode.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_creatQrCode.setBounds(343, 21, 93, 23);
		panel_Qr.add(btn_creatQrCode);
		
		JButton btn_Qr_savepath = new JButton("存放路径");
		btn_Qr_savepath.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_Qr_savepath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择文件夹");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					choice_path=fileChooser.getSelectedFile().getPath().trim();
					System.out.println(choice_path);
				}
			}
		});
		btn_Qr_savepath.setBounds(232, 21, 93, 23);
		panel_Qr.add(btn_Qr_savepath);
		
		txt_InputQrMsg = new JTextField();
		txt_InputQrMsg.setBounds(73, 14, 149, 37);
		panel_Qr.add(txt_InputQrMsg);
		txt_InputQrMsg.setColumns(10);
		
		JLabel label = new JLabel("输入信息：");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		label.setBounds(10, 25, 90, 15);
		panel_Qr.add(label);
		
		JPanel panel_img = new JPanel();
		panel_img.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u56FE\u7247\u76F8\u5173", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_img.setBounds(34, 83, 846, 123);
		add(panel_img);
		panel_img.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 19, 658, 44);
		panel_img.add(panel);
		panel.setLayout(null);
		
		JButton btn_convert_copy = new JButton("转换粘贴板的图");
		btn_convert_copy.setBounds(495, 10, 140, 23);
		panel.add(btn_convert_copy);
		btn_convert_copy.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		JButton btnNewButton = new JButton("转换选择的图");
		btnNewButton.setBounds(336, 10, 140, 23);
		panel.add(btnNewButton);
		btnNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		JButton btn_black_img_path = new JButton("存放路径");
		btn_black_img_path.setBounds(224, 10, 93, 23);
		panel.add(btn_black_img_path);
		btn_black_img_path.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		JButton btn_color_img = new JButton("选择图片");
		btn_color_img.setBounds(112, 10, 93, 23);
		panel.add(btn_color_img);
		btn_color_img.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		JLabel label_1 = new JLabel("彩图转黑白：");
		label_1.setBounds(10, 14, 72, 15);
		panel.add(label_1);
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(10, 71, 658, 44);
		panel_img.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btn_start_convert_img = new JButton("开始转换");
		btn_start_convert_img.setBounds(515, 10, 93, 23);
		panel_1.add(btn_start_convert_img);
		btn_start_convert_img.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		JButton btn_choice_convert_result = new JButton("存放路径");
		btn_choice_convert_result.setBounds(417, 10, 93, 23);
		panel_1.add(btn_choice_convert_result);
		btn_choice_convert_result.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		JButton btn_choiceConvert_img = new JButton("选择要转换的图片");
		btn_choiceConvert_img.setBounds(285, 10, 129, 23);
		panel_1.add(btn_choiceConvert_img);
		btn_choiceConvert_img.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		
		final JComboBox comboBox_convert_img_type = new JComboBox();
		comboBox_convert_img_type.setBounds(117, 10, 158, 21);
		panel_1.add(comboBox_convert_img_type);
		comboBox_convert_img_type.setModel(new DefaultComboBoxModel(new String[] {"GIF->JPG", "GIF->PNG", "PNG->JPG", "PNG->GIF", "BMP->PNG"}));
		
		JLabel label_1_1 = new JLabel("图片类型转换：");
		label_1_1.setBounds(10, 13, 84, 15);
		panel_1.add(label_1_1);
		label_1_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_choiceConvert_img.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择图片");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					img_start_choice_path=fileChooser.getSelectedFile().getPath().trim();
					String img_filetype=FileTypeUtil.getType(FileUtil.file(img_start_choice_path)).toUpperCase();
					//、、获取下拉列表值
					String com_begin=comboBox_convert_img_type.getSelectedItem().toString().split("->")[0];
					com_after=comboBox_convert_img_type.getSelectedItem().toString().split("->")[1];
//					System.out.println(img_filetype+","+com_begin+","+com_after);
					if(!com_begin.equals(img_filetype)){
						JOptionPane.showMessageDialog(null, "转换类型是"+comboBox_convert_img_type.getSelectedItem().toString()+",但是你选择的图片不是"+com_begin+"类型");
						return;
					}else{
						//获取图片
						begin_convert_img=FileUtil.file(img_start_choice_path);
					}
				}
			}
		});
		btn_choice_convert_result.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择文件夹");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					choice_path=fileChooser.getSelectedFile().getPath().trim();
//					System.out.println(choice_path);
				}
			}
		});
		btn_start_convert_img.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(img_start_choice_path==null){
					JOptionPane.showMessageDialog(null, "请选择要转换的图");
					return;
				}
				if("".equals(choice_path)||choice_path==null){
					JOptionPane.showMessageDialog(null, "请选择保存图片的路径");
					return;
				}
//				System.out.println(com_after.toLowerCase());
				try {
					String now_date=DateUtil.format(new Date(),date_formate_role);
					ImgUtil.convert(begin_convert_img, FileUtil.file(choice_path+"convert_"+now_date+"."+com_after.toLowerCase()));
					JOptionPane.showMessageDialog(null, "转换成功,图片保存在"+choice_path+"converted."+com_after.toLowerCase());
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "转换失败");
				}
			}
		});
		btn_color_img.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择图片");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					choice_path=fileChooser.getSelectedFile().getPath().trim();
					System.out.println(choice_path);
				}
			}
		});
		btn_black_img_path.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择文件夹");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					save_path=fileChooser.getSelectedFile().getPath().trim();
					System.out.println("存放路径："+save_path);
				}
				
				
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if("".equals(choice_path)|| choice_path==null){
					JOptionPane.showMessageDialog(null, "请选择图片");
					return;
				}
				if("".equals(save_path)|| save_path==null){
					JOptionPane.showMessageDialog(null, "请选择存放路径");
					return;
				}
				try {
					String date_now=DateUtil.format(new Date(), date_formate_role);
					ImgUtil.gray(FileUtil.file(choice_path), FileUtil.file(save_path+"gray_"+date_now+".png"));
					JOptionPane.showMessageDialog(null, "转换成功,图片保存在:"+save_path+"gray_"+date_now+".png");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "无法转换非图片");
					e.printStackTrace();
				}
			}
		});
		btn_convert_copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(save_path==null||"".equals(save_path)){
					JOptionPane.showMessageDialog(null, "请选择存放结果图片的路径。");
					return;
				}
				if(ClipboardUtil.getImage()==null){
					JOptionPane.showMessageDialog(null, "粘贴板没图片!");
					return;
				}
				try {
					String now_date=DateUtil.format(new  Date(), date_formate_role);
					Image img=ClipboardUtil.getImage();
					ImgUtil.gray(img, FileUtil.file(save_path+"gray_"+now_date+".png"));
					JOptionPane.showMessageDialog(null, "转换成功,图片保存在:"+save_path+"gray_"+now_date+".png");
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "转换失败");
				}
			}
		});
		
		JPanel panel_word = new JPanel();
		panel_word.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u5206\u8BCD", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_word.setBounds(34, 205, 846, 52);
		add(panel_word);
		panel_word.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("输入字符：");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 10, 65, 37);
		panel_word.add(lblNewLabel);
		
		txt_input_word = new JTextField();
		txt_input_word.setBounds(74, 10, 149, 37);
		panel_word.add(txt_input_word);
		txt_input_word.setColumns(10);
		
		JButton btn_word_savepath = new JButton("存放地址");
		btn_word_savepath.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_word_savepath.setBounds(233, 17, 93, 23);
		panel_word.add(btn_word_savepath);
		
		final JButton btn_split_word = new JButton("开始分词");
		btn_split_word.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_split_word.setBounds(346, 17, 93, 23);
		panel_word.add(btn_split_word);
		
		final JTextPane txt_show = new JTextPane();
		
		JScrollPane jsp = new JScrollPane();
		jsp.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		jsp.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "信息输出", TitledBorder.LEFT,
						TitledBorder.TOP, null, new Color(0, 0, 0)));
		jsp.setBounds(34, 347, 846, 132);
		add(jsp);
		jsp.setViewportView(txt_show);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "\u7CFB\u7EDF", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(34, 255, 846, 92);
		add(panel_2);
		panel_2.setLayout(null);
		
		JLabel label_2 = new JLabel("输入网址:");
		label_2.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		label_2.setBounds(10, 14, 54, 15);
		panel_2.add(label_2);
		
		txt_web_url = new JTextField();
		txt_web_url.setBounds(74, 11, 130, 21);
		panel_2.add(txt_web_url);
		txt_web_url.setColumns(10);
		
		JButton btn_urlToip = new JButton("转为ip");
		btn_urlToip.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_urlToip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				"https://www.baidu.com/"-->www.baidu.com
				final String hostName=txt_web_url.getText();
				if("".equals(hostName)){
					TextUtil.insertDocument("请输入网址", errorcolor, txt_show, errorcolor);
					return;
				}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String newhostName = "";
						try {
							newhostName = hostName.split("//")[1].replace("/","");
						} catch (Exception e) {
							TextUtil.insertDocument("网址不对或未查询到该网址...请输入正确网址,例如(https://www.baidu.com)", errorcolor, txt_show, errorcolor);
							return;
						}
//						System.out.println("new :"+newhostName);
						String ip=NetUtil.getIpByHost(newhostName);
//						System.out.println("ip:"+ip);
						if(ip.equals(newhostName)){
							TextUtil.insertDocument("网址不对或未查询到该网址...", errorcolor, txt_show, errorcolor);
							return;
						}else{
							TextUtil.insertDocument(ip, infocolor, txt_show, errorcolor);
						}
					}
				}).start();
				
			}
		});
		btn_urlToip.setBounds(214, 10, 93, 23);
		panel_2.add(btn_urlToip);
		
		JButton btn_getMac = new JButton("本机Mac地址");
		btn_getMac.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_getMac.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TextUtil.insertDocument("本机Mac地址为:【"+NetUtil.getLocalMacAddress()+"】", infocolor, txt_show, errorcolor);
			}
		});
		btn_getMac.setBounds(317, 10, 93, 23);
		panel_2.add(btn_getMac);
		
		JButton btn_localIp = new JButton("本机Ip");
		btn_localIp.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_localIp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TextUtil.insertDocument("Ipv4:【"+NetUtil.localIpv4s().toArray()[1].toString()+"】", infocolor, txt_show, errorcolor);
			}
		});
		btn_localIp.setBounds(420, 10, 93, 23);
		panel_2.add(btn_localIp);
		
		JButton btn_isinner_net = new JButton("是否为内网");
		btn_isinner_net.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_isinner_net.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(NetUtil.isInnerIP(NetUtil.localIpv4s().toArray()[1].toString())){
					TextUtil.insertDocument("当前Ip为【内网】", okcolor, txt_show, errorcolor);
				}else{
					TextUtil.insertDocument("当前Ip为【非内网】", infocolor, txt_show, errorcolor);
				}
			}
		});
		btn_isinner_net.setBounds(521, 10, 93, 23);
		panel_2.add(btn_isinner_net);
		
		JButton btn_os_arch = new JButton("获取系统信息");
		btn_os_arch.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_os_arch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OsInfo os=new OsInfo();
				TextUtil.insertDocument("OS架构:【"+os.getArch()+"】\n"+
						"OS的文件路径的分隔符:【"+os.getFileSeparator()+"】\n"+
						"OS的名称:【"+os.getName()+"】\n"+
						"OS的搜索路径分隔符:【"+os.getPathSeparator()+"】\n"+
						"OS的版本:【"+os.getVersion()+"】\n"
						, infocolor, txt_show, errorcolor);
			}
		});
		btn_os_arch.setBounds(624, 10, 93, 23);
		panel_2.add(btn_os_arch);
		
		JButton btn_getscreen_info = new JButton("获取屏幕分辨率");
		btn_getscreen_info.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_getscreen_info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TextUtil.insertDocument("屏幕宽高:【"+ScreenUtil.getWidth()+"x"+ScreenUtil.getHeight()+"】", chocolate, txt_show, errorcolor);
			}
		});
		btn_getscreen_info.setBounds(10, 39, 117, 23);
		panel_2.add(btn_getscreen_info);
		
		JButton btn_screen_window = new JButton("截取全屏");
		btn_screen_window.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_screen_window.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//缩放窗口（缩小窗口）
				JFrame.getFrames()[0].setExtendedState(JFrame.ICONIFIED);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(500);
							BufferedImage bufferedImage=ScreenUtil.captureScreen();
							ClipboardUtil.setImage(bufferedImage);
							JFrame.getFrames()[0].setExtendedState(JFrame.NORMAL);
							TextUtil.insertDocument("截图已经复制", okcolor, txt_show, errorcolor);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							TextUtil.insertDocument("截图生成错误", errorcolor, txt_show, errorcolor);
							JFrame.getFrames()[0].setExtendedState(JFrame.NORMAL);
						}
					}
				}).start();
				
//				setExtendedState(JFrame.ICONIFIED);
				
			}
		});
		btn_screen_window.setBounds(137, 39, 93, 23);
		panel_2.add(btn_screen_window);
		
		JButton btn_clear_txt = new JButton("清空信息");
		btn_clear_txt.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_clear_txt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if("".equals(txt_show.getText())){
					return;
				}
				txt_show.setText("");
			}
		});
		btn_clear_txt.setBounds(742, 11, 94, 72);
		panel_2.add(btn_clear_txt);
		
		JButton btn_getInfo_byUser = new JButton("获取设备用户信息");
		btn_getInfo_byUser.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_getInfo_byUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserInfo u=new UserInfo();
				TextUtil.insertDocument("当前登录用户的国家或区域设置:【"+u.getCountry()+"】\n"+
						"当前目录:【"+u.getCurrentDir()+"】\n"+
						"当前登录用户的home目录:【"+u.getHomeDir()+"】\n"+
						"当前登录用户的语言设置:【"+u.getLanguage()+"】\n"+
						"取得当前登录用户的名字:【"+u.getName()+"】\n"+
						"临时目录:【"+u.getTempDir()+"】"
						, infocolor, txt_show, errorcolor);
			}
		});
		btn_getInfo_byUser.setBounds(240, 39, 117, 23);
		panel_2.add(btn_getInfo_byUser);
		// 让滚动条始终在下(显示最新信息)
		DefaultCaret caret = (DefaultCaret) txt_show.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		btn_split_word.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if("".equals(txt_input_word.getText())||txt_input_word==null){
					JOptionPane.showMessageDialog(null, "请输入信息");
					return;
				}
				if(choice_path==null||"".equals(choice_path)){
					JOptionPane.showMessageDialog(null, "请选择存放地址");
					return;
				}
				split_word_thread=new Thread(new Runnable() {
					//自动根据用户引入的分词库的jar来自动选择使用的引擎
					TokenizerEngine engine = TokenizerUtil.createEngine();
					//解析文本
					String text = txt_input_word.getText();
					@Override
					public void run() {
						btn_split_word.setEnabled(false);
						// TODO Auto-generated method stub
						
						try {
							Result result = engine.parse(text);
							resultStr = CollUtil.join((Iterator<Word>)result, " ");
//							System.out.println(resultStr);
							String date_now=DateUtil.format(new Date(),date_formate_role);
							FileWriter writer = new FileWriter(choice_path+"split_"+date_now+".txt");
							for(String s:resultStr.split(" ")){
									writer.append(s+"\r\n");
							}
							JOptionPane.showMessageDialog(null, "分词完成,文件保存在"+choice_path+"split_"+date_now+".txt");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "分词失败");
							btn_split_word.setEnabled(true);
						}
						btn_split_word.setEnabled(true);
					}
				});
				split_word_thread.start();
			}
		});
		btn_word_savepath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择存放地址");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(getParent());
				if (JFileChooser.APPROVE_OPTION == result) {
					choice_path=fileChooser.getSelectedFile().getPath().trim();
					System.out.println(choice_path);
				}
			}
		});
		btn_creatQrCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if("".equals(txt_InputQrMsg.getText().trim())){
					JOptionPane.showMessageDialog(null, "请输入信息");
					return;
				}
				else if("".equals(choice_path)||choice_path==null){
					JOptionPane.showMessageDialog(null, "请选择地址");
					return;
				}
				try {
					String date_now=DateUtil.format(new Date(), date_formate_role);
					String Qr_img_path=choice_path+"QR_"+date_now+".jpg";
					QrCodeUtil.generate(txt_InputQrMsg.getText().trim(), 300, 300, FileUtil.file(Qr_img_path));
					JOptionPane.showMessageDialog(null, "生成二维码成功,保存路径为"+Qr_img_path+"");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "生成二维码失败!");
				}
			}
		});

	}
}

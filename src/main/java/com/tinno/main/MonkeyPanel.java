package com.tinno.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Date;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.swing.clipboard.ClipboardUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import javax.swing.border.LineBorder;

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

	/**
	 * Create the panel.
	 */
	public MonkeyPanel() {
		setLayout(null);
		
		JPanel panel_Qr = new JPanel();
		panel_Qr.setBackground(Color.WHITE);
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
		panel_word.setBounds(34, 216, 846, 52);
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

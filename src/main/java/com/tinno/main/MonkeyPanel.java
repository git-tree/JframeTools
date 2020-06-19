package com.tinno.main;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.swing.clipboard.ClipboardUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import java.awt.Font;

public class MonkeyPanel extends JPanel {
	private JTextField txt_InputQrMsg;
	private String choice_path;//选择的 路径
	private String save_path;//存放 路径,仅用在图片中，后面不用 这个变量
	private JTextField txt_input_word;
	private Thread split_word_thread;
	private String resultStr;//分词结果
	private final String date_formate_role="yyyyMMddHHmmss";

	/**
	 * Create the panel.
	 */
	public MonkeyPanel() {
		setLayout(null);
		
		JPanel panel_Qr = new JPanel();
		panel_Qr.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u4E8C\u7EF4\u7801", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Qr.setBounds(10, 10, 565, 63);
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
		panel_img.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5F69\u56FE\u8F6C\u9ED1\u767D", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_img.setBounds(10, 79, 565, 52);
		add(panel_img);
		panel_img.setLayout(null);
		
		JButton btn_color_img = new JButton("选择图片");
		btn_color_img.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_color_img.setBounds(10, 19, 93, 23);
		panel_img.add(btn_color_img);
		
		JButton btn_black_img_path = new JButton("存放路径");
		btn_black_img_path.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_black_img_path.setBounds(122, 19, 93, 23);
		panel_img.add(btn_black_img_path);
		
		JButton btnNewButton = new JButton("转换选择的图");
		btnNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btnNewButton.setBounds(234, 19, 140, 23);
		panel_img.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("转换粘贴板的图");
		btnNewButton_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btnNewButton_1.addActionListener(new ActionListener() {
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
		btnNewButton_1.setBounds(393, 19, 140, 23);
		panel_img.add(btnNewButton_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5206\u8BCD", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 141, 565, 52);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("输入字符：");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 10, 65, 37);
		panel.add(lblNewLabel);
		
		txt_input_word = new JTextField();
		txt_input_word.setBounds(74, 10, 149, 37);
		panel.add(txt_input_word);
		txt_input_word.setColumns(10);
		
		JButton btn_word_savepath = new JButton("存放地址");
		btn_word_savepath.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_word_savepath.setBounds(233, 17, 93, 23);
		panel.add(btn_word_savepath);
		
		final JButton btn_split_word = new JButton("开始分词");
		btn_split_word.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btn_split_word.setBounds(346, 17, 93, 23);
		panel.add(btn_split_word);
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

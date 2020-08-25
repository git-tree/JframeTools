package com.tinno.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

public class TextFileUtil {

	/**
	 * 对文本文件的关键词进行搜索
	 *
	 */
	public static class TextFileSearch {

		public List<String> SearchKeyword(File file, String keyword) {
			// 参数校验
			verifyParam(file, keyword);

			// 行读取
			LineNumberReader lineReader = null;
			List<String> l = CollUtil.newArrayList();
			try {
				lineReader = new LineNumberReader(new FileReader(file));
				String readLine = null;

				while ((readLine = lineReader.readLine()) != null) {
					// 判断每一行中,出现关键词的次数
					int index = 0;
					int next = 0;
					int times = 0;// 出现的次数
					// 判断次数
					while ((index = readLine.indexOf(keyword, next)) != -1) {
						next = index + keyword.length();
						times++;
					}
					if (times > 0) {
						// System.out.println("第" + lineReader.getLineNumber() +
						// "行" + "出现 " + keyword + " 次数: " + times);
						l.add("第" + lineReader.getLineNumber() + "行" + "出现 " + keyword + " 次数: " + times);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭流
				close(lineReader);
			}
			return l;
		}

		/**
		 * 参数校验
		 * 
		 */
		private void verifyParam(File file, String keyword) {
			// 对参数进行校验证
			if (file == null) {
				throw new NullPointerException("the file is null");
			}
			if (keyword == null || keyword.trim().equals("")) {
				throw new NullPointerException("the keyword is null or \"\" ");
			}

			if (!file.exists()) {
				throw new RuntimeException("the file is not exists");
			}
			// 非目录
			if (file.isDirectory()) {
				throw new RuntimeException("the file is a directory,not a file");
			}

			// 可读取
			if (!file.canRead()) {
				throw new RuntimeException("the file can't read");
			}
		}

		/**
		 * 关闭流
		 */
		private void close(Closeable able) {
			if (able != null) {
				try {
					able.close();
				} catch (IOException e) {
					e.printStackTrace();
					able = null;
				}
			}
		}

	}

	public static void main(String[] args) throws IOException {

		// 1、获取map 的key，作为excel的行列，后面更新为读取关键字
		FileReader fr = new FileReader(FileUtil.file("C:/Users/shusen.cui/Desktop/monkey_report/monkey_keywords.txt"));
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		// 关键字集合,读取关键字文本，或者界面上可操作，动态添加。
		List<String> keywords_list = CollUtil.newArrayList();
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			keywords_list.add(line);
		}
		// System.out.println(keywords_list.toString());
		// [ANR, Exceptio, Null, Error, CRASH]
		
		// 2、通过方法获取每种异常的集合
		// excel行数据map
		ArrayList<Map<String, Object>> rows = CollUtil.newArrayList();
		boolean testresult=true;
		TextFileSearch search = new TextFileSearch();
		List<Integer> sortlist = CollUtil.newArrayList();
		List<List<String>> allmsglist=CollUtil.newArrayList();
		
		for (int i = 0; i < keywords_list.size(); i++) {
			List<String> msglist=search.SearchKeyword(FileUtil.file("C:/Users/shusen.cui/Desktop/monkey_report/monkey_error.txt"),keywords_list.get(i).toString());
			sortlist.add(msglist.size());
			allmsglist.add(msglist);
			if(msglist.size()!=0){
				testresult=false;
			}
		}
		if(testresult){
			Map<String, Object> row=new HashMap<String, Object>();
			row.put("测试结果", "PASS");
			rows.add(row);
		}else{
			// 排序，找到最大数，然后作为最大行数
			Collections.sort(sortlist);
			for (int j = 0; j < sortlist.get(sortlist.size() - 1); j++) {
				Map<String, Object> row = new HashMap<String, Object>();
				for (int k = 0; k < keywords_list.size(); k++) {
					String value;
					try {
						value=allmsglist.get(k).get(j).toString();
					} catch (Exception e) {
						// TODO Auto-generated catch block
//						发生数组越界异常,就给那个单元格设置内容为空，
						value="";
					}
					row.put(keywords_list.get(k).toString(),value);
				}
				rows.add(row);
			}
		}
		
//		生成的表格格式大概如下:
		/*
		
						|Exception|Error|Anr|Crash|
						———————————————————————————
						|xxxxxxxxx|xxxx |xx | xx  |
						———————————————————————————
						|xx       |     |x  |   x |
						———————————————————————————
						|         |     |   |   x |
						———————————————————————————
						
		*/
		// 通过工具类创建writer
		ExcelWriter writer = ExcelUtil.getWriter("e:/monkey_Result.xlsx");
		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(keywords_list.size()-1, "monkey测试结果(若有异常,异常详情可用notepad++打开error.txt定位行数查看具体异常)");
		// 一次性写出内容，使用默认样式，强制输出标题
		writer.write(rows, true);
		// 关闭writer，释放内存
		writer.close();
	}
}

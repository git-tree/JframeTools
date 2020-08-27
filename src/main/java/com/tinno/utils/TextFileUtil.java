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

		public Map<String,List<String>> SearchKeyword(File file, String keyword) {
			// 参数校验
			verifyParam(file, keyword);

			// 行读取
			LineNumberReader lineReader = null;
			List<String> l = CollUtil.newArrayList();
			List<String> count=CollUtil.newArrayList();
			Map<String, List<String>> map=CollUtil.newHashMap();
			String pkg="";
			String activity="";
			String exception="";
			int beginnum=0;
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
						switch(keyword){
						case "ANR":
							pkg=readLine.split(" ")[2];
							activity=readLine.split(" ")[3];
							l.add("包名:"+pkg+" ,\nActivity:"+activity+ " ,\n行号:"+lineReader.getLineNumber());
							count.add(keyword+"--"+pkg);
							beginnum=lineReader.getLineNumber();
							break;
						case "Exception":
//							exception=readLine.split(" ")[3];
							if(lineReader.getLineNumber()-beginnum<=3){
								l.add("exception:\n"+readLine+ " ,\n行号:"+lineReader.getLineNumber());
							}
							else{
								beginnum=lineReader.getLineNumber();
							}
//							count.add(keyword+"--"+readLine);
							break;
						case "Null":
							l.add("第" + lineReader.getLineNumber() + "行" + "出现 " + keyword + " 次数: " + times);
							beginnum=lineReader.getLineNumber();
							break;
						case "Error":
							l.add("第" + lineReader.getLineNumber() + "行" + "出现 " + keyword + " 次数: " + times);
							beginnum=lineReader.getLineNumber();
							break;
						case "CRASH":
							pkg=readLine.split(" ")[2];
							l.add("包名:"+pkg+" ,\n行号:"+lineReader.getLineNumber());
							count.add(keyword+"--"+pkg);
							beginnum=lineReader.getLineNumber();
							break;
							default:
								System.out.println("无关键字");
								beginnum=lineReader.getLineNumber();
								break;
						}
						
//						 System.out.println("第" + lineReader.getLineNumber() +
//						 "行" + "出现 " + keyword + " 次数: " + times+"\tpkg:"+pkg);
//						l.add("第" + lineReader.getLineNumber() + "行" + "出现 " + keyword + " 次数: " + times+" pkg:"+pkg);
//						 l.add("包名:"+pkg+" ,行号:"+lineReader.getLineNumber());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭流
				close(lineReader);
			}
			map.put("result", l);
			map.put("count", count);
			return map;
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
}

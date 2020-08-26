package com.tinno.utils;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tinno.utils.TextFileUtil.TextFileSearch;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

public class Reportutils {
	public static void reportmonkey_result(String monkey_dir_name){
		// 1、获取map 的key，作为excel的行列，后面更新为读取关键字
		java.io.FileReader fr=null;
		// 关键字集合,读取关键字文本，或者界面上可操作，动态添加。
		List<String> keywords_list = CollUtil.newArrayList();
		try {
			File directory = new File("");// 参数为空
			String courseFile = directory.getCanonicalPath();
			fr = new java.io.FileReader(FileUtil.file(courseFile+"/monkey_keywords.txt"));
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			
				while ((line = br.readLine()) != null ||"".equals(line)) {
					System.out.println(line);
					keywords_list.add(line);
				}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			keywords_list.add("ANR");
			keywords_list.add("Exception");
			keywords_list.add("Null");
			keywords_list.add("Error");
			keywords_list.add("CRASH");
		}
		
		
		// 2、通过方法获取每种异常的集合
		// excel行数据map
		ArrayList<Map<String, Object>> rows = CollUtil.newArrayList();
		boolean testresult=true;
		TextFileSearch search = new TextFileSearch();
		List<Integer> sortlist = CollUtil.newArrayList();
		List<List<String>> allmsglist=CollUtil.newArrayList();
		
		for (int i = 0; i < keywords_list.size(); i++) {
			List<String> msglist=search.SearchKeyword(FileUtil.file(monkey_dir_name+"/monkey_error.txt"),keywords_list.get(i).toString());
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
		ExcelWriter writer=null;
		try {
			writer = ExcelUtil.getWriter(monkey_dir_name+"/monkey_Result.xlsx");
			// 合并单元格后的标题行，使用默认标题样式
			writer.merge(keywords_list.size()-1, "monkey测试结果(若有异常,异常详情可用notepad++打开error.txt定位行数查看具体异常)");
			// 一次性写出内容，使用默认样式，强制输出标题
			writer.write(rows, true);
			JFrameutil.showdialog("导出成功,路径"+monkey_dir_name+"/monkey_Result.xlsx");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JFrameutil.showdialog("报告已存在,请删除后重新导出试一下。");
		}finally{
			// 关闭writer，释放内存
			writer.close();
		}
	}
	
}

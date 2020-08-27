package com.tinno.utils;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import com.tinno.utils.TextFileUtil.TextFileSearch;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import cn.hutool.poi.excel.cell.CellUtil;
import cn.hutool.poi.excel.style.StyleUtil;

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
			System.out.println("未找到keyword.txt");
			keywords_list.add("CRASH");
			keywords_list.add("ANR");
			keywords_list.add("Exception");
			keywords_list.add("Null");
			keywords_list.add("Error");
		}
		// 2、通过方法获取每种异常的集合
		// excel行数据map
		ArrayList<Map<String, Object>> rows = CollUtil.newArrayList();
		boolean testresult=true;
		TextFileSearch search = new TextFileSearch();
		List<Integer> sortlist = CollUtil.newArrayList();
		List<List<String>> allmsglist=CollUtil.newArrayList();
		List<List<String>> allcount=CollUtil.newArrayList();
		
		
		for (int i = 0; i < keywords_list.size(); i++) {
			List<String> msglist=search.SearchKeyword(FileUtil.file(monkey_dir_name+"/monkey_error.txt"),keywords_list.get(i).toString()).get("result");
			List<String> countlist=search.SearchKeyword(FileUtil.file(monkey_dir_name+"/monkey_error.txt"),keywords_list.get(i).toString()).get("count");
			sortlist.add(msglist.size());
			allmsglist.add(msglist);
			if(!countlist.isEmpty()){
				allcount.add(countlist);
			}
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
			
			
			Map<String,Integer> countMap=null;
			int allanrCount=0;
			int allcrashCount=0;
			
			Map<String, Object> mtp=CollUtil.newHashMap();
			for (List<String> list : allcount) {
				countMap=com.tinno.utils.FileUtil.countRepeat(list);
//				System.out.println(countMap.toString());
				for (String s:list) {
//					System.out.println(s);
					switch (s.split("--")[0]) {
					case "ANR":
						allanrCount+=1;
						break;
					case "Exception":
						break;
					case "Null":
						break;
					case "Error":
						break;
					case "CRASH":
						allcrashCount+=1;
						break;
					default:
						break;
					}
				}
				StringBuilder sb=new StringBuilder();
				String k="";
				for(String key:countMap.keySet()){
					
					if(countMap.get(key) >= 1){
						k=key.split("--")[0];
//						System.out.println(key.split("--")[1]+"发生"+key.split("--")[0]+" "+countMap.get(key)+"次\n");
						sb.append(key.split("--")[1]+"发生"+key.split("--")[0]+" "+countMap.get(key)+"次\n");
					}
				}
				mtp.put(k, sb.toString());
			}
			rows.add(mtp);
//			System.out.println("ANR共"+allanrCount+"次");
//			System.out.println("CRASH共"+allcrashCount+"次");
			Map<String, Object> m=new HashMap<String, Object>();
			m.put("CRASH","CRASH共"+allcrashCount+"次\n");
			m.put("ANR","ANR共"+allanrCount+"次\n");
			rows.add(m);
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
			File f=new File(monkey_dir_name+"/monkey_Result.xlsx");
			if(f.exists()){
				f.delete();
			}
			writer = ExcelUtil.getWriter(monkey_dir_name+"/monkey_Result.xlsx");
			// 合并单元格后的标题行，使用默认标题样式
			writer.merge(keywords_list.size()-1, "monkey测试结果(若有异常,异常详情可用notepad++打开error.txt定位行数查看具体异常)");
			// 一次性写出内容，使用默认样式，强制输出标题
			StyleSet style=writer.getStyleSet();
			style.setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
			style.setWrapText();
			for(int s=0;s<keywords_list.size();s++){
//				设置列宽35
				writer.getSheet().setColumnWidth(s,35*256+184);
			}
			writer.setStyleSet(style);
			writer.write(rows, true);
			JFrameutil.showdialog("导出成功,路径"+monkey_dir_name+"/monkey_Result.xlsx");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JFrameutil.showdialog("报告导出异常...请关闭打开的报告，或删除重试。");
		}finally{
			// 关闭writer，释放内存
			writer.close();
		}
	}
}

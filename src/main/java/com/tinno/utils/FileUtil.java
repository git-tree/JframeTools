package com.tinno.utils;

import java.io.File;
import java.util.Map;
import com.tinno.pojo.Apk;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

public class FileUtil {

	public static Map<String, String> getApkInfo(File f, Map<String, String> map) {
		File[] arr = f.listFiles();
		for (File file : arr) {
			if (file.isFile() && file.getName().endsWith(".apk")) {
				map.put(file.getName(), file.getAbsolutePath());
			} else if (file.isDirectory()) {
				getApkInfo(file, map);
			}
		}
		return map;
	}

	public static Map<String, Apk> getApkInfos(File f, Map<String, Apk> map) throws Exception {
		File[] arr = f.listFiles();
		for (File file : arr) {
			if (file.isFile() && file.getName().endsWith(".apk")) {
				ApkFile apkFile = new ApkFile(file);
				ApkMeta apkMeta = apkFile.getApkMeta();
				Apk apk = new Apk(apkMeta.getLabel(), apkMeta.getPackageName(), file.getName(), file.getAbsolutePath());
				map.put(file.getName(), apk);
				// TODO Auto-generated catch block
			} else if (file.isDirectory()) {
				getApkInfos(file, map);
			}
		}
		return map;
	}
}

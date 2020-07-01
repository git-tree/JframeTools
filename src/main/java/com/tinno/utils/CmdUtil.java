package com.tinno.utils;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.FileUtil;

public class CmdUtil {

	public static boolean excuteCMDCommand(String cmdCommand) {
		boolean flag = false;
		StringBuilder stringBuilder = new StringBuilder();
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("cmd.exe /c " + cmdCommand);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			String str = stringBuilder.toString();
			if (!"".equals(str) && str.contains("uccess")) {
				flag = true;// 安装成功,或者卸载成功
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	public static String excuteCMDCommand_str(String cmdCommand) {
		StringBuilder stringBuilder = new StringBuilder();
		Process process = null;
		String result = "";
		try {
			process = Runtime.getRuntime().exec("cmd.exe /c " + cmdCommand);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			result = stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return result;
	}
	//打印cmd执行的log
	public static String excuteCMDCommand_str(String cmdCommand,boolean isprintLog) {
		StringBuilder stringBuilder = new StringBuilder();
		Process process = null;
		String result = "";
		try {
			process = Runtime.getRuntime().exec("cmd.exe /c " + cmdCommand);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
				if(isprintLog){
					System.out.println(line);
				}
			}
			result = stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return result;
	}
	public static boolean check_isappInstalled(String pkg) {
		String result = excuteCMDCommand_str("adb shell pm list packages").replace("package:", "");
		// System.out.println(result);
		String arr[] = result.split("\n");
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(pkg)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 执行bat文件，
	 * 
	 * @param file
	 *            bat文件路径
	 * @param isCloseWindow
	 *            执行完毕后是否关闭cmd窗口
	 * @return bat文件输出log
	 */
	public static String excuteBatFile(String file, boolean isCloseWindow) {
		String cmdCommand = null;
		if (isCloseWindow) {
			cmdCommand = "cmd.exe /c " + file;
		} else {
			cmdCommand = "cmd.exe /k " + file;
		}
		StringBuilder stringBuilder = new StringBuilder();
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmdCommand);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 执行bat文件,新开窗口
	 * 
	 * @param file
	 *            bat文件路径
	 * @param isCloseWindow
	 *            执行完毕后是否关闭cmd窗口
	 * @return bat文件输出log
	 */
	public static String excuteBatFileWithNewWindow(String file, boolean isCloseWindow) {
		String cmdCommand = null;
		if (isCloseWindow) {
			cmdCommand = "cmd.exe /c start" + file;
		} else {
			cmdCommand = "cmd.exe /k start" + file;
		}
		StringBuilder stringBuilder = new StringBuilder();
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmdCommand);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean checkDevice_isconn() {
		boolean flag = false;
		if (!"".equals(excuteCMDCommand_str("adb devices"))
				&& (excuteCMDCommand_str("adb devices").split("\n").length) > 1) {
			flag = true;
		}
		return flag;
	}

	public static boolean checkAdb() {
		boolean flag = false;
		if (!"".equals(excuteCMDCommand_str("adb --version"))
				&& excuteCMDCommand_str("adb --version").contains("Installed as")) {
			flag = true;
		}
		return flag;
	}

	/*
	 * 通过传入包名获取activity
	 */
	public static String pkgTostartCommand(String pkgName) {
		String command = "adb shell dumpsys package  " + pkgName + "  |findstr \"Activity Resolver Table:\"";
		String result = "";
		try {
			String returnstr = excuteCMDCommand_str(command);
			String[] arr = returnstr.split(":");
			String[] arr2 = null;
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].trim().contains(pkgName)) {
					arr2 = arr[i].split(" ");
					break;
				}
			}
			for (int i = 0; i < arr2.length; i++) {
				if (arr2[i].contains(pkgName)) {
					result = arr2[i];
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return result;
	}

	public static boolean startAppBystartBypkg(String pkg) {
		String result = pkgTostartCommand(pkg);
		boolean flag = false;
		try {
			if (!"".equals(result)) {
				String str = excuteCMDCommand_str("adb shell am start -n " + result);
				System.out.println(str);
				if (!str.contains("Error")) {
					flag = true;// 启动成功
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return flag;
	}

	public static void main(String[] args) throws IOException {
		// com.github.uiautomator
		// System.out.println(check_isappInstalled("com.qiyi.video"));
		System.out.println(
				FileUtil.mkdir("e:/monkey").getAbsolutePath());
		}
	/**
	 * 判断是否为亮屏
	 * @return true为亮屏,false灭屏
	 */
	public static boolean isScreenOn(){
		String command="adb shell dumpsys power | findstr \"Display Power:state=\"";
		boolean flag=false;
		String result=excuteCMDCommand_str(command);
		if(result.contains("Display Power: state=ON")){
			//亮屏
			flag=true;
		}
		return flag;
	}
	/**
	 * 点亮屏幕
	 */
	public static void makeScreenOn(){
		if(!isScreenOn()){
			excuteCMDCommand_str("adb shell input keyevent 26");
		}
	}
	/**
	 * 灭屏
	 */
	public static void makeScreenOff(){
		if(isScreenOn()){
			excuteCMDCommand_str("adb shell input keyevent 26");
		}
	}
	/**
	 * 屏幕常亮
	 */
	public static void setalways_screenon(){
		makeScreenOn();
		excuteCMDCommand_str("adb shell settings put system screen_off_timeout 999999999");
	}
	/**
	 * 设置亮屏时间
	 * @param min 分钟
	 */
	public static void setscreen_off_timeout(double min){
		int time=1*60*1000;
		try {
			time=(int) (min*60*1000);
			excuteCMDCommand_str("adb shell settings put system screen_off_timeout "+time+"");
			System.out.println(time);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			time=1*60*1000;
			excuteCMDCommand_str("adb shell settings put system screen_off_timeout "+time+"");
		}
	}
	/**
	 * 设置设备插上数据线不充电
	 */
	public static void setBatteryUnplug(){
		excuteCMDCommand_str("adb shell dumpsys battery unplug");
	}
	/**
	 * 重置电池设置
	 */
	public static void resetBattery(){
		excuteCMDCommand_str("adb shell dumpsys battery reset");
	}
	/**
	 * 设置电池电量
	 * @param level
	 */
	public static void setbatteryLevel(int level){
		excuteCMDCommand_str("adb shell dumpsys battery set level "+level);
	}
}

package com.tinno.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String TimeConsume(Date start, Date stop) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date parse = format.parse(format.format(start));
		Date date = format.parse(format.format(stop));
		System.out.println(parse);
		long between = date.getTime() - parse.getTime();
		long day = between / (24 * 60 * 60 * 1000);
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		// System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒");
		return day + "天" + hour + "小时" + min + "分" + s + "秒";
	}

	public static String TimeConsume_havelose(Date start, Date stop, long loseTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date parse = format.parse(format.format(start));
		Date date = format.parse(format.format(stop));
		long between = date.getTime() - parse.getTime() - loseTime;
		long day = between / (24 * 60 * 60 * 1000);
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		// System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒");
		return day + "天" + hour + "小时" + min + "分" + s + "秒";
	}
}

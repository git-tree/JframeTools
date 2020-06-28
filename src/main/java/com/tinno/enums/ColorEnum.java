package com.tinno.enums;

import java.awt.Color;

public enum ColorEnum {

	// 错误颜色
	ERRORCOLOR(Color.RED),// 红色
	//信息颜色
	INFOCOLOR(Color.BLUE),// 蓝色
	//成功颜色
	SUCCESSCOLOR(new Color(0, 128, 0)),// 绿色
	//日志颜色
	CHOCPLATECOLOR(new Color(210, 105, 30));// 巧克力
	
	private Color color;

	private ColorEnum(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
 
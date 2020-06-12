package com.tinno.pojo;

public class Apk {
	private String laBle;//中文名
	private String pkgName;//包名
	private String apkName;//apk在硬电脑上面名字
	private String apkLocation;//apk存在的地址
	
	public Apk() {
		super();
	}
	public Apk(String laBle, String pkgName, String apkName, String apkLocation) {
		super();
		this.laBle = laBle;
		this.pkgName = pkgName;
		this.apkName = apkName;
		this.apkLocation = apkLocation;
	}
	public String getLaBle() {
		return laBle;
	}

	public void setLaBle(String laBle) {
		this.laBle = laBle;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String getApkLocation() {
		return apkLocation;
	}

	public void setApkLocation(String apkLocation) {
		this.apkLocation = apkLocation;
	}

	@Override
	public String toString() {
		return "Apk [laBle=" + laBle + ", pkgName=" + pkgName + ", apkName=" + apkName + ", apkLocation=" + apkLocation
				+ "]";
	}
}

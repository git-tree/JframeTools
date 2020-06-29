package com.tinno.pojo;

import java.util.ArrayList;
import java.util.List;

public class MonkeyString {
	
	private List<String> pkg;
	private String level;
	private long testcount;
	private long event_space;
	private long seed;
	private String crash_goon;
	private String  anr_goon;
	private String exception_stay;
	private String lising_code;
	private String generate_report;
	private long touchPersent;
	private long motionPersent;
	private long trackballPersent;
	private long navPersent;
	private long turnonPersent;
	private long mainnavPersent;
	private long systemkeyPersent;
	private long appswitchPersent;
	private long keyboardPersent;
	private long otherPersent;
	public MonkeyString() {
		super();
	}
	public MonkeyString(List<String> pkg, String level, long testcount, long event_space, long seed, String crash_goon,
			String anr_goon, String exception_stay, String lising_code, String generate_report, long touchPersent,
			long motionPersent, long trackballPersent, long navPersent, long turnonPersent, long mainnavPersent,
			long systemkeyPersent, long appswitchPersent, long keyboardPersent, long otherPersent) {
		super();
		this.pkg = pkg;
		this.level = level;
		this.testcount = testcount;
		this.event_space = event_space;
		this.seed = seed;
		this.crash_goon = crash_goon;
		this.anr_goon = anr_goon;
		this.exception_stay = exception_stay;
		this.lising_code = lising_code;
		this.generate_report = generate_report;
		this.touchPersent = touchPersent;
		this.motionPersent = motionPersent;
		this.trackballPersent = trackballPersent;
		this.navPersent = navPersent;
		this.turnonPersent = turnonPersent;
		this.mainnavPersent = mainnavPersent;
		this.systemkeyPersent = systemkeyPersent;
		this.appswitchPersent = appswitchPersent;
		this.keyboardPersent = keyboardPersent;
		this.otherPersent = otherPersent;
	}

	public List<String> getPkg() {
		return pkg;
	}
	public void setPkg(List<String> pkg) {
		this.pkg = pkg;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public long getTestcount() {
		return testcount;
	}
	public void setTestcount(long testcount) {
		this.testcount = testcount;
	}
	public long getEvent_space() {
		return event_space;
	}
	public void setEvent_space(long event_space) {
		this.event_space = event_space;
	}
	public long getSeed() {
		return seed;
	}
	public void setSeed(long seed) {
		this.seed = seed;
	}
	public String getCrash_goon() {
		return crash_goon;
	}
	public void setCrash_goon(String crash_goon) {
		this.crash_goon = crash_goon;
	}
	public String getAnr_goon() {
		return anr_goon;
	}
	public void setAnr_goon(String anr_goon) {
		this.anr_goon = anr_goon;
	}
	public String getException_stay() {
		return exception_stay;
	}
	public void setException_stay(String exception_stay) {
		this.exception_stay = exception_stay;
	}
	public String getLising_code() {
		return lising_code;
	}
	public void setLising_code(String lising_code) {
		this.lising_code = lising_code;
	}
	public String getGenerate_report() {
		return generate_report;
	}
	public void setGenerate_report(String generate_report) {
		this.generate_report = generate_report;
	}
	public long getTouchPersent() {
		return touchPersent;
	}
	public void setTouchPersent(long touchPersent) {
		this.touchPersent = touchPersent;
	}
	public long getMotionPersent() {
		return motionPersent;
	}
	public void setMotionPersent(long motionPersent) {
		this.motionPersent = motionPersent;
	}
	public long getTrackballPersent() {
		return trackballPersent;
	}
	public void setTrackballPersent(long trackballPersent) {
		this.trackballPersent = trackballPersent;
	}
	public long getNavPersent() {
		return navPersent;
	}
	public void setNavPersent(long navPersent) {
		this.navPersent = navPersent;
	}
	public long getTurnonPersent() {
		return turnonPersent;
	}
	public void setTurnonPersent(long turnonPersent) {
		this.turnonPersent = turnonPersent;
	}
	public long getMainnavPersent() {
		return mainnavPersent;
	}
	public void setMainnavPersent(long mainnavPersent) {
		this.mainnavPersent = mainnavPersent;
	}
	public long getSystemkeyPersent() {
		return systemkeyPersent;
	}
	public void setSystemkeyPersent(long systemkeyPersent) {
		this.systemkeyPersent = systemkeyPersent;
	}
	public long getAppswitchPersent() {
		return appswitchPersent;
	}
	public void setAppswitchPersent(long appswitchPersent) {
		this.appswitchPersent = appswitchPersent;
	}
	public long getKeyboardPersent() {
		return keyboardPersent;
	}
	public void setKeyboardPersent(long keyboardPersent) {
		this.keyboardPersent = keyboardPersent;
	}
	public long getOtherPersent() {
		return otherPersent;
	}
	public void setOtherPersent(long otherPersent) {
		this.otherPersent = otherPersent;
	}
	public MonkeyString defaultconfig(){
		MonkeyString m=new MonkeyString();
		m.setPkg(new ArrayList<String>());
		m.setLevel("-v -v");
		m.setTestcount(24*60*60*1000/300);
		m.setEvent_space(300);
		m.setSeed(200);
		m.setCrash_goon("--ignore-crashes");
		m.setAnr_goon("--ignore-timeouts");
		m.setException_stay("--kill-process-after-error");
		m.setLising_code("--monitor-native-crashes");
		m.setGenerate_report("--hprof");
		m.setTouchPersent(15);
		m.setMotionPersent(10);
		m.setTrackballPersent(2);
		m.setNavPersent(25);
		m.setTurnonPersent(0);
		m.setMainnavPersent(15);
		m.setSystemkeyPersent(2);
		m.setAppswitchPersent(2);
		m.setKeyboardPersent(1);
		m.setOtherPersent(13);
		return m;
	}
	@Override
	public String toString() {
		return "MonkeyString [pkg=" + pkg + ", level=" + level + ", testcount=" + testcount + ", event_space="
				+ event_space + ", seed=" + seed + ", crash_goon=" + crash_goon + ", anr_goon=" + anr_goon
				+ ", exception_stay=" + exception_stay + ", lising_code=" + lising_code + ", generate_report="
				+ generate_report + ", touchPersent=" + touchPersent + ", motionPersent=" + motionPersent
				+ ", trackballPersent=" + trackballPersent + ", navPersent=" + navPersent + ", turnonPersent="
				+ turnonPersent + ", mainnavPersent=" + mainnavPersent + ", systemkeyPersent=" + systemkeyPersent
				+ ", appswitchPersent=" + appswitchPersent + ", keyboardPersent=" + keyboardPersent + ", otherPersent="
				+ otherPersent + "]";
	}
	

}

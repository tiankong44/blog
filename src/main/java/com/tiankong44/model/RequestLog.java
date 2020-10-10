package com.tiankong44.model;

import java.util.Arrays;

public class RequestLog {
	private String url;
	private String ip;
	private String classMethod;
	private Object[] args;

	public RequestLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestLog(String url, String ip, String classMethod, Object[] args) {
		super();
		this.url = url;
		this.ip = ip;
		this.classMethod = classMethod;
		this.args = args;
	}

	@Override
	public String toString() {
		return "[url=" + url + ", ip=" + ip + ", classMethod=" + classMethod + ", args="
				+ Arrays.toString(args) + "]";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getClassMethod() {
		return classMethod;
	}

	public void setClassMethod(String classMethod) {
		this.classMethod = classMethod;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

}

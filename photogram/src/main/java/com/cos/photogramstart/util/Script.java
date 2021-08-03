package com.cos.photogramstart.util;

public class Script { //에러페에지 대신 메세지로 에러메세지를 띄우기 위해!
	
	public static String back(String msg) {//자바스크립트로 UX를 좋게하기 위한 것. 유효성검사 실패시 예외화면으로 아예 넘어가 버리면 UX가 좋지 못함.
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert('"+msg+"');");
		sb.append("history.back();");
		sb.append("</script>");
		return sb.toString();
	}

}

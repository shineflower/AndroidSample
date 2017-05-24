package com.jackie.sample.bean;

import java.io.Serializable;

public class CommentData implements Serializable {
	String id;
	String head;
	String nickname;
	String date;
	String content;
	String isMyCmt;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getHead() {
		return head;
	}
	
	public void setHead(String head) {
		this.head = head;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIsMyCmt() {
		return isMyCmt;
	}

	public void setIsMyCmt(String isMyCmt) {
		this.isMyCmt = isMyCmt;
	}
}

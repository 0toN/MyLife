package cn.edu.tsu.se.mylife.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Saying {
	private String author;
	private String content;

	public void parse(JSONObject obj) throws JSONException {
		author = obj.optString("author");
		content = obj.optString("content");
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

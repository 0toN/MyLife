package cn.edu.tsu.se.mylife.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Trip {
	private int id;
	private String content;
	private String time;
	private boolean isNotice;

	public void parse(JSONObject obj) throws JSONException {
		id = obj.optInt("id");
		content = obj.optString("content");
		time = obj.optString("time");
		isNotice = obj.optBoolean("notice");
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isNotice() {
		return isNotice;
	}

	public void setNotice(boolean isNotice) {
		this.isNotice = isNotice;
	}
}

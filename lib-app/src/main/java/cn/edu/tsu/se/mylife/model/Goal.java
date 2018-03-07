package cn.edu.tsu.se.mylife.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Goal {
	private int id;
	private String title;
	private String content;
	private String time;

	public void parse(JSONObject obj) throws JSONException {
		id = obj.optInt("id");
		title = obj.optString("title");
		content = obj.optString("content");
		time = obj.optString("time");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
}

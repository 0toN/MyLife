package cn.edu.tsu.se.mylife.model;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.tsu.se.mylife.util.TimeUtil;

public class Dialog {
	private int id;
	private String title;
	private String content;
	private int mood;
	private String postTime;

	public void parse(JSONObject obj) throws JSONException {
		id = obj.optInt("id");
		title = obj.optString("title");
		content = obj.optString("content");
		mood = obj.optInt("mood");
		postTime = obj.optString("post_time");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMood() {
		return mood;
	}

	public void setMood(int mood) {
		this.mood = mood;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

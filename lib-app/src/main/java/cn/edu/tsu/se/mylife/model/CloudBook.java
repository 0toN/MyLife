package cn.edu.tsu.se.mylife.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CloudBook {
	private int id;
	private String name;
	private String postTime;
	private String size;

	public void parse(JSONObject obj) throws JSONException {
		id = obj.optInt("id");
		name = obj.optString("name");
		postTime = obj.optString("post_time");
		size = obj.optString("size");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}

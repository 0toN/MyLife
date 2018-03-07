package cn.edu.tsu.se.mylife.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.tsu.se.mylife.util.LogUtil;

public class HomePageProfile {
	private int code;
	private String message;
	private String headPortraitUrl;
	private String nickname;
	private String signature = "";
	private ArrayList<Saying> sayings;
	private ArrayList<Trip> noticeTrips;
	private ArrayList<Goal> goals;

	public void parse(JSONObject obj) throws JSONException {
		code = obj.optInt("code");
		message = obj.optString("message");
		JSONObject userObj = obj.optJSONObject("user");
		headPortraitUrl = userObj.optString("image_url");
		nickname = userObj.optString("name");
		if (!(userObj.optString("signature")).equals("null"))
			signature = userObj.optString("signature");
		JSONArray tripArray = obj.optJSONArray("trip_list");
		if (tripArray == null)
			return;
		noticeTrips = new ArrayList<>();
		for (int i = 0; i < tripArray.length(); i++) {
			JSONObject tripObj = tripArray.optJSONObject(i);
			Trip trip = new Trip();
			trip.parse(tripObj);
			noticeTrips.add(trip);
		}
		JSONArray goalArray = obj.optJSONArray("target_list");
		if (goalArray == null)
			return;
		goals = new ArrayList<>();
		for (int i = 0; i < goalArray.length(); i++) {
			JSONObject goalObj = goalArray.optJSONObject(i);
			Goal goal = new Goal();
			goal.parse(goalObj);
			goals.add(goal);
		}
		JSONArray sayingArray = obj.optJSONArray("dictum_list");
		if (sayingArray == null)
			return;
		sayings = new ArrayList<>();
		for (int i = 0; i < sayingArray.length(); i++) {
			JSONObject sayingObj = sayingArray.optJSONObject(i);
			Saying saying = new Saying();
			saying.parse(sayingObj);
			sayings.add(saying);
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHeadPortraitUrl() {
		return headPortraitUrl;
	}

	public void setHeadPortraitUrl(String headPortraitUrl) {
		this.headPortraitUrl = headPortraitUrl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public ArrayList<Trip> getNoticeTrips() {
		return noticeTrips;
	}

	public void setNoticeTrips(ArrayList<Trip> noticeTrips) {
		this.noticeTrips = noticeTrips;
	}

	public ArrayList<Goal> getGoals() {
		return goals;
	}

	public void setGoals(ArrayList<Goal> goals) {
		this.goals = goals;
	}

	public ArrayList<Saying> getSayings() {
		return sayings;
	}

	public void setSayings(ArrayList<Saying> sayings) {
		this.sayings = sayings;
	}
}

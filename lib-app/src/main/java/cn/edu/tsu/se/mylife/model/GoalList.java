package cn.edu.tsu.se.mylife.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoalList {
	private int code;
	private String message;

	private int totalRow;
	private int pageNumber;
	private boolean isFirstPage;
	private boolean isLastPage;
	private int totalPage;
	private int pageSize;

	private ArrayList<Goal> goals;

	public void parse(JSONObject obj) throws JSONException {
		code = obj.optInt("code");
		message = obj.optString("message");
		JSONObject goalPageObj = obj.optJSONObject("target_page");
		totalRow = goalPageObj.optInt("totalRow");
		pageNumber = goalPageObj.optInt("pageNumber");
		totalPage = goalPageObj.optInt("totalPage");
		pageSize = goalPageObj.optInt("pageSize");
		isFirstPage = goalPageObj.optBoolean("isFirstPage");
		isLastPage = goalPageObj.optBoolean("isLastPage");

		JSONArray goalArray = goalPageObj.optJSONArray("list");
		if (goalArray == null)
			return;
		goals = new ArrayList<>();
		for (int i = 0; i < goalArray.length(); i++) {
			JSONObject goalObj = goalArray.optJSONObject(i);
			Goal goal = new Goal();
			goal.parse(goalObj);
			goals.add(goal);
		}
	}

	public int add(Goal goal) {
		goals.add(goal);
		return goals.indexOf(goal);
	}

	public Goal get(int position) {
		return goals.get(position);
	}

	public int size() {
		return goals.size();
	}

	public Goal remove(int position) {
		Goal goal = goals.remove(position);
		return goal;
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

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public boolean isFirstPage() {
		return isFirstPage;
	}

	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public ArrayList<Goal> getGoals() {
		return goals;
	}

	public void setGoals(ArrayList<Goal> goals) {
		this.goals = goals;
	}
}

package cn.edu.tsu.se.mylife.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TripList {
	private int code;
	private String message;

	private int totalRow;
	private int pageNumber;
	private boolean isFirstPage;
	private boolean isLastPage;
	private int totalPage;
	private int pageSize;

	private ArrayList<Trip> trips;

	public void parse(JSONObject obj) throws JSONException {
		code = obj.optInt("code");
		message = obj.optString("message");
		JSONObject tripPageObj = obj.optJSONObject("trip_page");
		totalRow = tripPageObj.optInt("totalRow");
		pageNumber = tripPageObj.optInt("pageNumber");
		totalPage = tripPageObj.optInt("totalPage");
		pageSize = tripPageObj.optInt("pageSize");
		isFirstPage = tripPageObj.optBoolean("isFirstPage");
		isLastPage = tripPageObj.optBoolean("isLastPage");

		JSONArray tripArray = tripPageObj.optJSONArray("list");
		if (tripArray == null)
			return;
		trips = new ArrayList<>();
		for (int i = 0; i < tripArray.length(); i++) {
			JSONObject tripObj = tripArray.optJSONObject(i);
			Trip trip = new Trip();
			trip.parse(tripObj);
			trips.add(trip);
		}
	}

	public int add(Trip trip) {
		trips.add(trip);
		return trips.indexOf(trip);
	}

	public Trip get(int position) {
		return trips.get(position);
	}

	public int size() {
		return trips.size();
	}

	public Trip remove(int position) {
		Trip trip = trips.remove(position);
		return trip;
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

	public ArrayList<Trip> getTrips() {
		return trips;
	}

	public void setTrips(ArrayList<Trip> trips) {
		this.trips = trips;
	}
}

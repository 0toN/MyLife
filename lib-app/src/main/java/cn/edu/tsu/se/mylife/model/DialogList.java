package cn.edu.tsu.se.mylife.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DialogList {
	private int code;
	private String message;

	private int totalRow;
	private int pageNumber;
	private boolean isFirstPage;
	private boolean isLastPage;
	private int totalPage;
	private int pageSize;

	private ArrayList<Dialog> dialogs;

	public void parse(JSONObject obj) throws JSONException {
		code = obj.optInt("code");
		message = obj.optString("message");
		JSONObject logPageObj = obj.optJSONObject("log_page");
		totalRow = logPageObj.optInt("totalRow");
		pageNumber = logPageObj.optInt("pageNumber");
		totalPage = logPageObj.optInt("totalPage");
		pageSize = logPageObj.optInt("pageSize");
		isFirstPage = logPageObj.optBoolean("isFirstPage");
		isLastPage = logPageObj.optBoolean("isLastPage");

		JSONArray dialogArray = logPageObj.optJSONArray("list");
		if (dialogArray == null)
			return;
		dialogs = new ArrayList<>();
		for (int i = 0; i < dialogArray.length(); i++) {
			JSONObject dialogObj = dialogArray.optJSONObject(i);
			Dialog dialog = new Dialog();
			dialog.parse(dialogObj);
			dialogs.add(dialog);
		}
	}

	public int add(Dialog dialog) {
		dialogs.add(dialog);
		return dialogs.indexOf(dialog);
	}

	public Dialog get(int position) {
		return dialogs.get(position);
	}

	public int size() {
		return dialogs.size();
	}

	public Dialog remove(int position) {
		Dialog dialog = dialogs.remove(position);
		return dialog;
	}

	public int indexOf(Dialog dialog) {
		return dialogs.indexOf(dialog);
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

	public ArrayList<Dialog> getDialogs() {
		return dialogs;
	}

	public void setDialogs(ArrayList<Dialog> dialogs) {
		this.dialogs = dialogs;
	}
}

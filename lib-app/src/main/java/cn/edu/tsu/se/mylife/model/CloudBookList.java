package cn.edu.tsu.se.mylife.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CloudBookList {
	private int code;
	private String message;

	private ArrayList<CloudBook> cloudBooks;

	public void parse(JSONObject obj) throws JSONException {
		code = obj.optInt("code");
		message = obj.optString("message");
		JSONArray bookArray = obj.optJSONArray("pdf_list");
		if (bookArray == null)
			return;
		cloudBooks = new ArrayList<>();
		for (int i = 0; i < bookArray.length(); i++) {
			JSONObject bookObj = bookArray.optJSONObject(i);
			CloudBook book = new CloudBook();
			book.parse(bookObj);
			cloudBooks.add(book);
		}
	}

	public int add(CloudBook book) {
		cloudBooks.add(book);
		return cloudBooks.indexOf(book);
	}

	public CloudBook get(int position) {
		return cloudBooks.get(position);
	}

	public int size() {
		return cloudBooks.size();
	}

	public CloudBook remove(int position) {
		CloudBook book = cloudBooks.remove(position);
		return book;
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

	public ArrayList<CloudBook> getCloudBooks() {
		return cloudBooks;
	}

	public void setCloudBooks(ArrayList<CloudBook> cloudBooks) {
		this.cloudBooks = cloudBooks;
	}
}

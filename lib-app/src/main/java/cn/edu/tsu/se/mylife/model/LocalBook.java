package cn.edu.tsu.se.mylife.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "local_book")
public class LocalBook {
	@Id
	private int id;
	private String name;
	private String size;
	private String date;
	private String path;

	public LocalBook(String name, String size, String date) {
		super();
		this.name = name;
		this.size = size;
		this.date = date;
	}

	public LocalBook() {
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}

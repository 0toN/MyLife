package cn.edu.tsu.se.mylife.model;

public class MenuItem {
	private String title;
	private int iconResID;
	private int type;

	public MenuItem(int iconResID, String title, int type) {
		this.title = title;
		this.iconResID = iconResID;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIconRsId() {
		return iconResID;
	}

	public void setIconRsId(int iconRsId) {
		this.iconResID = iconRsId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}

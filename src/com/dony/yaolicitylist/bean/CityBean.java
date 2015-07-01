package com.dony.yaolicitylist.bean;

public class CityBean implements Info {
	private int id;
	private String name = "";
	private String pinyin = "";
	private float lat = 0.0f;
	private float lng = 0.0f;
	private boolean isOpen;
	private String division;

	public CityBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CityBean(int id, String name, String pinyin, float lat, float lng,
			boolean isOpen, String division) {
		super();
		this.id = id;
		this.name = name;
		this.pinyin = pinyin;
		this.lat = lat;
		this.lng = lng;
		this.isOpen = isOpen;
		this.division = division;
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

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

}

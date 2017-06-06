package com.jackie.sample.bean;

import com.jackie.sample.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapInfo implements Serializable {
	private static final long serialVersionUID = -758459502806858414L;
	//精度
	private double latitude;
	//纬度
	private double longitude;
	//图片ID，真实项目中可能是图片路径
	private int imageId;
	//商家名称
	private String name;
	//距离
	private String distance;
	//赞数量
	private int zan;

	public static List<MapInfo> MapInfoList = new ArrayList<>();

	static {
		MapInfoList.add(new MapInfo(34.242652, 108.971171, R.drawable.a01, "英伦贵族小旅馆",
				"距离209米", 1456));
		MapInfoList.add(new MapInfo(34.242952, 108.972171, R.drawable.a02, "沙井国际洗浴会所",
				"距离897米", 456));
		MapInfoList.add(new MapInfo(34.242852, 108.973171, R.drawable.a03, "五环服装城",
				"距离249米", 1456));
		MapInfoList.add(new MapInfo(34.242152, 108.971971, R.drawable.a04, "老米家泡馍小炒",
				"距离679米", 1456));
	}

	public MapInfo() {
	}

	public MapInfo(double latitude, double longitude, int imageId, String name,
			String distance, int zan) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageId = imageId;
		this.name = name;
		this.distance = distance;
		this.zan = zan;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}
}

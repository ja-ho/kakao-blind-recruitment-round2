package com.khkong.kakao2.data;

public class Image {
	private String type;
	private String id;
	private long feature;

	public Image() {
		feature = -1;
	}

	public long getFeature() {
		return feature;
	}

	public void setFeature(long feature) {
		this.feature = feature;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

package com.khkong.kakao2.data;

import java.util.ArrayList;

public class Doc {
	private ArrayList<Image> imageList;
	private int currImageSize;
	private String nextUrl;
	private int imageArrIndex;

	public Doc() {
		imageList = new ArrayList<Image>();
		imageArrIndex = 0;
	}

	public void listClear() {
		this.imageList.clear();
	}

	public void setImageList(ArrayList<Image> imageQueue) {
		this.imageList = imageQueue;
	}

	public int getImageArrIndex() {
		return imageArrIndex;
	}

	public void setImageArrIndex(int imageArrIndex) {
		this.imageArrIndex = imageArrIndex;
	}

	public ArrayList<Image> getImageList() {
		return imageList;
	}

	public void addImage(Image image) {
		this.imageList.add(image);
	}

	public int getImageListSize() {
		return imageList.size();
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	public void removeImage() {
		this.imageList.remove(0);
	}

}

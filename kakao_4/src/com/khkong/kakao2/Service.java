package com.khkong.kakao2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.khkong.kakao2.data.Doc;
import com.khkong.kakao2.data.GlobalData;
import com.khkong.kakao2.data.Image;

public class Service implements Runnable {
	private JSONParser parser = null;
	private String docId;
	private String token;
	private String docType;
	private Doc doc;
	private boolean isExit;

	public Service(String docId, String docType) {
		parser = new JSONParser();
		TokenMng mng = TokenMng.getInstance();
		this.token = mng.getToken();
		this.docId = docId;
		this.docType = docType;
		this.isExit = false;
		this.doc = new Doc();
	}

	@Override
	public void run() {
		try {
			play();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(docType + "::FINISH");
	}

	public void play() {
		getDocument(docId);
		JSONArray addArr = new JSONArray();
		JSONArray deleteArr = new JSONArray();

		while (!isExit) {
			while (doc.getImageListSize() < 50 && !isExit) {
				int originSize = doc.getImageListSize();
				getDocument(doc.getNextUrl());
				System.out.println(docType + "::getDoc" + (doc.getImageListSize() - originSize));
			}
			featureExtraction();
			ArrayList<Image> imageList = doc.getImageList();
			for (int i = 0; i < imageList.size(); i++) {
				if (imageList.get(i).getFeature() == -1)
					continue;
				JSONObject tmp = new JSONObject();
				tmp.put("id", imageList.get(i).getId());
				tmp.put("feature", imageList.get(i).getFeature());
				if (imageList.get(i).getType().equals("add")) {
					if (addArr.length() == 50) {
						JSONObject tmpObject = new JSONObject();
						tmpObject.put("data", addArr);
						String response = Connection.getInstance()
								.requestSave(GlobalData.HOST_URL + GlobalData.FEATURE_METHOD, token, tmpObject);
						System.out.println(docType + "::save::" + response);
						addArr = new JSONArray();
					}
					addArr.put(tmp);
				} else if (imageList.get(i).getType().equals("del")) {
					if (deleteArr.length() == 50) {
						JSONObject tmpObject = new JSONObject();
						tmpObject.put("data", deleteArr);
						String response = Connection.getInstance()
								.requestDelete(GlobalData.HOST_URL + GlobalData.FEATURE_METHOD, token, tmpObject);
						System.out.println(docType + "::delete::" + response);
						deleteArr = new JSONArray();
					}
					deleteArr.put(tmp);
				}
			}
			doc.listClear();
			doc.setImageArrIndex(0);
		}
	}

	public void featureExtraction() {
		ArrayList<Image> imageList = doc.getImageList();
		int size = imageList.size();
		String requestURL = GlobalData.HOST_URL + GlobalData.FEATURE_EXTRACTION;

		int count = 0;
		for (int i = doc.getImageArrIndex(); i < size; i++) {
			requestURL += imageList.get(i).getId();
			count++;
			if (count == 50) // 한번에 50 개 까지만.
				break;
			else if (i != size - 1)
				requestURL += ",";
		}

		String response = Connection.getInstance().request(requestURL, token);
		if (response.equals("400")) {
			System.out.println(docType + "::Service::featureExtraction::BAD REQUEST");
		} else if (response.equals("401")) {
			System.out.println(docType + "::Service::featureExtraction::UNAUTHORIZED");
			isExit = true;
		} else if (response.equals("503")) {
			System.out.println(docType + "::Service::featureExtraction::SERVICE UNAVAILABLE");
			try {
				Thread.sleep(100);
				featureExtraction();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			HashMap<String, Long> featureMap = parser.getFeatureParser(response);
			int len = doc.getImageArrIndex() + count > size ? size : doc.getImageArrIndex() + count;
			for (int i = doc.getImageArrIndex(); i < len; i++) {
				try {
					String id = imageList.get(i).getId();
					if (featureMap.containsKey(id)) {
						imageList.get(i).setFeature(featureMap.get(id));
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			doc.setImageArrIndex(len);
			if (doc.getImageArrIndex() != size) {
				featureExtraction();
			}
		}
		doc.setImageList(imageList);
	}

	public Doc getDocument(String id) {
		String response = Connection.getInstance().request(GlobalData.HOST_URL + id, token);
		if (response.equals("400")) {
			System.out.println(docType + "::Service::getDocument::BAD REQUEST");
		} else if (response.equals("401")) {
			System.out.println(docType + "::Service::getDocument::UNAUTHORIZED");
			isExit = true;
		} else if (response.equals("503")) {
			System.out.println(docType + "::Service::getDocument::SERVICE UNAVAILABLE");
			try {
				Thread.sleep(100);
				getDocument(id);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			parser.getDocParser(response, doc);
		}
		docId = id;
		return doc;
	}

}

package com.khkong.kakao2;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.khkong.kakao2.data.Doc;
import com.khkong.kakao2.data.Image;

public class JSONParser {

	public void getDocParser(String jsonData, Doc doc) {
		JSONObject responseJSON = new JSONObject(jsonData);
		String nextUrl = (String) responseJSON.get("next_url");
		JSONArray images = (JSONArray) responseJSON.get("images");
		int size = images.length();
		for (int i = 0; i < size; i++) {
			JSONObject object = (JSONObject) images.get(i);
			String type = (String) object.get("type");
			String id = (String) object.get("id");
			Image image = new Image();
			image.setId(id);
			image.setType(type);
			doc.addImage(image);
		}
		doc.setNextUrl(nextUrl);
	}

	public HashMap<String, Long> getFeatureParser(String jsonData) {
		HashMap<String, Long> map = new HashMap<String, Long>();
		JSONObject responseJSON = new JSONObject(jsonData);
		JSONArray features = (JSONArray) responseJSON.get("features");
		int size = features.length();
		for (int i = 0; i < size; i++) {
			JSONObject object = (JSONObject) features.get(i);
			try {
				long feature = (long) object.get("feature");
				String id = (String) object.get("id");
				map.put(id, feature);
			} catch (Exception e) {

			}
		}
		return map;
	}
}

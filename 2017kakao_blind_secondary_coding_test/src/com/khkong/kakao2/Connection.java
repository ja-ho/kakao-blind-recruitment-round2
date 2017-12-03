package com.khkong.kakao2;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class Connection {
	public static Connection instance = null;

	public static Connection getInstance() {
		if (instance == null) {
			instance = new Connection();
		}
		return instance;
	}

	public String request(String path) {
		// System.out.println("Solution::request::path::" + path);
		HttpURLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		String response = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				is = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] byteBuffer = new byte[1024];
				byte[] byteData = null;
				int nLength = 0;
				while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
					baos.write(byteBuffer, 0, nLength);
				}
				byteData = baos.toByteArray();
				response = new String(byteData);
			} else {
				response = String.valueOf(responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null)
				try {
					baos.close();
				} catch (Exception e2) {
				}
			if (is != null)
				try {
					is.close();
				} catch (Exception e2) {
				}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return response;
	}

	public String request(String path, String paramToken) {
		// System.out.println("Solution::request::path::" + path);
		// System.out.println("Solution::request::paramToken::" + paramToken);
		HttpURLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		String response = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("X-Auth-Token", paramToken);

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				is = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] byteBuffer = new byte[1024];
				byte[] byteData = null;
				int nLength = 0;
				while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
					baos.write(byteBuffer, 0, nLength);
				}
				byteData = baos.toByteArray();
				response = new String(byteData);
			} else {
				response = String.valueOf(responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null)
				try {
					baos.close();
				} catch (Exception e2) {
				}
			if (is != null)
				try {
					is.close();
				} catch (Exception e2) {
				}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return response;
	}

	public synchronized String requestSave(String path, String paramToken, JSONObject jsonObject) {
		// System.out.println("Solution::requestSave::path::" + path);
		// System.out.println(jsonObject.toString());
		HttpURLConnection conn = null;
		OutputStreamWriter osw = null;
		String response = "";
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("X-Auth-Token", paramToken);
			osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			osw.write(jsonObject.toString());
			osw.flush();
			response = String.valueOf(conn.getResponseCode());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(path);
		} finally {
			if (osw != null)
				try {
					osw.close();
				} catch (Exception e2) {
				}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return response;
	}

	public synchronized String requestDelete(String path, String paramToken, JSONObject jsonObject) {
		// System.out.println("Solution::requestDelete::path::" + path);
		// System.out.println(jsonObject.toString());
		HttpURLConnection conn = null;
		OutputStreamWriter osw = null;
		String response = "";
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("X-Auth-Token", paramToken);
			osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			osw.write(jsonObject.toString());
			osw.flush();
			response = String.valueOf(conn.getResponseCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (osw != null)
				try {
					osw.close();
				} catch (Exception e2) {
				}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return response;
	}

}

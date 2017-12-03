package com.khkong.kakao2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.khkong.kakao2.data.GlobalData;

public class TokenMng {
	private String token = "";
	public static TokenMng instance = null;

	public static TokenMng getInstance() {
		if (instance == null) {
			instance = new TokenMng();
		}
		return instance;
	}

	public String getToken() {
		return token;
	}

	public synchronized String createToken() {
		String token = null;
		String response = Connection.getInstance().request(GlobalData.HOST_URL + GlobalData.GET_TOKEN_ID);
		if (response.equals("403")) {
			token = loadTokenFile();
		} else if (response.equals("503")) {
			System.out.println("Solution::getToken::SERVICE UNAVAILABLE");
		} else if (response.equals("400")) {
			System.out.println("Solution::getToken::BAD REQUEST");
		} else {
			saveTokenFile(response/* token */);
			response = "200";
		}
		// System.out.println("Solution::getToken::response::" + response);
		// System.out.println("Solution::getToken::token::" + token);
		this.token = token;
		return response;
	}

	public void saveTokenFile(String token) {
		File file = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			file = new File("./res/token");
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(token);
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (Exception e2) {
				}
			if (fw != null)
				try {
					fw.close();
				} catch (Exception e2) {
				}
		}
	}

	public String loadTokenFile() {
		String token = null;
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			file = new File("./res/token");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			token = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e2) {
				}
			}
			if (fr != null)
				try {
					fr.close();
				} catch (Exception e2) {
				}
		}
		return token;
	}

}

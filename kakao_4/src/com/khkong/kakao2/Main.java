package com.khkong.kakao2;

import com.khkong.kakao2.data.GlobalData;

public class Main {

	public static void main(String[] args) {
		TokenMng tokenMng = TokenMng.getInstance();
		tokenMng.createToken();
		String token = tokenMng.getToken();

		String response = Connection.getInstance().request(GlobalData.HOST_URL + GlobalData.GET_SEED, token);
		if (response.equals("400")) {
			System.out.println("Main::getSeed::BAD REQUEST");
		} else if (response.equals("401")) {
			System.out.println("Main::getSeed::UNAUTHORIZED");
		} else if (response.equals("503")) {
			System.out.println("Main::getSeed::SERVICE UNAVAILABLE");
		} else {
			String[] splitResponse = response.split("\n");
			String blogSeed = splitResponse[0];
			String newsSeed = splitResponse[1];
			String socialSeed = splitResponse[2];
			String sportSeed = splitResponse[3];
			String artSeed = splitResponse[4];
			Thread blogThread = new Thread(new Service(blogSeed, "blog"));
			Thread newsThread = new Thread(new Service(newsSeed, "news"));
			Thread socialThread = new Thread(new Service(socialSeed, "social"));
			Thread sportThread = new Thread(new Service(sportSeed, "sport"));
			Thread artThread = new Thread(new Service(artSeed, "art"));
			blogThread.start();
			newsThread.start();
			socialThread.start();
			sportThread.start();
			artThread.start();
			try {
				blogThread.join();
				newsThread.join();
				socialThread.join();
				sportThread.join();
				artThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

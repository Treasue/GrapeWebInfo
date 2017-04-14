package grape.app.GrapeWebInfo;

import httpServer.booter;

public class AppWeb {

	public static void main(String[] args) {
		booter booter = new booter();
		System.out.println("GrapeWebInfo!");
		try {
			System.setProperty("AppName", "GrapeWebInfo");
			booter.start(6006);
		} catch (Exception e) {

		}

	}

}

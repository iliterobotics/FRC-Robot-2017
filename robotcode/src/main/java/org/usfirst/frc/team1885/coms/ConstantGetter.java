package org.usfirst.frc.team1885.coms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConstantGetter {

	private static final String ADDRESS = "http://michael-laptop.local";

	public static String getConstant(String var) {
		String url = ADDRESS + "/constant/" + var;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();

		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return "";

	}

	public static String setConstant(String var, String value){
		new Thread( new Runnable() {
			
		@Override
		public void run() {
			String url = ADDRESS + "/constant/" + var + "/" + value;
			try {
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = in.readLine()) != null){
				response.append(inputLine);
			}
			in.close();
			
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
			
		}}).start();
		return "";		
	}

	public static String addConstant(String var, String value) {
		String url = ADDRESS + "/constant/add/" + var + "/" + value;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();

		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return "";
	}
}

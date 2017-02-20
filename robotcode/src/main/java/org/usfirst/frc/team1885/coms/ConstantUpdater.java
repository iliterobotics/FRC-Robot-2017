package org.usfirst.frc.team1885.coms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

public class ConstantUpdater implements Runnable {
	public static ConstantUpdater instance;
	private boolean running;
	private Queue<Update> update;
	private Update currentUpdate;
	
	private ConstantUpdater() {
		update = new LinkedList<Update>();
		running = true;
	}
	
	public static ConstantUpdater getInstance() {
		if(instance == null) {
			instance = new ConstantUpdater();
		}
		return instance;
	}
	
	public void run() {
		while ( running ) {
			while (update.peek() != null ) {
				currentUpdate = update.poll();
				String url = ConstantGetter.ADDRESS + "/constant/" + currentUpdate.variable + "/" + currentUpdate.value;
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
				} 
				catch (MalformedURLException e) {} 
				catch (IOException e) {}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}	
		}
	}
	
	public void stop(){
		running = false;
	}
	
	public void addToConstant( String var, String val ){
		update.add(new Update(var, val));
	}
	
	private class Update{
		public String variable, value;
		Update(String var, String val) {
			this.variable = var;
			this.value = val;
		}
	}
	
}

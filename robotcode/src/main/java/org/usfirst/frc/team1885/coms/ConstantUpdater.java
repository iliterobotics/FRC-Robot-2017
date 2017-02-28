package org.usfirst.frc.team1885.coms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class ConstantUpdater implements Runnable {
	
	public static final String ADDRESS = "http://michael-laptop.local";
	public static final String NETWORK_TABLE = "Live values";
	
	public static ConstantUpdater instance;
	public static Thread instanceThread;
	
	private boolean running;
	private Queue<Update<?>> updateQueue;
	
	private NetworkTable netTable;
	
	private ConstantUpdater() {
		updateQueue = new LinkedList<>();
		netTable = NetworkTable.getTable(NETWORK_TABLE);
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
			while (updateQueue.peek() != null ) {
				Update<?> currentUpdate = updateQueue.poll();
				pushToNetworkTables(currentUpdate);
				pushToWebserver(currentUpdate);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				running = false;
			}	
		}
	}
	
	private void pushToWebserver(Update<?> currentUpdate){
		String url = ConstantGetter.ADDRESS + "/constant/" + currentUpdate.variable + "/" + currentUpdate.value;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			//int responseCode = con.getResponseCode();
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
	
	private void pushToNetworkTables(Update<?> currentUpdate){
		if(currentUpdate.value instanceof Number){
			double doubleValue = ((Number)(currentUpdate.value)).doubleValue();
			netTable.putNumber(currentUpdate.variable, doubleValue);
		}
		else if(currentUpdate.value instanceof Number[]){
			Number[] numberArrayValue = ((Number [])(currentUpdate.value));
			double[] doubleArrayValue = new double[numberArrayValue.length];
			for(int i = 0; i < numberArrayValue.length; i++){
				doubleArrayValue[i] = numberArrayValue[i].doubleValue();
			}
			netTable.putNumberArray(currentUpdate.variable, doubleArrayValue);
		}
		else{ //Treat it as a string
			String stringValue = (currentUpdate.value.toString());
			netTable.putString(currentUpdate.variable, stringValue);
		}
	}
	
	public static void restart(){
		if(instance != null) instance.running = false;
		instance = null;
	}
	
	public static void start(){
		if(instanceThread == null){
			instanceThread = new Thread(getInstance());
		} else {
			restart();
		}
	}
	
	public static void stop(){
		getInstance().running = false;
	}
	
	public static void putString( String var, String val ){
		getInstance().updateQueue.add(new Update<String>(var, val));
	}
	
	public static void putNumber(String var, Number val){
		getInstance().updateQueue.add(new Update<Number>(var, val));		
	}
	
	public static void putNumberArray(String var, Number[] vals){
		getInstance().updateQueue.add(new Update<Number[]>(var, vals));
	}
	
	private static class Update<E extends Object>{
		public String variable;
		public E value;
		Update(String var, E val) {
			this.variable = var;
			this.value = val;
		}
	}	
	
}

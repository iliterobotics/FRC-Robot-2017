package org.usfirst.frc.team1885.coms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
				//pushToSmartDashboard(currentUpdate);
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
		String url = ADDRESS + "/logvals/set";
		System.out.println(url);
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Accept", "application/json");
			
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
			JSONObject object = new JSONObject();
			object.put("Name", currentUpdate.variable);
			object.put("Value", currentUpdate.value);
			System.out.println(object.toString());
			writer.write(object.toString());
			writer.flush();
			writer.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = in.readLine()) != null){
				response.append(inputLine);
			}
			in.close();
		} 
		catch (MalformedURLException e) {} 
		catch (IOException e) {e.printStackTrace();}
		catch (JSONException e){e.printStackTrace();}
	}
	
	private void pushToSmartDashboard(Update<?> currentUpdate){
		if(currentUpdate.value instanceof Number){
			Update<Double> numberUpdate = currentUpdate.toDoubleUpdate();
			SmartDashboard.putNumber(numberUpdate.variable, numberUpdate.value);
		}
		else{ //Treat it as a string
			Update<String> stringUpdate = currentUpdate.toStringUpdate();
			SmartDashboard.putString(stringUpdate.variable, stringUpdate.value);
		}
	}
	
	private void pushToNetworkTables(Update<?> currentUpdate){
		if(currentUpdate.value instanceof Number){
			Update<Double> numberUpdate = currentUpdate.toDoubleUpdate();
			netTable.putNumber(numberUpdate.variable, numberUpdate.value);
		}
		else if(currentUpdate.value instanceof Number[]){
			Update<double[]> numberArrayUpdate = currentUpdate.toDoubleArrayUpdate();
			netTable.putNumberArray(numberArrayUpdate.variable, numberArrayUpdate.value);
		}
		else{ //Treat it as a string
			Update<String> stringUpdate = currentUpdate.toStringUpdate();
			netTable.putString(stringUpdate.variable, stringUpdate.value);
		}
	}
	
	public static void restart(){
		if(instance != null) instance.running = false;
		instance = null;
	}
	
	public static void start(){
		if(instanceThread == null){
			instanceThread = new Thread(getInstance());
			instanceThread.start();
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
		String variable;
		E value;
		Update(String var, E val) {
			this.variable = var;
			this.value = val;
		}
		Update<Double> toDoubleUpdate(){
			if(value instanceof Number){
				return new Update<Double>(variable, ((Number)value).doubleValue());
			}
			return null;
		}
		Update<double[]> toDoubleArrayUpdate(){
			if(value instanceof Number[]){
				Number[] numberArrayValue = ((Number [])(value));
				double[] doubleArrayValue = new double[numberArrayValue.length];
				for(int i = 0; i < numberArrayValue.length; i++){
					doubleArrayValue[i] = numberArrayValue[i].doubleValue();
				}
				return new Update<double[]>(variable, doubleArrayValue);
			}
			return null;
		}
		Update<String> toStringUpdate(){
			return new Update<String>(variable, value.toString());
		}

	}
	
	public static String getNetworkTablesString(String key){
		return getInstance().netTable.getString(key, null);
	}

	public static Double getNetworkTablesNumber(String key){
		return getInstance().netTable.getNumber(key, new Double(0));
	}

	
	public static void main(String[] args){
		start();
		for(int i = 0; i < 20; i++){
			putNumber("number", i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}

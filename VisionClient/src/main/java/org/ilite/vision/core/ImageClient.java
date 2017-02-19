package org.ilite.vision.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.usfirst.frc.team1885.visioncode.utils.ImageData;


public class ImageClient {

	public ImageClient (int port) throws UnknownHostException, IOException, ClassNotFoundException {

	    final Socket aSocket = new Socket("raspberrypi.local", port);
	    System.out.println("Got socket connection!");
        final ObjectInputStream  anObject = new ObjectInputStream(aSocket.getInputStream());
        System.out.println("Got object inputstream");

		ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
		        
		        Object readObject;
				try{
					readObject = anObject.readObject();
					System.out.println(readObject);
					if(readObject instanceof ImageData) {
						ImageData iObject = (ImageData)readObject;
						System.out.println("The X value is: " + iObject.getX() + " and the "
						        + "Y value is: " + iObject.getY());
						//Do stuff with image
					} else {
					    System.out.println("Got something else");
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, 0, 100, TimeUnit.MILLISECONDS);
		System.out.println("Donarino");
	}

public static void main(String [] args) throws UnknownHostException, ClassNotFoundException, IOException {
	
	ImageClient aClient = new ImageClient(1180);
	
}
}


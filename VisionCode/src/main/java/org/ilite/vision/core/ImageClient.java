package org.ilite.vision.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.usfirst.frc.team1885.visioncode.utils.SimpleImage;

	
public class ImageClient {
	
	public ImageClient (int port) throws UnknownHostException, IOException, ClassNotFoundException {
		Socket aSocket = new Socket("localhost", 1180);
		
		InputStream inputStream2 = aSocket.getInputStream();
		ExecutorService exce = Executors.newSingleThreadExecutor();
		final ObjectInputStream  anObject = new ObjectInputStream(aSocket.getInputStream());
		exce.submit(new Runnable() {

			@Override
			public void run() {
			}
			
		});
	ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
	scheduledExecutor.scheduleAtFixedRate(new Runnable() {

		@Override
		public void run() {
			Object readObject;
			try {
				readObject = anObject.readObject();
				if(readObject instanceof SimpleImage) {
					SimpleImage anObject = (SimpleImage)readObject;
					//Do stuff with image
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
    }
	}

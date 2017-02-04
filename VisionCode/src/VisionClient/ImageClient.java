package VisionClient;

import org.opencv.core.Mat;
import VisionClient.simple_image;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.*;
import javax.imageio.ImageIO;

	
public class ImageClient {
	
	public class simple_image {
		int numChannels;
		int rows, cols;
		byte [] rawImage;
		public int getNumChannels() {return numChannels;}
		public void getNumChannels(int numChannels) {this.numChannels = numChannels;}
		public int getRows() {return rows;}
		public void setRows(int rows) {this.rows = rows;}
		public int getCols() {return cols;}
		public void setCols(int cols) {this.cols = cols;}
		public byte[] getRawImage() {return rawImage;}
		public void setRawImage(byte[] rawImage) {
			this.rawImage = rawImage;
		}

	}


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
            Object readObject = anObject.readObject();
            if(readObject instanceof simple_image) {
                simple_image anObject = (simple_image)anObject;
                //Do stuff with image
            }
        }
    }, 0, 100, TimeUnit.MILLISECONDS);
    }
	}

package edu.ilitie.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class VisionMain {

	/**
	 * To get this to work: 
	 * 1) Download opencv 3.1: http://opencv.org/downloads.html
	 * 2) Extract the file that's downloaded 
	 * 3) Set the JVM argument: -Djava.library.path=<download location>/opencv/build/java/x64
	 * @param args
	 * 	One argument, the path to the image
	 */
	public static void main(String[] args) {
		//Load an image from a given path
		String imagePath = args[0];
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat imread = Imgcodecs.imread(imagePath);
		if(imread != null) {
			System.out.println("NOT NULL!");
		} else {
			System.out.println("NULL");
		}
	}
}

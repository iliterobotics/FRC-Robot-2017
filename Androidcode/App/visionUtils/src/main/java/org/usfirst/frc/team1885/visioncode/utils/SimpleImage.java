package org.usfirst.frc.team1885.visioncode.utils;

public class SimpleImage {
	
	int numChannels;
	int rows,cols;
	byte [] rawImage;
	public int getNumChannels() {
		return numChannels;
	}
	public void setNumChannels(int numChannels) {
		this.numChannels = numChannels;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public byte[] getRawImage() {
		return rawImage;
	}
	public void setRawImage(byte[] rawImage) {
		this.rawImage = rawImage;
	} 

}

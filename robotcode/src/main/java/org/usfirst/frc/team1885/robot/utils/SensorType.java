package org.usfirst.frc.team1885.robot.utils;

public enum SensorType {
	ENCODER(Double.MIN_VALUE, Double.MAX_VALUE, false), GYRO(-180, 180, true);
	
	double minValue, maxValue;
	boolean continuous;
	
	private SensorType(double minValue, double maxValue, boolean continuous) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.continuous = continuous;
	}
}

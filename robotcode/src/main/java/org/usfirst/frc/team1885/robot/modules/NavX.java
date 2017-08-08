package org.usfirst.frc.team1885.robot.modules;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort;

public class NavX{
	
	private static final SerialPort.Port DEFAULT_PORT = SerialPort.Port.kMXP;
	private double initialAngle;
	private final AHRS ahrs;
	
	
	public NavX() {
		this.ahrs = new AHRS(DEFAULT_PORT);
		initialAngle = ahrs.getYaw();
	}

	public double getInitialAngle() {
		return initialAngle;
	}

	public double getYaw() {
		return ahrs.getYaw();
	}

	public double getDisplacementX() {
		return ahrs.getDisplacementX();
	}
	
	public double getDisplacementY() {
		return ahrs.getDisplacementY();
	}
	
	public double getDisplacementZ() {
		return ahrs.getDisplacementZ();
	}

	public void resetDisplacement() {
		ahrs.resetDisplacement();
	}
	
	public boolean isCalibrating(){
		return ahrs.isCalibrating();
	}
		
	public double getAngle(){
		return convertTo360(ahrs.getAngle());
	}
	
	public double getAngleFromInitial(){
		return getAngleSum(getAngle(), -initialAngle);
	}
	
	public void setInitialAngle(double yaw){
		initialAngle = yaw;
	}
	
	public void resetInitialAngle() {
		initialAngle = getYaw();
	}

	private double convertTo360(double angle){
		if(angle < 0) return angle + 360;
		return angle;
	}
	
	public double getAngleSum(double angle1, double angle2) {
		double sum = angle1 + angle2;
		if(sum > 180){
			sum = -360 + sum;
		} else if(sum < -180){
			sum = 360 + sum;
		}
		return sum;
	}
	
	public double getAngleDiff(double angle1, double angle2){
		return getAngleSum(angle1, -angle2);
	}

}

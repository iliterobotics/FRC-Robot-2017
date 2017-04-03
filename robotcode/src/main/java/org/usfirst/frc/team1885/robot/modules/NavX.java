package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.common.impl.DefaultAHRSFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.ESerialPort;
import org.usfirst.frc.team1885.robot.common.interfaces.IAHRS;
import org.usfirst.frc.team1885.robot.common.interfaces.IAHRSFactory;

public class NavX{
	
	private static final ESerialPort DEFAULT_PORT = ESerialPort.kMXP;
	private double initialAngle;
	private final IAHRS iahrs;
	
	public NavX(){
		this(new DefaultAHRSFactory());
	}
	
	public NavX(IAHRSFactory factory) {
		this.iahrs = factory.getAHRS(DEFAULT_PORT);
		initialAngle = iahrs.getYaw();
	}

	public double getInitialAngle() {
		return initialAngle;
	}

	public double getYaw() {
		return iahrs.getYaw();
	}

	public double getDisplacementX() {
		return iahrs.getDisplacementX();
	}
	
	public double getDisplacementY() {
		return iahrs.getDisplacementY();
	}
	
	public double getDisplacementZ() {
		return iahrs.getDisplacementZ();
	}

	public void resetDisplacement() {
		iahrs.resetDisplacement();
	}
	
	public float getWorldLinearAccelZ() {
		return iahrs.getWorldLinearAccelZ();
	}
	
	public boolean isCalibrating(){
		return iahrs.isCalibrating();
	}
		
	public double getAngle(){
		return convertTo360(iahrs.getAngle());
	}
	
	public double getAngleOffStart(){
		return getAngleSum(getAngle(), -initialAngle);
	}
	
	public void setInitialAngle(double yaw){
		initialAngle = yaw;
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
	
	public double getAngleDistance(double angle1, double angle2){
		return getAngleSum(angle1, -angle2);
	}

}

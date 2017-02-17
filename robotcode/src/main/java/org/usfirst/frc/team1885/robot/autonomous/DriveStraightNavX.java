package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class DriveStraightNavX extends AutonomousCommand{
	
	private static final double INITIAL_POWER = 0.4;
	private static final double PROPORTION = 0.02;
	
	private final DriveTrain driveTrain;
	private final NavX navx;
	private final int distanceToTravel;
	
	private int initialLeftPosition;
	private int initialRightPosition;
	
	private double initialYaw;
	
	public DriveStraightNavX(DriveTrain dt, NavX navx, double footDistance){
		this.driveTrain = dt;
		this.navx = navx;
		this.distanceToTravel = feetToTicks(footDistance);
	}
	
	public int feetToTicks(double feet){
		double rotations = feet / (Math.PI * DriveTrain.WHEEL_DIAMETER);
		return (int)(rotations * 1024);
	}
	
	public void init(){
		initialYaw = navx.getYaw();
		initialLeftPosition = driveTrain.getLeftPosition();
		initialRightPosition = driveTrain.getRightPosition();
		System.out.println("Initial Yaw:" + navx.getYaw());
	}
	
	public boolean update(){

		if( getAverageDistanceTravel() >= distanceToTravel){
			driveTrain.setPower(0, 0);
			DriverStation.reportError("I AM STOPPING", false);
			return true;
		}

		double yawError = initialYaw - navx.getYaw();
		driveTrain.setPower(-(INITIAL_POWER + yawError * PROPORTION), -(INITIAL_POWER - yawError * PROPORTION));
		
		System.out.printf("YawDiff:%f\tDistError:%d\n", yawError, getAverageDistanceTravel());
		
		return false;
	}
	
	public double getDisplacement(){
		return Math.sqrt(Math.pow(navx.getDisplacementX(), 2) +
						 Math.pow(navx.getDisplacementY(), 2));
	}
	
	private int getAverageDistanceTravel(){
		return Math.abs(driveTrain.getLeftPosition() - initialLeftPosition) + 
			   Math.abs(driveTrain.getRightPosition() - initialRightPosition)/2;
	}

}

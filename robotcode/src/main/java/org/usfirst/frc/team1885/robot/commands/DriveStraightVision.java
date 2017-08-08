package org.usfirst.frc.team1885.robot.commands;

import static org.usfirst.frc.team1885.robot.commands.DriveStraight.PROPORTION;

import org.usfirst.frc.team1885.coms.ConstantUpdater;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class DriveStraightVision extends Command{

	private static final double INITIAL_POWER = 0.4;
	
	private final DriveTrain driveTrain;
	private final NavX navx;
	private final int distanceToTravel;
	
	private int initialLeftPosition;
	private int initialRightPosition;
	
	private double initialYaw;
	
	Joystick testStick;

	
	public DriveStraightVision(DriveTrain dt, NavX navx, double footDistance){
		this.driveTrain = dt;
		this.navx = navx;
		this.distanceToTravel = feetToTicks(footDistance);
		testStick = new Joystick(2);
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
		System.out.printf("InitL:%d InitR:%d\n", driveTrain.getLeftPosition(), driveTrain.getRightPosition());
	}
	
	public boolean update(){
		
		double visionAngle = ConstantUpdater.getNetworkTablesNumber("vision-angle");
		ConstantUpdater.putNumber("vision-angle", visionAngle);
		System.out.println("VISION ANGLE: " + visionAngle);
		if(Math.abs(visionAngle) > 30) visionAngle = 0;
		adjustBearing(visionAngle);
		
		if( getAverageDistanceTravel() >= distanceToTravel){
			driveTrain.setPower(0, 0);
			DriverStation.reportError("I AM STOPPING", false);
			System.out.printf("FinalL:%d FinalR:%d DistTravelled:%d Target:%d\n", driveTrain.getLeftPosition(), driveTrain.getRightPosition(), getAverageDistanceTravel(), distanceToTravel);
			return true;
		}

		double yawError = navx.getAngleDiff(initialYaw, navx.getYaw());
		driveTrain.setPower(-(INITIAL_POWER + yawError * PROPORTION), -(INITIAL_POWER - yawError * PROPORTION));
		
		return false;
	}
	
	private int getAverageDistanceTravel(){
		return (Math.abs(driveTrain.getLeftPosition() - initialLeftPosition) + 
			   Math.abs(driveTrain.getRightPosition() - initialRightPosition))/2;
	}
	
	public void adjustBearing(double angleDiff){
		initialYaw = navx.getAngleSum(initialYaw, angleDiff);
	}

}

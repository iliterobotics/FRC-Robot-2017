package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriveTrain.DriveMode;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class DriveStraightNavX extends AutonomousCommand{
	
	private static final double WHEEL_DIAMETER = 4; //In inches
	private static final double TICKS_PER_TURN = 1024;
	private static final double DISTANCE_PER_TICK = 4 / TICKS_PER_TURN;
	
	private static final double ALLOWABLE_ALIGNMENT_ERROR = 0.001;
	private static final int ALLOWABLE_DISTANCE_ERROR = 1;
	private static final double INITIAL_POWER = 0.3;
	private static final double PROPORTION = 0.02;
	
	private final DriveTrain driveTrain;
	private final NavX navx;
	
	private double initialYaw;
	private boolean aligned;
	
	private double distance;
	
	private int initialLeftEncPosition;
	private int initialRightEncPosition;
	private int leftEncError;
	private int rightEncError;
	private int averageError;
	
	public DriveStraightNavX(DriveTrain dt, NavX navx, double distance){
		this.driveTrain = dt;
		this.navx = navx;
		this.aligned = false;
		this.distance = distance;
	}
	
	public void init(){
		initialYaw = navx.getAngle();
		
		initialLeftEncPosition = driveTrain.getLeftEncoderPosition();
		initialRightEncPosition = driveTrain.getRightEncoderPosition();
		leftEncError = convertToTicks(distance) - (driveTrain.getLeftEncoderPosition() - initialLeftEncPosition);
		rightEncError = convertToTicks(distance) - (driveTrain.getRightEncoderPosition() - initialRightEncPosition);
		averageError = (leftEncError + rightEncError) / 2;
		
		driveTrain.setMode(DriveMode.DRIVER_CONTROL_LOW);
	}
	
	public boolean update() {
		
		leftEncError = convertToTicks(distance) - (driveTrain.getLeftEncoderPosition() - initialLeftEncPosition);
		rightEncError = convertToTicks(distance) - (driveTrain.getRightEncoderPosition() - initialRightEncPosition);
		averageError = (leftEncError + rightEncError) / 2;
		
		if(Math.abs(initialYaw) < ALLOWABLE_ALIGNMENT_ERROR){
			initialYaw = navx.getAngle();
			return false;
		}
		
		if(Math.abs(averageError) < ALLOWABLE_DISTANCE_ERROR) return true;
		
		double yawError = initialYaw - navx.getAngle();
		driveTrain.setMotors(-(INITIAL_POWER + yawError * PROPORTION), -(INITIAL_POWER - yawError * PROPORTION));
		aligned = false;
		
		DriverStation.reportError(String.format("Yaw Diff:%f, X:%f, Y:%f, Z:%f", yawError, navx.getDisplacementX(), navx.getDisplacementY(), navx.getDisplacementZ()), false);
		
		return false;
	}
	
	public double getDisplacement(){
		return Math.sqrt(Math.pow(navx.getDisplacementX(), 2) +
						 Math.pow(navx.getDisplacementY(), 2));
	}
	
	public double convertToInches(double ticks) {
		double inches = ticks * DISTANCE_PER_TICK;
		return inches;
	}
	
	public int convertToTicks(double distance)
	{
		return (int) (distance / DISTANCE_PER_TICK);
	}

}

package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class DriveStraightNavX extends AutonomousCommand{
	
	private static final double ALLOWABLE_ERROR = 0.001;
	private static final double INITIAL_POWER = 0.3;
=======
	private static final double INITIAL_POWER = 0.4;
>>>>>>> Stashed changes
	private static final double PROPORTION = 0.02;
	
	private final DriveTrain driveTrain;
	private final NavX navx;
	private final int distanceToTravel;
	
	private int initialLeftPosition;
	private int initialRightPosition;
	
	private double initialYaw;
	
	public DriveStraightNavX(DriveTrain dt, NavX navx, int tickDistance){
		this.driveTrain = dt;
		this.navx = navx;
		this.distanceToTravel = tickDistance;
	}
	
	public void init(){
		initialYaw = navx.getYaw();
		initialLeftPosition = driveTrain.getLeftPosition();
		initialRightPosition = driveTrain.getRightPosition();
	}
	
	public boolean update(){
		if(Math.abs(initialYaw) < ALLOWABLE_ERROR){
			initialYaw = navx.getYaw();
			return false;
		}
		double yawError = initialYaw - navx.getYaw();
		driveTrain.setPower(-(INITIAL_POWER + yawError * PROPORTION), -(INITIAL_POWER - yawError * PROPORTION));
		
		DriverStation.reportError(String.format("Yaw Diff:%f, X:%f, Y:%f, Z:%f", yawError, navx.getDisplacementX(), navx.getDisplacementY(), navx.getDisplacementZ()), false);
		
		return getAverageDistanceTravel() >= distanceToTravel;
	}
<<<<<<< Updated upstream
	
	public double getDisplacement(){
		return Math.sqrt(Math.pow(navx.getDisplacementX(), 2) +
						 Math.pow(navx.getDisplacementY(), 2));
	}
	
	private int getAverageDistanceTravel(){
		return ((driveTrain.getLeftPosition() - initialLeftPosition) + 
			   (driveTrain.getRightPosition() - initialRightPosition))/2;
	}

}

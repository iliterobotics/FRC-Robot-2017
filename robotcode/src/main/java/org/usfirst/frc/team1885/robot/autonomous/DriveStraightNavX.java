package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriveTrain.DriveMode;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class DriveStraightNavX extends AutonomousCommand{
	
	private static final double ALLOWABLE_ERROR = 0.001;
	private static final double INITIAL_POWER = 0.3;
	private static final double PROPORTION = 0.02;
	
	private final DriveTrain driveTrain;
	private final NavX navx;
	
	private double initialYaw;
	
	public DriveStraightNavX(DriveTrain dt, NavX navx){
		driveTrain = dt;
		this.navx = navx;
	}
	
	public void init(){
		initialYaw = navx.getAngle();
		driveTrain.setMode(DriveMode.DRIVER_CONTROL_LOW);
	}
	
	public boolean update(){
		if(Math.abs(initialYaw) < ALLOWABLE_ERROR){
			initialYaw = navx.getAngle();
			return false;
		}
		double yawError = initialYaw - navx.getAngle();
		driveTrain.setMotors(-(INITIAL_POWER + yawError * PROPORTION), -(INITIAL_POWER - yawError * PROPORTION));
		
		DriverStation.reportError(String.format("Yaw Diff:%f, X:%f, Y:%f, Z:%f", yawError, navx.getDisplacementX(), navx.getDisplacementY(), navx.getDisplacementZ()), false);
		
		return false;
	}
	
	public double getDisplacement(){
		return Math.sqrt(Math.pow(navx.getDisplacementX(), 2) +
						 Math.pow(navx.getDisplacementY(), 2));
	}

}

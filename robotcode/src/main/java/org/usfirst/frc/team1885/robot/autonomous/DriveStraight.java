package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class DriveStraight extends Command{
	
	public final double INITIAL_POWER;
	public static final double PROPORTION = 0.02;
	
	private final DriveTrain driveTrain;
	private final NavX navx;
	
	private double initialYaw;
	
	public DriveStraight(DriveTrain dt, NavX navx, double initialPower){
		this.driveTrain = dt;
		this.navx = navx;
		this.INITIAL_POWER = initialPower;
	}
	
	public void init(){
		initialYaw = navx.getYaw();
	}
	
	public boolean update(){
		System.out.println("Command printing");

		double yawError = navx.getAngleDistance(initialYaw, navx.getYaw());
		driveTrain.setPower(-(INITIAL_POWER + yawError * PROPORTION), -(INITIAL_POWER - yawError * PROPORTION));
		
		return false;
	}
	
	public void adjustBearing(double angleDiff){
		initialYaw = navx.getAngleSum(initialYaw, angleDiff);
	}
	
}

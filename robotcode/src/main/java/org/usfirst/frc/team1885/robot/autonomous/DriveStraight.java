package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class DriveStraight extends Command{
	
	public static final double INITIAL_POWER = 0.4;
	public static final double PROPORTION = 0.02;
	
	private final DriveTrain driveTrain;
	private final NavX navx;
	
	private double initialYaw;
	
	public DriveStraight(DriveTrain dt, NavX navx){
		this.driveTrain = dt;
		this.navx = navx;
	}
	
	public void init(){
		initialYaw = navx.getYaw();
		System.out.println("Initial Yaw:" + navx.getYaw());
	}
	
	public boolean update(){

		double yawError = initialYaw - navx.getYaw();
		driveTrain.setPower(-(INITIAL_POWER + yawError * PROPORTION), -(INITIAL_POWER - yawError * PROPORTION));
		
		return false;
	}
	
}

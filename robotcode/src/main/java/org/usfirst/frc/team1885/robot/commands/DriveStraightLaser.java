package org.usfirst.frc.team1885.robot.commands;

import static org.usfirst.frc.team1885.robot.commands.DriveStraight.PROPORTION;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.Lidar;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class DriveStraightLaser extends DriveStraight
{

	private DriveTrain driveTrain;
	private NavX navx;
	private Lidar lidar;
	private double initialDistance;
	private double distanceCentimeters;
	private double error;
	
	public DriveStraightLaser(DriveTrain dt, NavX navx, Lidar lidar, double initialPower, double distanceCentimeters) {
		super(dt, navx, initialPower);
		this.driveTrain = dt;
		this.navx = navx;
		this.lidar = lidar;
		this.initialDistance = lidar.getCentimeters();
		this.distanceCentimeters = initialDistance - distanceCentimeters;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean update() {
		if( lidar.getCentimeters() <= distanceCentimeters){
			driveTrain.setPower(0, 0);
			DriverStation.reportError("I AM STOPPING", false);
			return true;
		}

		double yawError = navx.getAngleDiff(super.getInitialYaw(), navx.getYaw());
		driveTrain.setPower(-(INITIAL_POWER + yawError * PROPORTION), -(INITIAL_POWER - yawError * PROPORTION));
		
		return false;
	}
	
	

}

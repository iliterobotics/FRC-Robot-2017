package org.usfirst.frc.team1885.robot.commands;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.Lidar;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class AlignWIthWallLidar2 extends Command{

	private int targetDistFromWall;
	private int currentDistFromWall;
	private DriveTrain dt;
	private NavX navx;
	private double initialPower;
	private Lidar lidar;
	
	private final double PROPORTION = 0.03;
	public AlignWIthWallLidar2(DriveTrain dt, NavX navx, double initialPower, Lidar lidar, int target) {
		
		this.lidar = lidar;
		this.dt = dt;
		this.navx = navx;
		this.initialPower = initialPower;
		this.targetDistFromWall = target;
	}
	
	
	public void init()
	{
		currentDistFromWall = lidar.getCentimeters();
	}
	
	public int getRealDistance()
	{
		currentDistFromWall = lidar.getCentimeters();
		int angleB = (int)  ( Math.sin(180 - 90 - navx.getYaw() ) )  ;
		return currentDistFromWall * angleB - targetDistFromWall;
	}
	
	public boolean update()
	{
		
		dt.setPower(initialPower - (PROPORTION * getRealDistance() ), initialPower + (PROPORTION * getRealDistance() ));
		return false;
	}
	

}

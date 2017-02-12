package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.NavX;

public class WaitForNavxCalibration extends AutonomousCommand{

	private final NavX navx;
	
	public WaitForNavxCalibration(NavX navx){
		this.navx = navx;
	}
	
	public void init() {

	}

	@Override
	public boolean update() {
		return !navx.isCalibrating();
	}

}

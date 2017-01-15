package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;

public class DriveStraight extends AutonomousCommand{

	private final DriveTrain driveTrain;
	
	public DriveStraight(DriveTrain driveTrain){
		this.driveTrain = driveTrain;
	}
	
	@Override
	public void init() {
		//driveTrain.setMode(DriveMode.);
	}

	@Override
	public boolean update() {
		return false;
	}

}

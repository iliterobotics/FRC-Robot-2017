package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriveTrain.DriveMode;
import org.usfirst.frc.team1885.robot.modules.NavX;

import com.kauailabs.navx.frc.AHRS;

public class TurnToDegree extends AutonomousCommand{
	private DriveTrain driveTrain;
	private NavX navx;
	private double targetYaw;
	private double yawError;
	private double initialYaw;
	private int turnDirection;
	
	public TurnToDegree(DriveTrain driveTrain, NavX navx, double targetYaw) {
		this.driveTrain = driveTrain;
		this.navx = navx;
		this.targetYaw = targetYaw;
	}

	@Override
	public void init() {
		driveTrain.setMode(DriveMode.DRIVER_CONTROL);
		initialYaw = navx.getInitialYaw();
	}

	@Override
	public boolean update() {
		double leftDrive, rightDrive;
		
		return false;
	}
}

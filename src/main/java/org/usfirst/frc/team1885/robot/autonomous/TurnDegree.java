package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriveTrain.DriveMode;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;

public class TurnDegree extends AutonomousCommand {
	private DriveTrain driveTrain;
	private AHRS navx;
	
	private double targetYaw;
	private int turnDirection;
	
	public TurnDegree(DriveTrain driveTrain, AHRS navx, double targetYaw) {
		this.driveTrain = driveTrain;
		this.navx = navx;
		this.targetYaw = targetYaw;
	}
	
	@Override
	public void init() {
		driveTrain.setMode(DriveMode.DRIVER_CONTROL_LOW);
	}

	@Override
	public boolean update() {
		double leftDrive;
		double rightDrive;		
		if(Math.abs(navx.getYaw()) < targetYaw) {
			leftDrive = turnDirection < 0 ? -(navx.getYaw()/targetYaw) : (navx.getYaw()/targetYaw);
			rightDrive = turnDirection > 0 ? -(navx.getYaw()/targetYaw) : (navx.getYaw()/targetYaw);
		}	
		else {
			leftDrive = rightDrive = 0.0;
		}
		DriverStation.reportError(String.format("Left: %f Right: %f", leftDrive, rightDrive), false);
		driveTrain.setMotors(leftDrive, rightDrive);
		return false;
	}
	
}

package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriveTrain.DriveMode;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;

public class TurnDegree extends AutonomousCommand {
	public static final int SPIN_LEFT = -1;
	public static final int SPIN_RIGHT = 1;
	private static final double power = 0.5;
	
	private DriveTrain driveTrain;
	private AHRS navx;
	
	private double initialYaw;
	private double targetYaw;
	private int spinDirection;
	
	public TurnDegree(DriveTrain driveTrain, AHRS navx, double targetYaw, int spinDirection) {
		this.driveTrain = driveTrain;
		this.navx = navx;
		this.targetYaw = targetYaw * spinDirection;
		this.spinDirection = spinDirection;
	}
	
	@Override
	public void init() {
		initialYaw = navx.getYaw();
		driveTrain.setMode(DriveMode.DRIVER_CONTROL_LOW);
	}

	@Override
	public boolean update() {
		double leftDrive;
		double rightDrive;
		DriverStation.reportError(String.format("Yaw: %f", navx.getYaw()), false);
		if(navx.getYaw() < targetYaw) {
			leftDrive = spinDirection == SPIN_LEFT ? -power : power;
			rightDrive = spinDirection == SPIN_RIGHT ? -power : power;
		}	
		else {
			leftDrive = rightDrive = 0;
		}
		driveTrain.setMotors(leftDrive, rightDrive);
		return false;
	}
	
}

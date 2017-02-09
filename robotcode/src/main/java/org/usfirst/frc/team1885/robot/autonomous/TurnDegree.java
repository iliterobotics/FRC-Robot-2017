package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class TurnDegree extends AutonomousCommand {
	
	private static final double MAX_ERROR = 0.001;
	
	private DriveTrain driveTrain;
	private NavX navx;
	
	private double currYaw;
	private double initialYaw;
	private double targetYaw;
	private double yawError;
	
	private double reducer = 1;
	private double proportion;
	private int turnDirection;
	
	public TurnDegree(DriveTrain driveTrain, NavX navx, double degrees) {
		this.driveTrain = driveTrain;
		this.navx = navx;
		
		this.currYaw = navx.getYaw();
		this.initialYaw = navx.getYaw();
		this.targetYaw = (degrees + initialYaw) % 180;
		this.yawError = Math.abs(targetYaw) - Math.abs(initialYaw);
		
		this.proportion = yawError / currYaw;
		this.turnDirection = (this.initialYaw < this.targetYaw) ? 1 : -1;
	}
	
	@Override
	public void init() {
		navx.zeroYaw();
	}

	@Override
	public boolean update() {
		
		double leftDrive;
		double rightDrive;
		
		currYaw = navx.getYaw();
		
		yawError = Math.abs(targetYaw) - Math.abs(navx.getYaw());
		proportion = (yawError * invert(currYaw)) * reducer;
		
//		reducer = (navx.getYaw() / targetYaw) * MAX_REDUCER;
		
		if(!isWithinError(currYaw, targetYaw, MAX_ERROR)) {
			leftDrive = turnDirection * proportion;
			rightDrive = -turnDirection * proportion;
			DriverStation.reportError(String.format("Turning to Target Yaw: %f Current Yaw: %f Proportion: %f Left: %f Right: %f",
													targetYaw, currYaw, proportion, leftDrive, rightDrive ), false);
		} else {
			DriverStation.reportError(String.format("Reached Target Yaw: %f Current Yaw: ", targetYaw, navx.getYaw()), false);
			leftDrive = rightDrive = 0.0;
			return true;
		}
		
		DriverStation.reportError(String.format("Left: %f Right: %f", leftDrive, rightDrive), false);
		DriverStation.reportError("Yaw: " + currYaw, false);
		
		driveTrain.setPower(rightDrive, leftDrive);
		return false;
		
	}
	
	private boolean isWithinError(double value, double targetValue, double error)
	{
		if( value < (targetValue + error) && value > (targetValue - error) ) return true;
		return false;
	}
	
	/**
	 * 
	 * @param value The value you wish to invert
	 * @return The inverted value (ex: x -> 1/x)
	 */
	private double invert(double value)
	{
		if(value == 0) return 0;
		return 1/value;
	}
	
}

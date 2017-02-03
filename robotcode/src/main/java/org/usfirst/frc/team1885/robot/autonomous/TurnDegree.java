package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class TurnDegree extends AutonomousCommand {
	
	private DriveTrain drivetrain;
	private NavX navx;
	
	private static final double MAX_ERROR = 1;
	private static final double PROPORTION = 0.004006;
	
	private double degrees;
	private double targetYaw;
	private double error;
	
	public TurnDegree(DriveTrain drivetrain, NavX navx, double degrees)
	{
		this.drivetrain = drivetrain;
		this.navx = navx;
		this.degrees = degrees;
	}
	
	public void init()
	{
		navx.zeroYaw(); //Makes sure we will be turning relative to our current heading
		this.targetYaw = (navx.getYaw() + degrees) % 180;  //Calculate the target heading off of # of degrees to turn
		this.error = (targetYaw - navx.getYaw()) % 180; //Calculate the initial error value
		DriverStation.reportError(String.format("Starting TurnDegree. \n Initial Yaw: %f \n Current Yaw: %f \n Target Yaw: %f \n Error: %f", navx.getInitialYaw(), navx.getYaw(), targetYaw, error), false);
	}
	
	public boolean update()
	{
		if(isWithinError(navx.getYaw(), targetYaw, MAX_ERROR)) return true; //Terminate if within error margin
		DriverStation.reportError(String.format("Updating TurnDegree. \n Current Yaw: %f \n Target Yaw: %f \n Error: %f", navx.getYaw(), targetYaw, error), false);
		
		double leftPower, rightPower;
		error = ( targetYaw - navx.getYaw()) % 180; //Update error value
		
		leftPower = (PROPORTION * error); 
		rightPower = -(PROPORTION * error); //So we don't just drive straight
		
		drivetrain.setMotors(leftPower, rightPower);
		DriverStation.reportError(String.format("Left: %f Right %f", leftPower, rightPower), false );
		return false;
	}
	
	/**
	 * 
	 * @param value The value to test.
	 * @param targetValue The value we want the value variable to be at.
	 * @param error How far off can the value be from the target value.
	 * @return Whether the value is equal to the target value +/- error
	 */
	private boolean isWithinError(double value, double targetValue, double error)
	{
		if( value < (targetValue + error) && value > (targetValue - error) ) return true; //Is it within the defined error range?
		DriverStation.reportError(String.format("Value: %f Target: %f Allowable Error: %f", value, targetValue, error), false);
		return false;
	}
	
}

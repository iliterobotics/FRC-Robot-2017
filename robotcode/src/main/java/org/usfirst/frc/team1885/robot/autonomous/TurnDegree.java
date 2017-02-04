package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class TurnDegree extends AutonomousCommand {
	
	private DriveTrain drivetrain;
	private NavX navx;
	
	private static final double MAX_ERROR = 1;
	private static final double KP = 0.0085;
	private static final double KD = 0.01;
	private static final double KI = 0.000005;
	
	private double degrees;
	private double targetYaw;
	private double error;
	private double lastError;
	private double totalError;
	
	public TurnDegree(DriveTrain drivetrain, NavX navx, double degrees)
	{
		this.drivetrain = drivetrain;
		this.navx = navx;
		this.degrees = degrees;
	}
	
	public void init()
	{
		navx.zeroYaw(); //Makes sure we will be turning relative to our current heading
		this.targetYaw = degrees;  //Calculate the target heading off of # of degrees to turn
		this.lastError = this.error = getAngleDifference(navx.getYaw(), targetYaw); //Calculate the initial error value
		this.totalError += this.error;
		DriverStation.reportError(String.format("Starting TurnDegree. \n Initial Yaw: %f \n Current Yaw: %f \n Target Yaw: %f \n Error: %f", navx.getInitialYaw(), navx.getYaw(), targetYaw, error), false);
	}
	
	public boolean update()
	{
		error = getAngleDifference(navx.getYaw(), targetYaw); //Update error value
		this.totalError += this.error; //Update running error total
		DriverStation.reportError("Error:" + error,  false);
		if(Math.abs(error) < MAX_ERROR){
			drivetrain.setMotors(0, 0);
			return true;
		}
		
		double leftPower, rightPower;
		
		double output = (KP * error) + (KD * (error - lastError) + (KI * totalError));
		
		leftPower = output; 
		rightPower = -output;
		
		drivetrain.setMotors(leftPower, rightPower);
		DriverStation.reportError(String.format("Left: %f Right %f", leftPower, rightPower), false );
		
		lastError = error;
		return false;
	}
	
	private double getAngleDifference(double angle1, double angle2){
		double difference = angle1 - angle2;
		if(Math.abs(difference) > 180){
			if(angle1 < 0){
				difference = angle2 - (angle1 + 360);
			}else{
				difference = (angle2 + 360) - angle1;				
			}
		}
		return difference;
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

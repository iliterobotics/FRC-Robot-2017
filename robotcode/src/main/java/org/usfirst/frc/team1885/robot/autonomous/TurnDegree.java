package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class TurnDegree extends AutonomousCommand {
	
	private DriveTrain drivetrain;
	private NavX navx;
	
	private static final double MAX_ERROR = 1;
	private static final double KP = 0.0065;
	private static final double KD = 0.0;
	private static final double KI = 0.000005;
	
	private double degrees, targetYaw;
	private double error, lastError, totalError;
	
	double leftPower, rightPower, output = 0;
	
	public TurnDegree(DriveTrain drivetrain, NavX navx, double degrees)
	{
		this.drivetrain = drivetrain;
		this.navx = navx;
		this.degrees = degrees;
	}
	
	public void init()
	{
		this.targetYaw = getAngleSum(navx.getYaw(), degrees);  //Calculate the target heading off of # of degrees to turn
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
			output = leftPower = rightPower = 0;
			DriverStation.reportError(String.format("Execution complete. Left: %f Right: %f", leftPower, rightPower), false);
			drivetrain.setPower(0, 0);
			return true;
		}
		
		output = (KP * error) + (KD * (error - lastError) + (KI * totalError));
		
		leftPower = output; 
		rightPower = -output;
		
		drivetrain.setPower(leftPower, rightPower);
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
	
	private double getAngleSum(double angle1, double angle2) {
		double difference = angle1 + angle2;
		if(Math.abs(difference) > 180){
			if(angle1 < 0){
				difference = angle2 - (angle1 + 360);
			}else{
				difference = (angle2 + 360) - angle1;				
			}
		}
		return difference;
	}
	
}

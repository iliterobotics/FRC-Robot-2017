package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriveTrain.DriveMode;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class TurnToDegree extends AutonomousCommand {
	
	private DriveTrain drivetrain;
	private NavX navx;
	
	private static final int MIN_ALIGNED_COUNT = 5;
	private static final double MAX_ERROR = 1;
	private static final double KP = 0.006; 
	private static final double KD = 0.00; 
	private static final double KI = 0.0; 
	
	private double degrees, targetYaw;
	private double error, lastError, totalError;
	private double alignedCount;
	
	double leftPower, rightPower, output = 0;
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees)
	{
		this.drivetrain = drivetrain;
		this.navx = navx;
		this.degrees = degrees;
		this.alignedCount = 0;
	}
	
	public void init()
	{
		this.targetYaw = convertTo360(degrees);  //Calculate the target heading off of # of degrees to turn
		this.lastError = this.error = getAngleSum(convertTo360(navx.getAngle()), -convertTo360(targetYaw)); //Calculate the initial error value
		this.totalError += this.error;
	}
	
	public boolean update()
	{
		error = getAngleSum(convertTo360(navx.getAngle()), -convertTo360(targetYaw)); //Update error value
		this.totalError += this.error; //Update running error total
		
		if((Math.abs(error) < MAX_ERROR)) alignedCount++;
		if(alignedCount >= MIN_ALIGNED_COUNT) return true;
		
		output = ((KP * error) + (KI * totalError) + (KD * (error + lastError)));
		leftPower = output; 
		rightPower = -output;
		
		drivetrain.setPower(leftPower, rightPower);
		
		lastError = error;
		return false;
	}
	
	private double convertTo360(double angle){
		if(angle < 0) return angle + 360;
		return angle;
	}
	
	private double getAngleSum(double angle1, double angle2) {
		double sum = angle1 + angle2;
		if(sum > 180){
			sum = -360 + sum;
		} else if(sum < -180){
			sum = 360 + sum;
		}
		return sum;
	}
	
}

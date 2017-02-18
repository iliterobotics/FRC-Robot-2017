package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class TurnToDegree extends Command {
	
	private DriveTrain drivetrain;
	private NavX navx;
	
	private static final int MIN_ALIGNED_COUNT = 5;
	private static final double MAX_ERROR = 1;
	private static final double KP = 0.01; 
	private static final double KD = 0.3; 
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
		this.targetYaw = degrees;  //Calculate the target heading off of # of degrees to turn
		this.lastError = this.error = getError(); //Calculate the initial error value
		this.totalError += this.error;
	}
	
	public boolean update()
	{
		error = getError(); //Update error value
		System.out.println(error);
		this.totalError += this.error; //Update running error total
		
		if((Math.abs(error) < MAX_ERROR)) alignedCount++;
		if(alignedCount >= MIN_ALIGNED_COUNT) return true;
		
		output = ((KP * error) + (KI * totalError) + (KD * (error - lastError)));
		leftPower = output; 
		rightPower = -output;
		
		drivetrain.setPower(leftPower, rightPower);
		
		lastError = error;
		return false;
	}
	
	public double getError(){
		return navx.getAngleDistance(navx.getAngleOffStart(), targetYaw);
	}
	
}

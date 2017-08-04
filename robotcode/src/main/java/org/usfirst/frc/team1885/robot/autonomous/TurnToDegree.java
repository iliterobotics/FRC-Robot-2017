package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class TurnToDegree extends Command {
	
	private DriveTrain drivetrain;
	private NavX navx;
	
	private static final int MIN_ALIGNED_COUNT = 5;
	private static final double kMinOutput = 0.05;
	private static final double kMaxOutput = 1;
	private static final long kTimeout = 3000;
	private static final double KP = 0.0101;
	private static final double kD = 0.0105;
	private static final double kI = 0.0;
	
	private double mP, mI, mD;
	private double degrees, targetYaw;
	private double error, lastError, totalError;
	
	private double alignedCount;
	private final double allowableError, mTimeout;
	private final double mMinOutput, mMaxOutput;
	
	private long startTime;
	
	double leftPower, rightPower, output = 0;
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees, double allowableError, double timeout, double minOutput, double maxOutput, double mP, double mI, double mD)
	{
		this.drivetrain = drivetrain;
		this.navx = navx;
		this.degrees = degrees;
		this.alignedCount = 0;
		this.allowableError = allowableError;
		this.mTimeout = timeout;
		this.mMinOutput = minOutput;
		this.mMaxOutput = maxOutput;
		this.mP = mP;
		this.mI = mI;
		this.mD = mD;
	}
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees, double allowableError, double timeout, double mP, double mI, double mD)
	{
		this(drivetrain, navx, degrees, allowableError, timeout, kMinOutput, kMaxOutput, mP, mI, mD);
	}
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees, double allowableError) {
		this(drivetrain, navx, degrees, allowableError, kTimeout, kMinOutput, kMaxOutput, KP, kI, kD);
	}
	
	public void init()
	{
		this.targetYaw = degrees;  //Calculate the target heading off of # of degrees to turn
		this.lastError = this.error = getError(); //Calculate the initial error value
		this.totalError += this.error;
		startTime = System.currentTimeMillis();
	}
	
	public boolean update()
	{
		error = getError(); //Update error value
		System.out.println(error);
		this.totalError += this.error; //Update running error total
		
		if((Math.abs(error) < allowableError)) alignedCount++;
		if(alignedCount >= MIN_ALIGNED_COUNT) return true;
		if(System.currentTimeMillis() - startTime > mTimeout) return true;
		
		output = ((mP * error) + (mI * totalError) + (mD * (error - lastError)));
		
		if(Math.abs(output) < mMinOutput){
			double scalar = output > 0 ? 1 : -1;
			output = mMinOutput * scalar;
		}
		
		if(Math.abs(output) > mMaxOutput) {
			double scalar = output > 0 ? 1 : -1;
			output = mMinOutput * scalar;
		}
		
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

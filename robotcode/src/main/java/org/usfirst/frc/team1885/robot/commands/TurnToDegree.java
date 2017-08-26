package org.usfirst.frc.team1885.robot.commands;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class TurnToDegree extends Command {
	
	private static final int TIMEOUT = 3000;
	
	private DriveTrain drivetrain;
	private NavX navx;
	
	private static final double GYRO_TURN_THRESHOLD_TICKS = 1024;
	private static final int MIN_ALIGNED_COUNT = 5;
	private static final double MINIMUM_POWER = 0.05;
	private static final double KP = 0.0111;
	private static final double KD = 0.0085;
	private static final double KI = 0.0;
	
	private double mP, mI, mD;
	private double degrees, targetYaw;
	private double error, avgTickError, lastError, totalError;
	private double alignedCount;
	private final double allowableError;
	
	private double turnDistanceTicks;
	private boolean finished, gyroTurnStarted;
	
	private long startTime;
	
	double leftPower, rightPower, output = 0;
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees, double allowableError, double mP, double mI, double mD)
	{
		this.drivetrain = drivetrain;
		this.navx = navx;
		this.degrees = degrees;
		this.alignedCount = 0;
		this.allowableError = allowableError;
		this.finished = false;
		this.gyroTurnStarted = false;
		this.mP = mP;
		this.mI = mI;
		this.mD = mD;
	}
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees, double allowableError) {
		this(drivetrain, navx, degrees, allowableError, KP, KI, KD);
	}
	
	public void init()
	{
		turnDistanceTicks = DriveTrain.TICKS_PER_DEGREE * degrees;
	}
	
	private void initGyroTurn() {
		navx.resetInitialAngle();
		this.targetYaw = degrees;  //Calculate the target heading off of # of degrees to turn
		this.lastError = this.error = getError(); //Calculate the initial error value
		this.totalError += this.error;
		startTime = System.currentTimeMillis();
	}
	
	public boolean update()
	{
		error = getError(); //Update error value
		avgTickError = (drivetrain.getLeftClosedLoopError() + (-1 * drivetrain.getRightClosedLoopError())) / 2;
		
		drivetrain.changeTrapezoidalPosition(-turnDistanceTicks, turnDistanceTicks);
		turnDistanceTicks = 0; //Don't change our position after initial call
		
		if(Math.abs(avgTickError) < GYRO_TURN_THRESHOLD_TICKS) { //Start closed-loop w/ gyro if within certain degree threshold
			if(!gyroTurnStarted) {
				gyroTurnStarted = true;
				initGyroTurn();
			}
			finished = updateGyroTurn(error);
			return finished;
		}
		
		return false;
	}
	
	private boolean updateGyroTurn(double error) {
		this.totalError += this.error; //Update running error total
		
		if((Math.abs(error) < allowableError)) alignedCount++;
		if(alignedCount >= MIN_ALIGNED_COUNT) return true;
		if(System.currentTimeMillis() - startTime > TIMEOUT) return true;
		
		output = ((KP * error) + (KI * totalError) + (KD * (error - lastError)));
		if(Math.abs(output) < MINIMUM_POWER){
			double direction = output>0?1:-1;
			output = MINIMUM_POWER * direction;
		}
		leftPower = output; 
		rightPower = -output;

		System.out.println("Error: " + getError() + " Angle: " + navx.getAngleFromInitial() + " Output: " + output);
		drivetrain.setPower(leftPower, rightPower);
		
		lastError = error;
		return false;
	}
	
	public double getError(){
		return navx.getAngleDiff(navx.getAngleFromInitial(), targetYaw);
	}
	
}

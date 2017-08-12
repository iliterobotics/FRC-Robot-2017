package org.usfirst.frc.team1885.robot.utils;

public class PIDController {
	private static final int MIN_ON_TARGET_COUNT = 5;
	private static final double kMinOutput = 0.05;
	private static final double kMaxOutput = 1;
	private static final long kTimeout = 3000;
	private static final double kP = 0.0101;
	private static final double kD = 0.0105;
	private static final double kI = 0.0;
	
	private double mP, mI, mD;
	private double setpoint;
	private double error, lastError, totalError;
	
	private double onTargetCount;
	private final double allowableError;
	private double mTimeout;
	private double mMinInput, mMaxInput;
	private boolean continuous;
	private double output, mMinOutput, mMaxOutput;
	
	private long startTime;
	private boolean isTimedOut;
	
	public PIDController(double setpoint, double allowableError, double timeout, double minInput, double maxInput, boolean continuous, double minOutput, double maxOutput, double mP, double mI, double mD)
	{
		this.mP = mP;
		this.mI = mI;
		this.mD = mD;
		this.setpoint = setpoint;
		this.onTargetCount = 0;
		this.allowableError = allowableError;
		this.mTimeout = timeout;
		this.mMinInput = minInput;
		this.mMaxInput = maxInput;
		this.continuous = continuous;
		this.mMinOutput = minOutput;
		this.mMaxOutput = maxOutput;
		this.output = 0;
		this.isTimedOut = false;
	}
	
	public PIDController(double setpoint, double allowableError, SensorType sensorType, double timeout, double mP, double mI, double mD)
	{
		this(setpoint, allowableError, timeout, sensorType.minValue, sensorType.maxValue, sensorType.continuous, kMinOutput, kMaxOutput, mP, mI, mD);
	}
	
	public PIDController(double setpoint, double allowableError, SensorType sensorType) {
		this(setpoint, allowableError, kTimeout, sensorType.minValue, sensorType.maxValue, sensorType.continuous, kMinOutput, kMaxOutput, kP, kI, kD);
	}
	
	public void init()
	{
		startTime = System.currentTimeMillis();
	}
	
	public double update(double currentInput)
	{
		
		if(System.currentTimeMillis() - startTime > mTimeout) isTimedOut = true;
		
		error = getError(setpoint, currentInput); //Update error value
		this.totalError += error; //Update running error total
		
		if((Math.abs(error) < allowableError)) onTargetCount++;
		
		output = ((mP * error) + (mI * totalError) + (mD * (error - lastError)));
		
		if(Math.abs(output) < mMinOutput){
			double scalar = output > 0 ? 1 : -1;
			output = mMinOutput * scalar;
		}
		
		if(Math.abs(output) > mMaxOutput) {
			double scalar = output > 0 ? 1 : -1;
			output = mMinOutput * scalar;
		}
		
		lastError = error;
		return output;
	}
	
	public double getError(double setpoint, double currentInput) {
		double error = setpoint - currentInput;
		if(continuous) {
			if(Math.abs(error) > (mMaxInput - mMinInput) / 2) { //Calculate wrap around point
				if(error < 0) {
					error = error - (mMaxInput + mMinInput);
				} 
				if(error > 0) {
					error = (mMaxInput - mMinInput) - error;
				}
			}
		}
		return error;
	}
	
	public boolean onTarget() {
		if(isTimedOut) return true;
		if(onTargetCount >= MIN_ON_TARGET_COUNT) return true;
		if(System.currentTimeMillis() - startTime > mTimeout) return true;
		return false;
	}
}

package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;
import org.usfirst.frc.team1885.robot.utils.PIDController;
import org.usfirst.frc.team1885.robot.utils.SensorType;

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
	
	private PIDController turnController;
	
	private long startTime;
	
	double leftPower, rightPower, output = 0;
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees, double allowableError, double timeout, double mP, double mI, double mD)
	{
		this.drivetrain = drivetrain;
		this.navx = navx;
		this.turnController = new PIDController(degrees, allowableError, SensorType.GYRO, timeout, mP, mI, mD); 
	}
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees, double allowableError) {
		this(drivetrain, navx, degrees, allowableError, kTimeout, KP, kI, kD);
	}
	
	public void init()
	{
		startTime = System.currentTimeMillis();
	}
	
	public boolean update()
	{
		
		double output = turnController.update(navx.getAngleOffStart());
		
		leftPower = output; 
		rightPower = -output;
		
		drivetrain.setPower(leftPower, rightPower);
		return turnController.onTarget();
	}
	
}

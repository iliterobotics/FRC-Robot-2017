package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class TurnToDegree extends Command implements PIDOutput {
	
	private static final int TIMEOUT = 3000;
	
	private DriveTrain drivetrain;
	private NavX navx;
	private PIDController turnController;
	
	private static final int MIN_ALIGNED_COUNT = 5;
	private static final double KP = 0.0101;
	private static final double KD = 0.0105;
	private static final double KI = 0.0;
	
	private double mP, mI, mD;
	private double degrees, targetYaw;
	private double error;
	private double alignedCount;
	private final double allowableError;
	
	private long startTime;
	
	double leftPower, rightPower, output = 0;
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees, double allowableError, double mP, double mI, double mD)
	{
		this.drivetrain = drivetrain;
		this.navx = navx;
		this.degrees = degrees;
		this.alignedCount = 0;
		this.allowableError = allowableError;
		this.mP = mP;
		this.mI = mI;
		this.mD = mD;
		turnController = new PIDController(KP, KI, KD, navx.getAHRS(), this);
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(allowableError);
		turnController.setContinuous(true);
	    LiveWindow.addActuator("DriveSystem", "RotateController", turnController);

	}
	
	public TurnToDegree(DriveTrain drivetrain, NavX navx, double degrees, double allowableError) {
		this(drivetrain, navx, degrees, allowableError, KP, KI, KD);
	}
	
	public void init()
	{
		this.targetYaw = degrees;  //Calculate the target heading off of # of degrees to turn
		startTime = System.currentTimeMillis();
	}
	
	public boolean update()
	{
		error = getError(); //Update error value
		System.out.println(error);
		
		if(turnController.onTarget()) alignedCount++;
		if(alignedCount >= MIN_ALIGNED_COUNT) return true;
		if(System.currentTimeMillis() - startTime > TIMEOUT) return true;

		leftPower = output; 
		rightPower = -output;
		
		drivetrain.setPower(leftPower, rightPower);
		
		return false;
	}
	
	public double getError(){
		return navx.getAngleDistance(navx.getAngleOffStart(), targetYaw);
	}

	@Override
	public void pidWrite(double output) {
		this.output = output;
	}
	
}

package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriveTrain.DriveMode;

public class DriveStraight extends AutonomousCommand{

	private static final double INITIAL_POWER = 0.5;
	private static final double PROPORTION = 0.05;
	
	private final DriveTrain driveTrain;
	private double leftOutput;
	private double rightOutput;
	
	
	public DriveStraight(DriveTrain driveTrain){
		this.driveTrain = driveTrain;
	}
	
	@Override
	public void init() {
		driveTrain.setMode(DriveMode.DRIVER_CONTROL_LOW);
		leftOutput = INITIAL_POWER;
		rightOutput = INITIAL_POWER;
	}

	@Override
	public boolean update() {
		double difference = Math.abs(driveTrain.getRightEncoderVelocity()) - Math.abs(driveTrain.getRightEncoderVelocity());
		
		rightOutput -= difference * PROPORTION;
		rightOutput += difference * PROPORTION;
		
		driveTrain.setMotors(-rightOutput, -leftOutput);
		return false;
	}

}

package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriveTrain.DriveMode;

import edu.wpi.first.wpilibj.DriverStation;

public class DriveStraight extends AutonomousCommand{

	private static final double INITIAL_POWER = 0.5;
	private static final double PROPORTION = 0.0001;
	
	private final DriveTrain driveTrain;
	private double leftOutput;
	private double rightOutput;
	
	
	public DriveStraight(DriveTrain driveTrain){
		this.driveTrain = driveTrain;
	}
	
	@Override
	public void init() {
		driveTrain.setMode(DriveMode.DRIVER_CONTROL);
		leftOutput = INITIAL_POWER;
		rightOutput = INITIAL_POWER;
	}

	@Override
	public boolean update() {
		double difference = Math.abs(driveTrain.getRightEncoderVelocity()) - Math.abs(driveTrain.getLeftEncoderVelocity());
		
		DriverStation.reportError(String.format("LeftVel:%d RightVel:%d CiO:%f", driveTrain.getLeftEncoderVelocity(), driveTrain.getRightEncoderVelocity(), difference * PROPORTION), false);
		
		rightOutput -= difference * PROPORTION;
		leftOutput += difference * PROPORTION;
		
		//DriverStation.reportError(String.format("Diff:%f, Left:%f Right:%f", difference, leftOutput, rightOutput), false);
		
		driveTrain.setMotors(-leftOutput, -rightOutput);
		return false;
	}

}

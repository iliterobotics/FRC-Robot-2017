package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriveTrain.DriveMode;
import org.usfirst.frc.team1885.robot.modules.UltraSonic;

public class UltrasonicStop extends AutonomousCommand{
	
	private final double POWER = 0.1;
	
	private final DriveTrain driveTrain;
	private final UltraSonic ultra;
	private final int distance;
	private double leftOutput;
	private double rightOutput;
	
	public UltrasonicStop(DriveTrain driveTrain, UltraSonic ultra, int distance)
	{
		this.ultra = ultra;
		this.driveTrain = driveTrain;
		this.distance = distance;
	}
	
	
	
	@Override
	public void init() {
		driveTrain.setMode(DriveMode.DRIVER_CONTROL_LOW);
		leftOutput = POWER;
		rightOutput = POWER;
	}

	@Override
	public boolean update() {
		
		if ( ultra.getInches() < distance )
		{
			driveTrain.setMotors(0, 0);
			return true;
		}
		
		driveTrain.setMotors(-leftOutput, -rightOutput);
		return false;
	}

}

package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.Joystick;

public class DriverControlArcadeOneStick extends DriverControl {
	
	public DriverControlArcadeOneStick(DriveTrain driveTrain, Climber climber, NavX navx, GearManipulator gearManipulator) {
		super(driveTrain, gearManipulator, climber, navx);
	}
	
	public void updateDriveTrain() {
		double throttle = getController(ControllerType.LEFT_STICK).getAxis(Joystick.AxisType.kY);
		double turn = getController(ControllerType.LEFT_STICK).getAxis(Joystick.AxisType.kX);
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		setSpeeds(leftInput, rightInput);		
	}

}

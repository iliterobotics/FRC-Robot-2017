package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.Joystick;

public class DriverControlArcadeOneStick extends DriverControl {
	
	public DriverControlArcadeOneStick(DriveTrain driveTrain, NavX navx) {
		super(driveTrain, navx);
	}
	
	public void updateDriveTrain() {
		System.out.println("Updating DriveTrain");
		double throttle = getController(ControllerType.LEFT_STICK).getAxis(Joystick.AxisType.kY);
		double turn = getController(ControllerType.LEFT_STICK).getAxis(Joystick.AxisType.kX);
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		setSpeeds(leftInput, rightInput);		
	}

}

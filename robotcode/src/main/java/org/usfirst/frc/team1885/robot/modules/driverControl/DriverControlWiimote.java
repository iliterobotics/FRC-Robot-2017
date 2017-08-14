package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl.ControllerType;

import edu.wpi.first.wpilibj.Joystick;

public class DriverControlWiimote extends DriverControl{
 
	//For use with the HID Wiimote Driver
	
	private static final int A_BUTTON = 3;
	private static final int B_BUTTON = 4;
	private static final int PLUS_BUTTON = 5;
	private static final int HOME_BUTTON = 7;
	private static final int MINUS_BUTTON = 6;
	private static final int ONE_BUTTON = 1;
	private static final int TWO_BUTTON = 2;
	
	private static final double WHEEL_ROLL_DEADBAND = 0.05;
	private static final double DPAD_DEADBAND = 0.05;
	
	//Axes could've been put in an enum with values that describes how they're flipped, but this is quicker
	private static final int WHEEL_DPAD_X_AXIS = 0;
	private static final int WHEEL_DPAD_Y_AXIS = 1;
	private static final int WHEEL_ROLL_AXIS = 3;
	private static final int WHEEL_PITCH_AXIS = 4;
	
	private static final int DPAD_X_AXIS = 1;
	private static final int DPAD_Y_AXIS = 0;
	private static final int ROLL_AXIS = 4;
	private static final int PITCH_AXIS = 3;
	
	public DriverControlWiimote(DriveTrain driveTrain, NavX navx) {
		super(driveTrain, navx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateDriveTrain() {
		Joystick driverController = getController(ControllerType.CONTROLLER);
		
		double throttle = 0;
		double turn = 0;
		
		if(driverController.getRawButton(A_BUTTON)) {
			double wheelPitch = driverController.getRawAxis(WHEEL_PITCH_AXIS);
			if(wheelPitch < 0) wheelPitch = 0;
			wheelPitch = 1 - wheelPitch;
			throttle = wheelPitch;
		}
		if(driverController.getRawButton(B_BUTTON)) {
			double wheelPitch = driverController.getRawAxis(WHEEL_PITCH_AXIS);
			if(wheelPitch < 0) wheelPitch = 0;
			wheelPitch = wheelPitch - 1;
			throttle = wheelPitch;
		}
		if(driverController.getRawButton(TWO_BUTTON)) {
			double wheelRoll = driverController.getRawAxis(WHEEL_ROLL_AXIS);
			if(Math.abs(wheelRoll) < WHEEL_ROLL_DEADBAND) wheelRoll = 0;
			turn = wheelRoll;
		}
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;
		

		setSpeeds(leftInput, rightInput);
		
	}
	
	
}

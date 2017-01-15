package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;

public class DriverControlArcadeControllerTwoStick extends DriverControl{

	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain) {
		super(driveTrain);
	}

	@Override
	public void update() {
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_LEFT_Y);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		setSpeeds(leftInput, rightInput);
	}

}

package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;

public class DriverControlArcadeControllerTwoStick extends DriverControl{

	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain) {
		super(driveTrain);
	}

	@Override
	public void update() {
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(1);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(4);
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		setSpeeds(leftInput, rightInput);
	}

}

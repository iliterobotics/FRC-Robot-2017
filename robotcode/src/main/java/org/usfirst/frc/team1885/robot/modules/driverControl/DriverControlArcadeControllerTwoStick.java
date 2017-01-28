package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;

public class DriverControlArcadeControllerTwoStick extends DriverControl{

	private static final int REDUCER = 2;

	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain) { 
		this(driveTrain, new DefaultJoystickFactory());
	}
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, IJoystickFactory joystickFactory) {
		super(driveTrain, joystickFactory);
	}

	@Override
	public void update() {
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_LEFT_Y);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		if(getController(ControllerType.CONTROLLER).getRawButton(5)) turn /= REDUCER;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		setSpeeds(leftInput, rightInput);
	}

}

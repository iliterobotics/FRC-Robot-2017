package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;

public class DriverControlArcadeControllerTwoStick extends DriverControl{
	private static final int REDUCER = 2;
	
	private static final int GEAR_SHIFT = 3;
	private static final int REDUCE = 5;

	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, GearManipulator gearManipulator) { 
		this(driveTrain, gearManipulator, new DefaultJoystickFactory());
	}
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, GearManipulator gearManipulator, IJoystickFactory joystickFactory) {
		super(driveTrain, gearManipulator, joystickFactory);
	}
	
	public void updateDriveTrain() {
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_LEFT_Y);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		if(getController(ControllerType.CONTROLLER).getRawButton(REDUCE)) turn /= REDUCER;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		setSpeeds(leftInput, rightInput);
		updateGearShift(getController(ControllerType.CONTROLLER).getRawButton(GEAR_SHIFT));
	}

	@Override
	public void update() {
		updateDriveTrain();
		updateManipulatorServos();
		updateManipulatorPneumatics();
	}	
}

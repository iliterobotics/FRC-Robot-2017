package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.EJoystickAxis;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl.ControllerType;


public class DriverControlArcadeControllerTwoStick extends DriverControl{
	private static final double TURN_REDUCER = 0.5;
	private static final double HIGH_GEAR_TURN_REDUCER = 0.2;
	private DriveTrain driveTrain;
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, GearManipulator gearManipulator) { 
		this(driveTrain, gearManipulator, new DefaultJoystickFactory());
	}
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, GearManipulator gearManipulator, IJoystickFactory joystickFactory) {
		super(driveTrain, gearManipulator,joystickFactory);
		this.driveTrain = driveTrain;
	}

	@Override
	public void updateDriveTrain() {
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_LEFT_Y);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		if(getController(ControllerType.CONTROLLER).getRawButton(5)) turn *= TURN_REDUCER;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;
		
		setCasters(getController(ControllerType.CONTROLLER).getRawAxis(5) >= 0.9);
		setSpeeds(leftInput, rightInput);
	}
	
	public double getReducer(double value) {
		return (value + 1.0) / 2.0;
	}
	
}

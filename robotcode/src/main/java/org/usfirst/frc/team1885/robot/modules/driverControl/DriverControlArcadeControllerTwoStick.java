package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.EJoystickAxis;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl.ControllerType;

public class DriverControlArcadeControllerTwoStick extends DriverControl{
	
	public static final double kP = 0.03;
	public static final double kI = 0.0;
	public static final double kD = 0.0;
	public static final double kF = 0.0;
	
	public static final double kToleranceDegrees = 2.0f;
	
	private static final int REDUCER = 2;

	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain) { 
		this(driveTrain, new DefaultJoystickFactory());
	}
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, IJoystickFactory joystickFactory) {
		super(driveTrain, joystickFactory);
	}

	@Override
	public void update() {
		double reducer = getReducer(getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kZ));
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_LEFT_Y);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		if(getController(ControllerType.CONTROLLER).getRawButton(5)) turn /= REDUCER;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;
		
		leftInput *= reducer;
		rightInput *= reducer;

		setSpeeds(leftInput, rightInput);
	}
	
	public double getReducer(double value) {
		return (value + 1.0) / 2.0;
	}
	
}

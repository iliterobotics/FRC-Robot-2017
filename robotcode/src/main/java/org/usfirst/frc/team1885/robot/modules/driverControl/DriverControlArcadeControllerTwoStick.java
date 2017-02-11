package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.EJoystickAxis;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;


public class DriverControlArcadeControllerTwoStick extends DriverControl{
	private static final int REDUCER = 2;
	private DriveTrain driveTrain;
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, NavX navx) { 
		this(driveTrain, new DefaultJoystickFactory(), navx);
	}
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, IJoystickFactory joystickFactory, NavX navx) {
		super(driveTrain, joystickFactory);
		this.driveTrain = driveTrain;
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

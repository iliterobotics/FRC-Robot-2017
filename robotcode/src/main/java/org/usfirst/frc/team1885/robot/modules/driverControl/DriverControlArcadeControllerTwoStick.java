package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;


public class DriverControlArcadeControllerTwoStick extends DriverControl{
	private static final int TURN_90 = 6;
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
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_LEFT_Y);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		if(getController(ControllerType.CONTROLLER).getRawButton(5)) turn /= REDUCER;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;
		
		setSpeeds(leftInput, rightInput);
		
	}
	
	public void turnToAngle() {
		boolean turnControllerState = getController(ControllerType.CONTROLLER).getRawButton(TURN_90);
		if(turnControllerState) {
			driveTrain.setTurnTarget(90.0f);
		}
		driveTrain.turnControlState(turnControllerState);
	}
}

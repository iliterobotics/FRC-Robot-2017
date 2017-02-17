package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.NavX;


public class DriverControlArcadeControllerTwoStick extends DriverControl{
	
	private static final int TURN_REDUCER_BUTTON = 5;
	private static final int CASTER_BUTTON = 3;
	private static final int WARP_SPEED_FORWARD = 0;
	
	private static final double TURN_REDUCER = 0.5;
	private static final double HIGH_GEAR_TURN_REDUCER = 0.2;

	private DriveTrain driveTrain;
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, GearManipulator gearManipulator, Climber climber, NavX navx) { 
		this(driveTrain, gearManipulator, climber, navx, new DefaultJoystickFactory());
	}
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, GearManipulator gearManipulator, Climber climber, NavX navx, IJoystickFactory joystickFactory) {
		super(driveTrain, gearManipulator, climber, navx, joystickFactory);
		this.driveTrain = driveTrain;
	}

	@Override
	public void updateDriveTrain() {
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_LEFT_Y);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		if(getController(ControllerType.CONTROLLER).getRawButton(TURN_REDUCER_BUTTON)) turn *= TURN_REDUCER;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;
		
		setCasters(getController(ControllerType.CONTROLLER).getRawAxis(CASTER_BUTTON) >= 0.9);
		setSpeeds(leftInput, rightInput);
		
		if(getController(ControllerType.CONTROLLER).getPOV() == WARP_SPEED_FORWARD && !isWarpSpeed()){
			initiateWarpSpeed();
		}
		else if( isWarpSpeed() && getController(ControllerType.CONTROLLER).getPOV() != WARP_SPEED_FORWARD){
			disableWarpSpeed();
		}
	}
	
	public double getReducer(double value) {
		return (value + 1.0) / 2.0;
	}
	
}

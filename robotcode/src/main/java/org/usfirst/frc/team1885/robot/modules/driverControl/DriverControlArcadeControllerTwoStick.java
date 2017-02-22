package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystick;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.NavX;


public class DriverControlArcadeControllerTwoStick extends DriverControl{
	
	private static final int TURN_REDUCER_BUTTON = 5;
	private static final int CASTER_AXIS = 2;
	private static final int REDUCER_AXIS = 3;
	
	private static final int WARP_SPEED_FORWARD = 0;
	private static final int WARP_SPEED_BACKWARD = 180;

	private static final int NUDGE_BUTTON_LEFT = 5;
	private static final int NUDGE_BUTTON_RIGHT = 6;
	
	private static final double REDUCER = 0.6;
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
		IJoystick driverController = getController(ControllerType.CONTROLLER);
		double throttle = driverController.getRawAxis(GAMEPAD_LEFT_Y);
		double turn = driverController.getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;
		
		if(driverController.getRawAxis(REDUCER_AXIS) > 0.8){
			leftInput *= REDUCER;
			rightInput *= REDUCER;
		}

		
		setCasters(driverController.getRawAxis(CASTER_AXIS) >= 0.9);
		setSpeeds(leftInput, rightInput);

		if(driverController.getRawButton(NUDGE_BUTTON_RIGHT) && !isNudging()){
			nudge(1);
		}
		else if(driverController.getRawButton(NUDGE_BUTTON_LEFT) && !isNudging()){
			nudge(-1);			
		}
		
		if(driverController.getPOV() == WARP_SPEED_FORWARD && !isWarpSpeed()){
			initiateWarpSpeed(1);
		}
		if(driverController.getPOV() == WARP_SPEED_BACKWARD && !isWarpSpeed()){
			initiateWarpSpeed(-1);
		}
		else if( isWarpSpeed() && driverController.getPOV() != WARP_SPEED_FORWARD && driverController.getPOV() != WARP_SPEED_BACKWARD){
			disableWarpSpeed();
		}
	}
	
	public double getReducer(double value) {
		return (value + 1.0) / 2.0;
	}
	
}

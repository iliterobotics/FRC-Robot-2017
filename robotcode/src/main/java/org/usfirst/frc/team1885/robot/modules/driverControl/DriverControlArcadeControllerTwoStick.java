package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.Joystick;


public class DriverControlArcadeControllerTwoStick extends DriverControl{
	
	private static final int TURN_REDUCER_BUTTON = 5;
	private static final int REDUCER_AXIS = 3;
	
	private static final int WARP_SPEED_FORWARD = 0;
	private static final int WARP_SPEED_BACKWARD = 180;
	
	private static final int HIGH_GEAR_AXIS = 2;

	private static final int NUDGE_BUTTON_LEFT = 3;
	private static final int NUDGE_BUTTON_RIGHT = 2;
	
	private static final double REDUCER = 0.6;
	private static final double HIGH_GEAR_TURN_REDUCER = 0.2;

	private DriveTrain driveTrain;
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, NavX navx) { 
		super(driveTrain, navx);
		this.driveTrain = driveTrain;
	}

	@Override
	public void updateDriveTrain() {
		Joystick driverController = getController(ControllerType.CONTROLLER);
		double throttle = driverController.getRawAxis(GAMEPAD_LEFT_Y);
		double turn = driverController.getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;
		
		if(driverController.getRawAxis(REDUCER_AXIS) >= TRIGGER_DEADZONE){
			leftInput *= REDUCER;
			rightInput *= REDUCER;
		}

		setSpeeds(leftInput, rightInput);
		setShift(driverController.getRawAxis(HIGH_GEAR_AXIS) >= TRIGGER_DEADZONE);
		
		if(driverController.getRawButton(NUDGE_BUTTON_RIGHT) && !isNudging()){
			nudge(1);
		}
		else if(driverController.getRawButton(NUDGE_BUTTON_LEFT) && !isNudging()){
			nudge(-1);			
		}
		
//		if(driverController.getPOV() == WARP_SPEED_FORWARD && !isWarpSpeed()){
//			initiateWarpSpeed(1);
//		}
//		if(driverController.getPOV() == WARP_SPEED_BACKWARD && !isWarpSpeed()){
//			initiateWarpSpeed(-1);
//		}
//		else if( isWarpSpeed() && driverController.getPOV() != WARP_SPEED_FORWARD && driverController.getPOV() != WARP_SPEED_BACKWARD){
//			disableWarpSpeed();
//		}
	}
	
}

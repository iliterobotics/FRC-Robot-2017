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
	private static final int CASTER_BUTTON = 2;
	private static final int SHIFTER_AXIS = 3;
	
	private static final int WARP_SPEED_FORWARD = 0;

	private static final int NUDGE_BUTTON_LEFT = 4;
	private static final int NUDGE_BUTTON_RIGHT = 5;
	
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
		IJoystick driverController = getController(ControllerType.CONTROLLER);
		double throttle = driverController.getRawAxis(GAMEPAD_LEFT_Y);
		double turn = driverController.getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		if(driverController.getRawButton(TURN_REDUCER_BUTTON)) turn *= TURN_REDUCER;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;
		
		setCasters(driverController.getRawAxis(CASTER_BUTTON) >= 0.9);
		setShift(driverController.getRawAxis(SHIFTER_AXIS) >= 0.9);
		setSpeeds(leftInput, rightInput);

		if(driverController.getRawButton(NUDGE_BUTTON_RIGHT) && !isNudging()){
			
		}
		
		if(driverController.getPOV() == WARP_SPEED_FORWARD && !isWarpSpeed()){
			initiateWarpSpeed();
		}
		else if( isWarpSpeed() && driverController.getPOV() != WARP_SPEED_FORWARD){
			disableWarpSpeed();
		}
	}
	
	public double getReducer(double value) {
		return (value + 1.0) / 2.0;
	}
	
}

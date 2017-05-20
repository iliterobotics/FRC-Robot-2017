package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.NavX;
import org.usfirst.frc.team1885.robot.modules.Shooter;

import edu.wpi.first.wpilibj.Joystick;


public class DriverControlArcadeControllerTwoStick extends DriverControl{
	
	private static final int TURN_REDUCER_BUTTON = 5;
	private static final int SHOOT_BUTTON = 6;
	private static final int REDUCER_AXIS = 3;
	
	private static final int SHOOTER_UP_BUTTON = 1;
	private static final int SHOOTER_DOWN_BUTTON = 0;
	
	private static final double ADJUST_AMOUNT = 0;

	private DriveTrain driveTrain;
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, GearManipulator gearManipulator, Climber climber, NavX navx, Shooter shooter) { 
		super(driveTrain, gearManipulator, climber, navx, shooter);
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
		
		if(driverController.getRawButton(SHOOTER_DOWN_BUTTON)) getShooter().setAngle(getShooter().getAngle() - ADJUST_AMOUNT);
		if(driverController.getRawButton(SHOOTER_UP_BUTTON)) getShooter().setAngle(getShooter().getAngle() + ADJUST_AMOUNT);
		
		if(driverController.getRawButton(SHOOT_BUTTON)) getShooter().shoot();
		
		setSpeeds(leftInput, rightInput);
		
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

package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class DriverControlArcadeControllerTwoStick extends DriverControl implements PIDOutput{
	private static final int TURN_90 = 6;
	public static final double kP = 0.03;
	public static final double kI = 0.0;
	public static final double kD = 0.0;
	public static final double kF = 0.0;
	
	public static final double kToleranceDegrees = 2.0f;
	
	private double rotateToAngleRate;
	private PIDController turnController;
	
	private static final int REDUCER = 2;

	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, NavX navx) { 
		this(driveTrain, new DefaultJoystickFactory(), navx);
	}
	
	public DriverControlArcadeControllerTwoStick(DriveTrain driveTrain, IJoystickFactory joystickFactory, NavX navx) {
		super(driveTrain, joystickFactory);
		turnController = new PIDController(kP, kI, kD, kF, navx, this);
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(kToleranceDegrees);
		turnController.setContinuous(true);
	}

	@Override
	public void update() {
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_LEFT_Y);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		if(getController(ControllerType.CONTROLLER).getRawButton(5)) turn /= REDUCER;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;
		
		if(turnToAngle()) {
			turnController.enable();
			leftInput = rotateToAngleRate;
			rightInput = -rotateToAngleRate;
		}
		else {
			turnController.disable();
		}
		setSpeeds(leftInput, rightInput);
		
	}
	
	
	
	public boolean turnToAngle() {
		DriverStation.reportError("How you doin", false);
		if(getController(ControllerType.CONTROLLER).getRawButton(TURN_90)) {
			DriverStation.reportError("We moving fam", false);
			turnController.setSetpoint(90.0f);			
			return true;
		}
		return false;
	}
	
	@Override
	public void pidWrite(double output) {
		rotateToAngleRate = output;
	}
}

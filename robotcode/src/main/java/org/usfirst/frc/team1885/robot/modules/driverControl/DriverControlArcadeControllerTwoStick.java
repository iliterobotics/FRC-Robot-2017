package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class DriverControlArcadeControllerTwoStick extends DriverControl implements PIDOutput{
	private double rotateToAngleRate;
	private PIDController turnController;
	private NavX navx;
	
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
		
		turnController = new PIDController(kP, kI, kD, kF, navx, this);
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(kToleranceDegrees);
		turnController.setContinuous(true);
		
		LiveWindow.addActuator("Drive System", "Rotate Controller", turnController);
	}

	@Override
	public void update() {
		double throttle = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_LEFT_Y);
		double turn = getController(ControllerType.CONTROLLER).getRawAxis(GAMEPAD_RIGHT_X);
		
		double leftInput, rightInput;
		
		if(getController(ControllerType.CONTROLLER).getRawButton(5)) turn /= REDUCER;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		if(turnToAngle()) turnController.enable();
		else turnController.disable();
		
		setSpeeds(leftInput, rightInput);
	}
	
	public boolean turnToAngle() {
		if(getController(ControllerType.CONTROLLER).getRawButton(6)) {
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

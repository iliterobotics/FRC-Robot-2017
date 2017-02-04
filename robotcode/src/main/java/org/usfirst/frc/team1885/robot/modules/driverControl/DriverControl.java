package org.usfirst.frc.team1885.robot.modules.driverControl;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.robot.common.interfaces.IJoystick;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.Module;

public abstract class DriverControl implements Module {
	
	public static final double DEADZONE = 0.01;
	public static final int GAMEPAD_LEFT_X = 0;
	public static final int GAMEPAD_LEFT_Y = 1;
	public static final int GAMEPAD_LEFT_TRIGGER = 2;
	public static final int GAMEPAD_RIGHT_TRIGGER = 3;
	public static final int GAMEPAD_RIGHT_X = 4;
	public static final int GAMEPAD_RIGHT_Y = 5;
	
	public static final int SERVO_TOGGLE = 6;
	public static final int INTAKE_TOGGLE = 8;
	public static final int INTAKE_IN = 5;
	public static final int INTAKE_OUT = 7;
	
	private static final double INTAKING = 0.9;
	private static final double OUTTAKING = -0.9;
	

	private Map<ControllerType, IJoystick> controllerMap;

	private final DriveTrain driveTrain;
	private final GearManipulator gearManipulator;
	private IJoystickFactory joystickFactory;
	
	private boolean servoToggle, intakeToggle;	
	private boolean servoPreviousState, intakePreviousState;
	private double intakeSpeed;

	public enum ControllerType {
		LEFT_STICK(0), RIGHT_STICK(1), CONTROLLER(2), MANIPULATOR_CONTROLLER(3);

		final int controllerId;

		ControllerType(int id) {
			controllerId = id;
		}
	}

	public DriverControl(DriveTrain driveTrain, GearManipulator gearManipulator, IJoystickFactory created) {
		this.driveTrain = driveTrain;
		this.gearManipulator = gearManipulator;
		this.joystickFactory = created;	
		controllerMap = new HashMap<ControllerType, IJoystick>();
	}

	public void initialize() {
		servoToggle = intakeToggle = false;
		servoPreviousState = intakePreviousState = false;
		intakeSpeed = 0.0;
		for (ControllerType type : ControllerType.values()) {
			controllerMap.put(type, joystickFactory.createJoystick(type.controllerId));
					
		}
		driveTrain.setMode(DriveTrain.DriveMode.DRIVER_CONTROL);		
	}
	
	public abstract void updateDriveTrain();
	
	public void updateManipulatorServos() {
		if(getController(ControllerType.MANIPULATOR_CONTROLLER).getRawButton(SERVO_TOGGLE) && !servoPreviousState) {
			servoPreviousState = servoToggle;
			servoToggle = !servoToggle;
		}
		else if(!getController(ControllerType.CONTROLLER).getRawButton(SERVO_TOGGLE)){
			servoPreviousState = false;
		}
		gearManipulator.updateServos(servoToggle);
	}
	
	public void updateManipulatorPneumatics() {
		if(getController(ControllerType.MANIPULATOR_CONTROLLER).getRawButton(INTAKE_TOGGLE) && !intakePreviousState) {
			intakePreviousState = intakeToggle;
			intakeToggle = !intakeToggle;
		}
		else if(!getController(ControllerType.MANIPULATOR_CONTROLLER).getRawButton(INTAKE_TOGGLE)){
			intakePreviousState = false;
		}
		gearManipulator.updatePneumatics(intakeToggle);
	}
	
	public void updateIntake() {
		if(getController(ControllerType.MANIPULATOR_CONTROLLER).getRawButton(INTAKE_IN)) {
			intakeSpeed = INTAKING;
		}
		else if(getController(ControllerType.MANIPULATOR_CONTROLLER).getRawButton(INTAKE_OUT)) {
			intakeSpeed = OUTTAKING;
		}
		else {
			intakeSpeed = 0.0;
		}
		gearManipulator.updateIntake(intakeSpeed);
	}
	
	public void updateGearShift(boolean value) {
		driveTrain.setGearShift(value);
	}
	
	public void setSpeeds(double left, double right){
		if(Math.abs(left - right) < DEADZONE ){
			left = right = (left + right) / 2;
		}
		if(Math.abs(left) < DEADZONE){
			left = 0;
		}
		if(Math.abs(right) < DEADZONE){
			right = 0;
		}
		driveTrain.setMotors(left, right);
	}
	
	public IJoystick getController(ControllerType type){
		return controllerMap.get(type);
	}
	
}

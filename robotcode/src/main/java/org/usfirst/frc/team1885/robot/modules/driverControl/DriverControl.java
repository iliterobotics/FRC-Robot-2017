package org.usfirst.frc.team1885.robot.modules.driverControl;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.robot.common.interfaces.IJoystick;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.Module;

public abstract class DriverControl implements Module {
	
	public static final double DEADZONE = 0.03;
	public static final int GAMEPAD_LEFT_X = 0;
	public static final int GAMEPAD_LEFT_Y = 1;
	public static final int GAMEPAD_LEFT_TRIGGER = 2;
	public static final int GAMEPAD_RIGHT_TRIGGER = 3;
	public static final int GAMEPAD_RIGHT_X = 4;
	public static final int GAMEPAD_RIGHT_Y = 5;
	

	private Map<ControllerType, IJoystick> controllerMap;

	private final DriveTrain driveTrain;
	private final GearManipulator gearManipulator;
	private IJoystickFactory joystickFactory;

	public enum ControllerType {
		LEFT_STICK(0), RIGHT_STICK(1), CONTROLLER(2), CONTROLLER_2(3);

		final int controllerId;

		ControllerType(int id) {
			controllerId = id;
		}
	}

	public DriverControl(DriveTrain driveTrain, GearManipulator gearManipulator, IJoystickFactory created) {
		this.driveTrain = driveTrain;
		this.joystickFactory = created;
		this.gearManipulator = gearManipulator;
		controllerMap = new HashMap<ControllerType, IJoystick>();
	}

	public void initialize() {
		for (ControllerType type : ControllerType.values()) {
			controllerMap.put(type, joystickFactory.createJoystick(type.controllerId));
					
		}
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
		driveTrain.setPower(left, right);
	}
	
	public void updateManipulator(){
		IJoystick manipulatorController = getController(ControllerType.CONTROLLER_2);
		double intakeSpeed = 0;
		if(manipulatorController.getRawAxis(1) > 0.5){
			gearManipulator.setIntakeSpeed(-GearManipulator.DEFAULT_INTAKE_SPEED);
		}
		else if(manipulatorController.getRawAxis(1) < -0.5){
			gearManipulator.setIntakeSpeed(GearManipulator.DEFAULT_INTAKE_SPEED);
		}
		
	}
	
	public abstract void updateDriveTrain();
	
	public void update(){
		updateManipulator();
	}
	
	public void setShift(boolean shifted){
		driveTrain.setShift(shifted);
	}
	
	public void setCasters(boolean casted){
		driveTrain.setCasting(casted);
	}
	
	public IJoystick getController(ControllerType type){
		return controllerMap.get(type);
	}
}

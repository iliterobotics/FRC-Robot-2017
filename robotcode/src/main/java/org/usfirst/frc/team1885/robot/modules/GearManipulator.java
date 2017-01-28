package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl.ControllerType;

import edu.wpi.first.wpilibj.Servo;

public class GearManipulator implements Module{
	private static final int POSITION_1 = 0;
	private static final int POSITION_2 = 65;
	private static final int GEAR_TOGGLE = 5;
	private Map<ServoType, Servo> servoMap = new HashMap<ServoType, Servo>();
	private boolean toggle;
	private DriverControl driverControl;
	private boolean previousState;
	
	private enum ServoType {
		RIGHT_GEAR_HOLD(0), LEFT_GEAR_HOLD(1);
		
		final int channel;
		int position;
		
		ServoType(int channel) {
			this.channel = channel;
			position = POSITION_1;
		}
	}
	
	public GearManipulator(DriverControl driverControl) {
		servoMap = new HashMap<>();
		this.driverControl = driverControl;
		toggle = previousState = false;
	}

	@Override
	public void initialize() {
		for(ServoType type : ServoType.values()) {
			Servo servo = new Servo(type.channel);
			servoMap.put(type, servo);
		}		
	}

	@Override
	public void update() {
		if(driverControl.getController(ControllerType.CONTROLLER).getRawButton(GEAR_TOGGLE) && !previousState) {
			previousState = toggle;
			toggle = !toggle;
		}
		else if(!driverControl.getController(ControllerType.CONTROLLER).getRawButton(GEAR_TOGGLE)){
			previousState = false;
		}
		if(toggle) {
			servoMap.get(servoMap.get(ServoType.LEFT_GEAR_HOLD)).set(0.0);
			servoMap.get(servoMap.get(ServoType.RIGHT_GEAR_HOLD)).set(0.0);
		}
		else {
			servoMap.get(servoMap.get(ServoType.LEFT_GEAR_HOLD)).set(0.3);
			servoMap.get(servoMap.get(ServoType.RIGHT_GEAR_HOLD)).set(0.5);
		}
//		if(driverControl.getController(ControllerType.CONTROLLER).getRawButton(5)) {
//			servoMap.get(ServoType.LEFT_GEAR_HOLD).set(0.0);
//			servoMap.get(ServoType.RIGHT_GEAR_HOLD).set(0.0);
//		}
//		else if(driverControl.getController(ControllerType.CONTROLLER).getRawButton(6)) {
//			servoMap.get(ServoType.LEFT_GEAR_HOLD).set(0.5);
//			servoMap.get(ServoType.RIGHT_GEAR_HOLD).set(0.5);
//		}
	}
	
}
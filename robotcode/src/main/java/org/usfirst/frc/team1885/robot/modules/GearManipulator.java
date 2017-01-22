package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl.ControllerType;

import edu.wpi.first.wpilibj.Servo;

public class GearManipulator implements Module{
	private static final int POSITION_1 = 0;
	private static final int POSITION_2 = 45;
	private Map<ServoType, Servo> servoMap = new HashMap<ServoType, Servo>();
	private boolean toggle;
	private DriverControl driverControl;
	
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
		toggle = false;
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
		if(driverControl.getController(ControllerType.CONTROLLER).getRawButton(5)) {
			toggle = !toggle;
		}
		for(ServoType servo : servoMap.keySet()) {
			if (toggle) {
				if (servo.position == POSITION_1) {
					servo.position = POSITION_2;
				}
				else {
					servo.position = POSITION_1;
				}
			}	
			servoMap.get(servo).setAngle(servo.position);
		}
	}
	
}

package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

/**
 * Class for running all drive train control operations from both autonomous and driver-control
 */

public class DriveTrain implements Module{

	private static final float MAX_POWER_DIFF = 0.01f;
	
	private float desiredLeftPower;
	private float desiredRightPower;
	private float actualLeftPower;
	private float actualRightPower;
	
	private DriveMode currentMode;
	
	public enum DriveMode{
		DRIVER_CONTROL_HIGH, DRIVER_CONTROL_LOW;
	}
	private enum MotorType{
		LEFT_MOTOR(0), RIGHT_MOTOR(1);
		
		final int talonId;
		
		MotorType(int talonId){
			this.talonId = talonId;
		}
	}
	
	private Map<MotorType, CANTalon> motorMap;
	
	@Override
	public void initialize() {
		for(MotorType type : MotorType.values()){
			motorMap.put(type, new CANTalon(type.talonId));
		}
	}
	
	public void setMode(DriveMode mode){
		this.currentMode = mode;
		switch(currentMode){
		case DRIVER_CONTROL_HIGH:
		case DRIVER_CONTROL_LOW:
			actualLeftPower = 0;
			desiredLeftPower = 0;
			actualRightPower = 0;
			desiredRightPower = 0;
			setMotorMode(TalonControlMode.PercentVbus);
			break;
		}
	}
	
	public DriveTrain(){
		motorMap = new HashMap<>();
	}
	
	private void setMotors(float left, float right){
		motorMap.get(MotorType.LEFT_MOTOR).set(left);
		motorMap.get(MotorType.RIGHT_MOTOR).set(right);
	}
	
	public void setMotorMode(TalonControlMode talonMode){
		for(CANTalon talon : motorMap.values()){
			talon.setControlMode(talonMode.value);
		}
	}
	
	public TalonControlMode getMotorMode(){
		if(!motorMap.values().isEmpty()){
			return motorMap.values().iterator().next().getControlMode();
		}
		return null;
	}

	@Override
	public void update() {
			switch(currentMode){
			case DRIVER_CONTROL_HIGH:
			case DRIVER_CONTROL_LOW:
				if(Math.abs(actualLeftPower - desiredLeftPower) > MAX_POWER_DIFF){
					int direction = (desiredLeftPower > actualLeftPower)?1:-1;
					actualLeftPower = direction * MAX_POWER_DIFF;
				}
				else actualLeftPower = desiredLeftPower;
				if(Math.abs(actualRightPower - desiredRightPower) > MAX_POWER_DIFF){
					int direction = (desiredRightPower > actualRightPower)?1:-1;
					actualRightPower = direction * MAX_POWER_DIFF;
				}
				else actualLeftPower = desiredRightPower;
				
				setMotors(actualLeftPower, actualRightPower);
				break;
		}
	}
}
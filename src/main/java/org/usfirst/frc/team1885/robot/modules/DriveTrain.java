package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;

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
		LEFT_MOTOR(1, 3), RIGHT_MOTOR(2, 4);
		
		final int talonIds[];
		
		MotorType(int ... talonIds){
			this.talonIds = talonIds;
		}
	}
	
	private Map<MotorType, CANTalon[]> motorMap;
	
	@Override
	public void initialize() {
		for(MotorType type : MotorType.values()){
			CANTalon[] talons = new CANTalon[type.talonIds.length];
			for(int i = 0; i < talons.length; i++){
				talons[i] = new CANTalon(type.talonIds[i]);
			}
			motorMap.put(type, talons);
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
			setMotorMode(TalonControlMode.Speed);
			break;
		}
	}
	
	public DriveTrain(){
		motorMap = new HashMap<>();
	}
	
	public void setSpeeds(float left, float right){
		desiredLeftPower = left;
		desiredRightPower = right;
	}
	
	private void setMotors(float left, float right){
		DriverStation.reportError(String.format("Left:%f, Right%f", left, right), false);
		for(CANTalon canTalon : motorMap.get(MotorType.LEFT_MOTOR)){
			canTalon.set(left);
		}
		for(CANTalon canTalon : motorMap.get(MotorType.RIGHT_MOTOR)){
			canTalon.set(right);			
		}
	}
	
	public void setMotorMode(TalonControlMode talonMode){
		for(CANTalon[] talons : motorMap.values()){
			for(CANTalon talon : talons){
				talon.setControlMode(talonMode.value);
			}
		}
	}
	
	public TalonControlMode getMotorMode(){
		if(!motorMap.values().isEmpty()){
			return motorMap.values().iterator().next()[0].getControlMode();
		}
		return null;
	}

	@Override
	public void update() {
			switch(currentMode){
			case DRIVER_CONTROL_HIGH:
			case DRIVER_CONTROL_LOW:
//				if(Math.abs(actualLeftPower - desiredLeftPower) > MAX_POWER_DIFF){
//					int direction = (desiredLeftPower > actualLeftPower)?1:-1;
//					actualLeftPower = direction * MAX_POWER_DIFF;
//				}
				actualLeftPower = desiredLeftPower;
//				if(Math.abs(actualRightPower - desiredRightPower) > MAX_POWER_DIFF){
//					int direction = (desiredRightPower > actualRightPower)?1:-1;
//					actualRightPower = direction * MAX_POWER_DIFF;
//				}
				actualRightPower = desiredRightPower;
				
				setMotors(actualLeftPower, actualRightPower);
				break;
		}
	}
}
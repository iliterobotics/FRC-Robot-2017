package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Class for running all drive train control operations from both autonomous and driver-control
 */

public class DriveTrain implements Module{

	private static final double MAX_MOTOR_DIFF = 0.02;
	
	private double desiredLeftPower;
	private double desiredRightPower;
	private double actualLeftPower;
	private double actualRightPower;
		
	private DriveMode currentMode;
	
	public enum DriveMode{
		DRIVER_CONTROL_HIGH, DRIVER_CONTROL_LOW, TICK_VEL;
	}
	private enum MotorType{
		LEFT_MOTOR(-1, 2, 4), RIGHT_MOTOR(1, 3, 1);
		
		final int talonId;
		final int followerIds[];
		final double modifier;
		
		MotorType(double modifier, int talonId, int ... followerIds){
			this.modifier = modifier;
			this.talonId = talonId;
			this.followerIds = followerIds;
		}
	}
	
	private Map<MotorType, CANTalon> motorMap;

	public DriveTrain(){
		motorMap = new HashMap<>();
	}
	
	@Override
	public void initialize() {
		for(MotorType type : MotorType.values()){
			CANTalon talon = new CANTalon(type.talonId);
			talon.setEncPosition(0);
			for(int followerId : type.followerIds){
				CANTalon follower = new CANTalon(followerId);
				follower.setControlMode(TalonControlMode.Follower.value);
				follower.set(type.talonId);
			}
			motorMap.put(type, talon);
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
		case TICK_VEL:
			setMotorMode(TalonControlMode.Speed);
		}
	}
	
	public int getLeftEncoderVelocity(){
		return motorMap.get(MotorType.LEFT_MOTOR).getEncVelocity();
	}
	
	public int getRightEncoderVelocity(){
		return motorMap.get(MotorType.RIGHT_MOTOR).getEncVelocity();		
	}
	
	public void setMotors(double left, double right){
		desiredLeftPower = left;
		desiredRightPower = right;
	}
	
	private void setMotor(MotorType type, double value){
		motorMap.get(type).set(value * type.modifier);
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
				actualLeftPower = getRampedValue(actualLeftPower, desiredLeftPower);
				actualRightPower = getRampedValue(actualRightPower, desiredRightPower);
				setMotor(MotorType.LEFT_MOTOR, actualLeftPower);
				setMotor(MotorType.RIGHT_MOTOR, actualRightPower);
				break;
			case TICK_VEL:
				actualLeftPower = desiredLeftPower;
				actualRightPower = desiredRightPower;
				motorMap.get(MotorType.LEFT_MOTOR).setFeedbackDevice(FeedbackDevice.AnalogEncoder);
				motorMap.get(MotorType.RIGHT_MOTOR).setFeedbackDevice(FeedbackDevice.AnalogEncoder);
				DriverStation.reportError(String.format("Left:%d, Right:%d", motorMap.get(MotorType.LEFT_MOTOR).getEncVelocity(), motorMap.get(MotorType.RIGHT_MOTOR).getEncVelocity()), false); 
				setMotor(MotorType.LEFT_MOTOR, actualLeftPower);
				setMotor(MotorType.RIGHT_MOTOR, actualRightPower);
				break;
		}
	}
	
	public double getRampedValue(double oldValue, double newValue){
		if(Math.abs(oldValue - newValue) > MAX_MOTOR_DIFF){
			int direction = (newValue - oldValue) > 0?1:-1;
			return oldValue + (MAX_MOTOR_DIFF * direction);
		}
		else return newValue;
	}
}
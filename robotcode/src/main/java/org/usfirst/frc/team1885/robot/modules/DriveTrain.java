package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.robot.common.impl.DefaultCanTalonFactory;
import org.usfirst.frc.team1885.robot.common.impl.DefaultDriverStation;
import org.usfirst.frc.team1885.robot.common.impl.EFeedbackDevice;
import org.usfirst.frc.team1885.robot.common.impl.ETalonControlMode;
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalon;
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalonFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.IDriverStation;


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
		LEFT_MOTOR(-1, 1, 3, 5), RIGHT_MOTOR(1, 2, 4, 6);
		
		final int talonId;
		final int followerIds[];
		final double modifier;
		
		MotorType(double modifier, int talonId, int ... followerIds){
			this.modifier = modifier;
			this.talonId = talonId;
			this.followerIds = followerIds;
		}
	}
	
	private Map<MotorType, ICanTalon> motorMap;

	private final ICanTalonFactory canTalonFactory;

	private final IDriverStation driverStation;
	
	public DriveTrain() {
		this(new DefaultCanTalonFactory(),
				new DefaultDriverStation());
	}

	public DriveTrain(ICanTalonFactory canTalonFactory, IDriverStation driverStation){
		this.canTalonFactory = canTalonFactory;
		this.driverStation = driverStation;
		motorMap = new HashMap<>();
		setMode(DriveMode.DRIVER_CONTROL_LOW);
	}
	
	@Override
	public void initialize() {
		for(MotorType type : MotorType.values()){
			ICanTalon talon = canTalonFactory.getCanTalon(type.talonId);
			talon.setEncPosition(0);
			talon.setP(0.5);
			driverStation.reportError(String.format("(%f, %f, %f)", talon.getP(), talon.getI(), talon.getD()), false);
			for(int followerId : type.followerIds){
				ICanTalon follower = canTalonFactory.getCanTalon(followerId);
				follower.setControlMode(ETalonControlMode.Follower);
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
			setMotorMode(ETalonControlMode.PercentVbus);
			break;
		case TICK_VEL:
			setMotorMode(ETalonControlMode.PercentVbus);
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
	
	public void setMotorMode(ETalonControlMode talonMode){
		for(ICanTalon talon : motorMap.values()){
			talon.setControlMode(talonMode);
		}
	}
	
	public ETalonControlMode getMotorMode(){
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
				motorMap.get(MotorType.LEFT_MOTOR).setFeedbackDevice(EFeedbackDevice.AnalogEncoder);
				motorMap.get(MotorType.RIGHT_MOTOR).setFeedbackDevice(EFeedbackDevice.AnalogEncoder);
				driverStation.reportError(String.format("Left:%d, Right:%d", motorMap.get(MotorType.LEFT_MOTOR).getEncVelocity(), motorMap.get(MotorType.RIGHT_MOTOR).getEncVelocity()), false); 
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
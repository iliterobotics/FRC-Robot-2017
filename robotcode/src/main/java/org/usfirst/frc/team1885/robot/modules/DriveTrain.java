package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.coms.ConstantUpdater;
import org.usfirst.frc.team1885.robot.common.impl.DefaultCanTalonFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalonFactory;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Class for running all drive train control operations from both autonomous and
 * driver-control
 */
public class DriveTrain implements Module {

	public static final double WHEEL_DIAMETER = 3.98;
	public static final int SHIFT_SOLENOID_ID = 2;
	public static final int CASTER_SOLENOID_ID = 7;
	// Voltage proportion control variables
	private static final double DEFAULT_RAMP_RATE = 72.0; // in V/sec
	private static final double HIGH_GEAR_RAMP_RATE = 120.0; // in V/sec

	private double desiredLeftPower;
	private double desiredRightPower;
	private double actualLeftPower;
	private double actualRightPower;

	// Speed control variables

	private int desiredLeftSpeed;
	private int desiredRightSpeed;
	private int actualLeftSpeed;
	private int actualRightSpeed;

	// Position control variables

	private DriveMode currentMode;

	public enum DriveMode {
		P_VBUS, POSITION, TICK_VEL;
	}

	private enum MotorType {
		LEFT_MOTOR(-1, 2, 4), RIGHT_MOTOR(1, 3, 1);

		final int talonId;
		final int followerIds[];
		final double modifier;

		MotorType(double modifier, int talonId, int... followerIds) {
			this.modifier = modifier;
			this.talonId = talonId;
			this.followerIds = followerIds;
		}
	}

	private Map<MotorType, CANTalon> motorMap;
	private Solenoid gearShifter;
	private Solenoid casterShifter;

	private final ICanTalonFactory canTalonFactory;

	public DriveTrain() {
		this(new DefaultCanTalonFactory());
	}

	public DriveTrain(ICanTalonFactory canTalonFactory) {
		this.canTalonFactory = canTalonFactory;
		motorMap = new HashMap<>();
		gearShifter = new Solenoid(SHIFT_SOLENOID_ID);
		casterShifter = new Solenoid(CASTER_SOLENOID_ID);
		for (MotorType type : MotorType.values()) {
			CANTalon talon = new CANTalon(type.talonId);
			talon.setEncPosition(0);
			talon.setP(0.5);
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			for (int followerId : type.followerIds) {
				CANTalon follower = new CANTalon(followerId);
				follower.setControlMode(TalonControlMode.Follower.value);
				follower.set(type.talonId);
			}
			motorMap.put(type, talon);
		}
	}

	@Override
	public void initialize() {
		setMode(DriveMode.P_VBUS);
		ConstantUpdater.putNumber("leftpos", 0);
		ConstantUpdater.putNumber("rightpos", 0);
		ConstantUpdater.putNumber("leftvel", 0);
		ConstantUpdater.putNumber("rightvel", 0);
		ConstantUpdater.putNumber("drive_train_current", 0);
	}

	private void setMode(DriveMode mode) {
		if (currentMode == mode)
			return;
		this.currentMode = mode;
		switch (currentMode) {
		case P_VBUS:
			actualLeftPower = 0;
			actualRightPower = 0;
			desiredLeftPower = 0;
			desiredRightPower = 0;
			setMotorMode(TalonControlMode.PercentVbus);
			setVoltageRampRate(DEFAULT_RAMP_RATE);
			break;
		case TICK_VEL:
			actualLeftSpeed = 0;
			actualRightSpeed = 0;
			desiredLeftSpeed = 0;
			desiredRightSpeed = 0;
			setVoltageRampRate(Integer.MAX_VALUE);
			setMotorMode(TalonControlMode.Speed);
			motorMap.get(MotorType.LEFT_MOTOR).setFeedbackDevice(FeedbackDevice.AnalogEncoder);
			motorMap.get(MotorType.RIGHT_MOTOR).setFeedbackDevice(FeedbackDevice.AnalogEncoder);
			break;
		case POSITION:
			setVoltageRampRate(Integer.MAX_VALUE);
			break;
		}
	}

	public void setShift(boolean shift) {
		gearShifter.set(shift);
		if(shift){
			setVoltageRampRate(HIGH_GEAR_RAMP_RATE);			
		}else{
			setVoltageRampRate(DEFAULT_RAMP_RATE);
		}
	}

	public void lowerCasters(boolean lowered) {
		casterShifter.set(lowered);
	}

	private void setVoltageRampRate(double rate) {
		motorMap.get(MotorType.LEFT_MOTOR).setVoltageRampRate(rate);
		motorMap.get(MotorType.RIGHT_MOTOR).setVoltageRampRate(rate);
	}

	public int getLeftPosition() {
		return motorMap.get(MotorType.LEFT_MOTOR).getEncPosition();
	}

	public int getRightPosition() {
		return motorMap.get(MotorType.RIGHT_MOTOR).getEncPosition();
	}

	public int getLeftEncoderVelocity() {
		return motorMap.get(MotorType.LEFT_MOTOR).getEncVelocity();
	}

	public int getRightEncoderVelocity() {
		return motorMap.get(MotorType.RIGHT_MOTOR).getEncVelocity();
	}

	public void setPower(double left, double right) {
		setMode(DriveMode.P_VBUS);
		desiredLeftPower = left;
		desiredRightPower = right;
	}

	public void setSpeed(double left, double right) {
		setMode(DriveMode.TICK_VEL);
	}

	public void setPosition(double left, double right) {
		setMode(DriveMode.POSITION);
	}

	private void setMotor(MotorType type, double value) {
		motorMap.get(type).set(value * type.modifier);
	}

	public void setMotorMode(TalonControlMode talonMode) {
		for (CANTalon talon : motorMap.values()) {
			talon.setControlMode(talonMode.value);
		}
	}

	public TalonControlMode getMotorMode() {
		if (!motorMap.values().isEmpty()) {
			return motorMap.values().iterator().next().getControlMode();
		}
		return null;
	}

	@Override
	public void update() {
		switch (currentMode) {
		case P_VBUS:
			actualLeftPower = desiredLeftPower;
			actualRightPower = desiredRightPower;
			setMotor(MotorType.LEFT_MOTOR, actualLeftPower);
			setMotor(MotorType.RIGHT_MOTOR, actualRightPower);
			break;
		case TICK_VEL:
			actualLeftSpeed = desiredLeftSpeed;
			actualRightSpeed = desiredRightSpeed;
			setMotor(MotorType.LEFT_MOTOR, actualLeftSpeed);
			setMotor(MotorType.RIGHT_MOTOR, actualRightSpeed);
			break;
		case POSITION:
			break;
		}
		ConstantUpdater.putNumber("leftpos", getLeftPosition());
		ConstantUpdater.putNumber("rightpos", getRightPosition());
		ConstantUpdater.putNumber("leftvel", getLeftEncoderVelocity());
		ConstantUpdater.putNumber("rightvel", getRightEncoderVelocity());
		ConstantUpdater.putNumber("drive_train_current",  getCurrentFeedback());

	}
	
	public double getCurrentFeedback(){
		return (motorMap.get(MotorType.LEFT_MOTOR).getOutputCurrent() + motorMap.get(MotorType.RIGHT_MOTOR).getOutputCurrent())/2;
	}

	public void setCasting(boolean casted) {
		casterShifter.set(casted);
	}

}
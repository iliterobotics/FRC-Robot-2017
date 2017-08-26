package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.coms.ConstantUpdater;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Class for running all drive train control operations from both autonomous and
 * driver-control
 */
public class DriveTrain implements Module {

	public static final double MAX_VELOCITY_RPM = 0;
	
	//Motion Magic (Velocity) constants
	public static final double MAGIC_ACCEL = 0; //RPM per second
	public static final double MAGIC_CRUISE_VEL = 0; //RPM
	public static final int MAGIC_PROFILE_SLOT = 0;
	
	public static final double LEFT_RAMPRATE = 0;
	public static final int LEFT_IZONE = 0; //Encoder Ticks
	public static final double LEFT_kP = 0;
	public static final double LEFT_kI = 0;
	public static final double LEFT_kD = 0;
	public static final double LEFT_kF = 0;
	
	public static final double RIGHT_RAMPRATE = 0;
	public static final int RIGHT_IZONE = 0; //Encoder Ticks
	public static final double RIGHT_kP = 0;
	public static final double RIGHT_kI = 0;
	public static final double RIGHT_kD = 0;
	public static final double RIGHT_kF = 0;
	
	public static final double WHEEL_DIAMETER = 3.98;
	public static final int SHIFT_SOLENOID_ID = 2;
	// Voltage proportion control variables
	private static final double DEFAULT_RAMP_RATE = 72.0; // in V/sec- 2017 ramp rate
	private static final double HIGH_GEAR_RAMP_RATE = 120.0; // in V/sec= 2017 ramp rate

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
	
	private double desiredLeftPosition;
	private double desiredRightPosition;
	private double actualLeftPosition;
	private double actualRightPosition;

	private DriveMode currentMode;

	public enum DriveMode {
		P_VBUS, POSITION, MOTION_MAGIC, TICK_VEL;
	}

	private enum MotorType {
		LEFT_MOTOR(-1, 1, 3, 5), RIGHT_MOTOR(1, 2, 4, 6);

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

	public DriveTrain() {
		motorMap = new HashMap<>();
		gearShifter = new Solenoid(SHIFT_SOLENOID_ID);
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
			//setVoltageRampRate(DEFAULT_RAMP_RATE);
			break;
		case TICK_VEL:
			actualLeftSpeed = 0;
			actualRightSpeed = 0;
			desiredLeftSpeed = 0;
			desiredRightSpeed = 0;
			//setVoltageRampRate(Integer.MAX_VALUE);
			setMotorMode(TalonControlMode.Speed);
			setFeedbackDevice(FeedbackDevice.AnalogEncoder);
			break;
		case POSITION:
			actualLeftPosition = 0;
			actualRightPosition = 0;
			desiredLeftPosition = 0;
			desiredRightPosition = 0;
			setVoltageRampRate(Integer.MAX_VALUE);
			break;
		case MOTION_MAGIC:
			actualLeftPosition = 0;
			actualRightPosition = 0;
			desiredLeftPosition = 0;
			desiredRightPosition = 0;
			setFeedbackDevice(FeedbackDevice.QuadEncoder);
			setMotorMode(TalonControlMode.MotionMagic);
			CANTalon leftMotor = motorMap.get(MotorType.LEFT_MOTOR);
			CANTalon rightMotor = motorMap.get(MotorType.RIGHT_MOTOR);
			leftMotor.setPID(LEFT_kP, LEFT_kI, LEFT_kD, LEFT_kF, LEFT_IZONE, LEFT_RAMPRATE, MAGIC_PROFILE_SLOT);
			leftMotor.setMotionMagicAcceleration(MAGIC_ACCEL);
			leftMotor.setMotionMagicCruiseVelocity(MAGIC_CRUISE_VEL);
			rightMotor.setPID(RIGHT_kP, RIGHT_kI, RIGHT_kD, RIGHT_kF, RIGHT_IZONE, RIGHT_RAMPRATE, MAGIC_PROFILE_SLOT);
			rightMotor.setMotionMagicAcceleration(MAGIC_ACCEL);
			rightMotor.setMotionMagicCruiseVelocity(MAGIC_CRUISE_VEL);
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
	
	public void setTrapezoidalPosition(double left, double right) {
		setMode(DriveMode.MOTION_MAGIC);
		desiredLeftPosition = left;
		desiredRightPosition = right;
	}
	
	public void changeTrapezoidalPosition(double leftDelta, double rightDelta) {
		setTrapezoidalPosition((getLeftPosition() + leftDelta) % 1024, (getRightPosition() + rightDelta) % 1024);
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
		case MOTION_MAGIC:
			actualLeftPosition = desiredLeftPosition;
			actualRightPosition = desiredRightPosition;
			setMotor(MotorType.LEFT_MOTOR, actualLeftPosition);
			setMotor(MotorType.RIGHT_MOTOR, actualRightPosition);
			break;
		}
		ConstantUpdater.putNumber("leftpos", getLeftPosition());
		ConstantUpdater.putNumber("rightpos", getRightPosition());
		ConstantUpdater.putNumber("leftvel", getLeftEncoderVelocity());
		ConstantUpdater.putNumber("rightvel", getRightEncoderVelocity());
		ConstantUpdater.putNumber("drive_train_current",  getCurrentFeedback());

	}
	
	private void setFeedbackDevice(FeedbackDevice device) {
		for(MotorType m : MotorType.values()) {
			motorMap.get(m).setFeedbackDevice(device);
		}
	}
	
	public double getCurrentFeedback(){
		return (motorMap.get(MotorType.LEFT_MOTOR).getOutputCurrent() + motorMap.get(MotorType.RIGHT_MOTOR).getOutputCurrent())/2;
	}

}
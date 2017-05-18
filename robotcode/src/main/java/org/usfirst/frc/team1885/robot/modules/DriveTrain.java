package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Talon;

/**
 * Class for running all drive train control operations from both autonomous and
 * driver-control
 */
public class DriveTrain implements Module {

	public static final double WHEEL_DIAMETER = 3.98;
	// Voltage proportion control variables
	
	private static final int LEFT_MOTOR_ID = 0;
	private static final int RIGHT_MOTOR_ID = 0;

	private double desiredLeftSpeed;
	private double desiredRightSpeed;
	private double actualLeftSpeed;
	private double actualRightSpeed;

	public enum DriveMode {
		P_VBUS, POSITION, TICK_VEL;
	}

	private enum MotorType {
		LEFT_MOTOR(-1, LEFT_MOTOR_ID), RIGHT_MOTOR(1, RIGHT_MOTOR_ID);

		final int talonId;
		final double modifier;

		MotorType(double modifier, int talonId) {
			this.modifier = modifier;
			this.talonId = talonId;
		}
	}

	private Map<MotorType, PWM> motorMap;

	public DriveTrain() {
		motorMap = new HashMap<>();
		for (MotorType type : MotorType.values()) {
			PWM talon = new Talon(type.talonId);
			motorMap.put(type, talon);
		}
	}

	@Override
	public void initialize() {
	}

	public void setSpeed(double left, double right) {
		desiredLeftSpeed = left;
		desiredRightSpeed = right;
	}

	private void setMotor(MotorType type, double value) {
		motorMap.get(type).setSpeed(value * type.modifier);
	}

	@Override
	public void update() {
		actualLeftSpeed = desiredLeftSpeed;
		actualRightSpeed = desiredRightSpeed;
		setMotor(MotorType.LEFT_MOTOR, actualLeftSpeed);
		setMotor(MotorType.RIGHT_MOTOR, actualRightSpeed);

	}

}
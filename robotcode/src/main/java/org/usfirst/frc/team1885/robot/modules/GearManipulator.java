package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.robot.common.impl.DefaultCanTalonFactory;
import org.usfirst.frc.team1885.robot.common.impl.ETalonControlMode;
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalon;
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalonFactory;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Servo;

public class GearManipulator implements Module{
	private static final int INTAKE_MOTOR_ID = 7;
	
	private static final double SERVO_DEFAULT = 0.0;
	private static final double LEFT_SERVO_POSITION = 0.3;
	private static final double RIGHT_SERVO_POSITION = 0.5;
	
	private Map<ServoType, Servo> servoMap;
	private Map<PneumaticType, DoubleSolenoid> solenoidMap;
	private ICanTalon intakeMotor;
	private ICanTalonFactory canTalonFactory;
	
	
	private enum PneumaticType {
		LEFT_INTAKE(0,1), RIGHT_INTAKE(2,3);
		
		final int port1;
		final int port2;
		Value value;
		
		PneumaticType(int port1, int port2) {
			this.port1 = port1;
			this.port2 = port2;
			value = Value.kReverse;
		}
		
		void changeValue(){
			value = value.equals(Value.kReverse) ? Value.kForward : Value.kReverse;
		}
	}
	
	private enum ServoType {
		RIGHT_GEAR_HOLD(0), LEFT_GEAR_HOLD(1);
		
		final int channel;
		
		ServoType(int channel) {
			this.channel = channel;
		}
	}
	
	public GearManipulator() {
		this(new DefaultCanTalonFactory());
	}
	
	public GearManipulator(ICanTalonFactory canTalonFactory) {
		servoMap = new HashMap<>();
		solenoidMap = new HashMap<>();		
		this.canTalonFactory = canTalonFactory;		
	}
	
	@Override
	public void initialize() {
		this.intakeMotor = canTalonFactory.getCanTalon(INTAKE_MOTOR_ID);
		for(ServoType type : ServoType.values()) {
			Servo servo = new Servo(type.channel);
			servoMap.put(type, servo);
		}		
		for(PneumaticType type : PneumaticType.values()) {
			DoubleSolenoid solenoid = new DoubleSolenoid(type.port1, type.port2);
			solenoidMap.put(type, solenoid);
		}
		intakeMotor = canTalonFactory.getCanTalon(INTAKE_MOTOR_ID);
		intakeMotor.setControlMode(ETalonControlMode.PercentVbus);
	}
	
	public void updateServos(boolean servoToggle) {		
		if(servoToggle) {
			servoMap.get(servoMap.get(ServoType.LEFT_GEAR_HOLD)).set(SERVO_DEFAULT);
			servoMap.get(servoMap.get(ServoType.RIGHT_GEAR_HOLD)).set(SERVO_DEFAULT);
		}
		else {
			servoMap.get(servoMap.get(ServoType.LEFT_GEAR_HOLD)).set(LEFT_SERVO_POSITION);
			servoMap.get(servoMap.get(ServoType.RIGHT_GEAR_HOLD)).set(RIGHT_SERVO_POSITION);
		}
	}
	
	public void updatePneumatics(boolean intakeToggle) {
		if(intakeToggle) {
			PneumaticType.LEFT_INTAKE.changeValue();
			PneumaticType.RIGHT_INTAKE.changeValue();
		}
		solenoidMap.get(PneumaticType.LEFT_INTAKE).set(PneumaticType.LEFT_INTAKE.value);
		solenoidMap.get(PneumaticType.RIGHT_INTAKE).set(PneumaticType.RIGHT_INTAKE.value);
	}
	
	public void updateIntake(double value) {
		intakeMotor.set(value);
	}

	@Override
	public void update() {
	}
	
}

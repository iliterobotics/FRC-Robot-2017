package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.PWM;

public class Shooter implements Module {

	private static final int SHOOTER_ID = 0;
	private static final int DUMP_ID = 0;
	private static final int PRESSURE_SENSOR_ID = 0;
	private static final int ANGLE_POTENTIOMETER_ID = 0;
	private static final int ELEVATION_MOTOR_ID = 0;
	
	private static final double DEGREES_PER_VOLT = 0;
	
	private static final int ELEVATON_PWM_MAX = 0;
	private static final int ELEVATON_PWM_MIN = 0;
	private static final int ELEVATON_DEADBAND_MAX = 0;
	private static final int ELEVATON_DEADBAND_CENTER = 0;
	private static final int ELEVATON_DEADBAND_MIN = 0;
	
	private static final double KP = 0;
	private static final double KI = 0;
	private static final double KD = 0;
	private static final double KF = 0;
	
	private static final long ALLOWABLE_ERROR = 0;
	
	private static final double LOW_PRESSURE = 5;
	private static final double SHOT_TIME_MS = 200;
	
	private DigitalOutput dump, shooter;
	private AnalogInput pressureSensor, anglePot;
	private PWM elevationMotor;
	
	private boolean isShooting, isDumping;
	private long shotTime;
	
	private double error, lastError, totalError;
	private double targetAngle;
	
	
	public Shooter() {
		
		dump = new DigitalOutput(DUMP_ID);
		shooter = new DigitalOutput(SHOOTER_ID);
		pressureSensor = new AnalogInput(PRESSURE_SENSOR_ID);
		anglePot = new AnalogInput(ANGLE_POTENTIOMETER_ID);
		elevationMotor = new PWM(ELEVATION_MOTOR_ID);
		
		isShooting = isDumping = false;
		shotTime = 0;
		
	}
	
	@Override
	public void initialize() {
		
		dump.disablePWM();
		shooter.disablePWM();
		elevationMotor.setBounds(ELEVATON_PWM_MAX, ELEVATON_DEADBAND_MAX, ELEVATON_DEADBAND_CENTER, ELEVATON_DEADBAND_MIN, ELEVATON_PWM_MIN);
		
	}

	@Override
	public void update() {
		
		error = targetAngle - getAngle();
		totalError += error;
		
		double output = ((KP * error) + (KI * totalError) + (KD * (error - lastError)));
		
		elevationMotor.setSpeed(output);
		
		if(getPressure() <= LOW_PRESSURE) dump(); 
		if(shooter.get() && (System.currentTimeMillis() - shotTime) >= SHOT_TIME_MS) shooter.set(false); 
		
		safety();
		
		if(isDumping) dump.set(true); 
		if(isShooting) {
			shooter.set(true);
			shotTime = System.currentTimeMillis();
		}
		
		lastError = error;
		
	}
	
	public void setAngle(double targetAngle) {
		this.targetAngle = targetAngle;
	}
	
	public void safety() {
		if(isShooting && isDumping) {
			isDumping = false;
			isShooting = false;
		}
		if(dump.get() && shooter.get()) {
			dump.set(false);
			shooter.set(false);
		}
		
	}
	
	public void shoot() {
		isShooting = true;
	}
	
	private void dump() {
		isDumping = true;
	}
	
	public double getPressure() {
		return 1.0;
	}
	
	public double getAngle() {
		return anglePot.getVoltage() / DEGREES_PER_VOLT;
	}

}

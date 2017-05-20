package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;

public class Shooter implements Module {

	private static final int SHOOTER_ID = 0;
	private static final int DUMP_ID = 1;
	private static final int PRESSURE_SENSOR_ID = 2;
	
	private static final double LOW_PRESSURE = 5;
	private static final double SHOT_TIME_MS = 200;
	
	private DigitalOutput dump, shooter;
	private AnalogInput pressureSensor;
	
	private boolean isShooting, isDumping;
	private long shotTime;
	
	public Shooter() {
		
		dump = new DigitalOutput(DUMP_ID);
		shooter = new DigitalOutput(SHOOTER_ID);
		pressureSensor = new AnalogInput(PRESSURE_SENSOR_ID);
		
		isShooting = isDumping = false;
		shotTime = 0;
		
	}
	
	@Override
	public void initialize() {
		
		dump.disablePWM();
		shooter.disablePWM();
		
	}

	@Override
	public void update() {
		
		if(getPressure() <= LOW_PRESSURE) dump(); 
		if(shooter.get() && (System.currentTimeMillis() - shotTime) >= SHOT_TIME_MS) setShooterState(false); 
		
		safety();
		
		if(isDumping) dump.set(true); 
		if(isShooting) {
			shooter.set(true);
			shotTime = System.currentTimeMillis();
		}
		
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

}

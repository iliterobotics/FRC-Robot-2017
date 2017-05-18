package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;

public class Shooter implements Module {

	private static final int SHOOTER_RELAY_ID = 0;
	private static final int DUMP_RELAY_ID = 1;
	private static final int PRESSURE_SENSOR_ID = 2;
	
	private static final double LOW_PRESSURE = 5;
	private static final double SHOT_TIME_MS = 200;
	
	private Relay dump, shooter;
	private AnalogInput pressureSensor;
	
	private boolean isShooting, isDumping;
	private long shotTime;
	
	public Shooter() {
		
		dump = new Relay(DUMP_RELAY_ID);
		shooter = new Relay(SHOOTER_RELAY_ID);
		pressureSensor = new AnalogInput(PRESSURE_SENSOR_ID);
		
		isShooting = isDumping = false;
		shotTime = 0;
		
	}
	
	@Override
	public void initialize() {

		dump.setDirection(Direction.kForward);
		shooter.setDirection(Direction.kForward);
		
	}

	@Override
	public void update() {
		
		if(getPressure() <= LOW_PRESSURE) dump(); 
		if(getShooterState() && (System.currentTimeMillis() - shotTime) >= SHOT_TIME_MS) setShooterState(false); 
		
		safety();
		
		if(isDumping) setDumpState(true); 
		if(isShooting) {
			setShooterState(true);
			shotTime = System.currentTimeMillis();
		}
		
	}
	
	public void safety() {
		if(isShooting && isDumping) {
			isDumping = false;
			isShooting = false;
		}
		if(getDumpState() && getShooterState()) {
			setDumpState(false);
			setShooterState(false);
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
	
	private void setDumpState(boolean state) {
		if(state) {
			dump.set(Value.kOn);
		} else {
			dump.set(Value.kOff);
		}
	}
	
	private void setShooterState(boolean state) {
		if(state) {
			shooter.set(Value.kOn);
		} else {
			shooter.set(Value.kOff);
		}
	}
	
	public boolean getDumpState() {
		return (dump.get() == Value.kOn) ? true : false;
	}
	
	public boolean getShooterState() {
		return (shooter.get() == Value.kOn) ? true : false;
	}

}

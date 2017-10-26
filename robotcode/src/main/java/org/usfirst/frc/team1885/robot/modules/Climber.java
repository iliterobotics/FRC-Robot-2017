package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.coms.ConstantUpdater;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;



public class Climber implements Module{

	private static final double MAX_CURRENT_V_RATIO = 5;
	private static final double CLIMBER_POWER = 1.0;
	private static final double PULSE_POWER = 0.35;
	
	private static final int[] TALON_IDS = {7, 8};
	
	public enum ClimberState{
		INIT, PULSING, PAUSE, CLIMBING, STALLED, BUMPING;
	}
	
	private ClimberState currentState;
	
	private CANTalon masterTalon;
	private double currentPower;
	
	private static final int MAX_STALL_TIME = 50;
	private static final int MAX_BUMP_TIME = 100;
	private static final int MAX_PAUSE_TIME = 200;
	private static final int MAX_PULSE_TIME = 100;
	
	private boolean broken;
	
	private boolean didStall;
	
	private long initStallTime;
	private long initPauseTime;
	private long initPulseTime;
	private long initBumpTime;
	
	public Climber(){
		masterTalon = new CANTalon(TALON_IDS[0]);
		for(int i = 1; i < TALON_IDS.length; i++){
			CANTalon talon = new CANTalon(TALON_IDS[i]);
			talon.setControlMode(TalonControlMode.Follower.value);
			talon.set(TALON_IDS[0]);
		}
		masterTalon.setControlMode(TalonControlMode.PercentVbus.value);
	}
	
	public void initialize() {
		currentState = ClimberState.INIT;
		ConstantUpdater.putNumber("current", 0);
		ConstantUpdater.putNumber("voltage", 0);
	}
	
	public void run(){
		switch(currentState){
		case INIT:
			currentState = ClimberState.PULSING;
			initPulseTime = System.currentTimeMillis();
			break;
		case PAUSE:
			currentState = ClimberState.CLIMBING;
			break;
		case STALLED:
			currentState = ClimberState.BUMPING;
			initBumpTime = System.currentTimeMillis();
			break;
		case CLIMBING:
			currentState = ClimberState.STALLED;
		default:
			break;
		}
	}
	
	public void update() {
		
		switch(currentState){
		case INIT:
		case STALLED:
			currentPower = 0;
			break;
		case PULSING:
			currentPower = PULSE_POWER;
			if((System.currentTimeMillis() - initPulseTime) >= MAX_PULSE_TIME){
				currentState = ClimberState.PAUSE;
				initPauseTime = System.currentTimeMillis();
			}
			break;
		case PAUSE:
			currentPower = 0;
			if((System.currentTimeMillis() - initPauseTime) >= MAX_PAUSE_TIME){
				currentState = ClimberState.CLIMBING;
			}
			break;
		case CLIMBING:
			currentPower = CLIMBER_POWER;
			double current = masterTalon.getOutputCurrent();
			double voltage = masterTalon.getOutputVoltage();
			ConstantUpdater.putNumber("current", current);
			ConstantUpdater.putNumber("voltage", voltage);
			double ratio = current/voltage;
			System.out.println("Ratio: " + ratio );
			if(ratio > MAX_CURRENT_V_RATIO){
				if(!didStall){
					initStallTime = System.currentTimeMillis();
				}
				if( (System.currentTimeMillis() - initStallTime) >= MAX_STALL_TIME){
					currentState = ClimberState.STALLED;
				}
				didStall = true;
			} else {
				didStall = false;
			}
			break;
		case BUMPING:
			currentPower = CLIMBER_POWER;
			if((System.currentTimeMillis() - initBumpTime) >= MAX_BUMP_TIME){
				currentState = ClimberState.STALLED;
			}
			break;
		}
		masterTalon.set(currentPower);
	}
	
	public void setClimberPower(double power){
		currentPower = power;
	}
	
	public ClimberState getClimberState(){
		return currentState;
	}
	
	public void setClimberState(ClimberState state) {
		currentState = state;
	}

}

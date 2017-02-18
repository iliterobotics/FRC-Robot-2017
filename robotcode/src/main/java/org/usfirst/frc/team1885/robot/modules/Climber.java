package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.Robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class Climber implements Module{

	private static final double MAX_CURRENT_V_RATIO = 2;
	private static final double CLIMBER_POWER = 1.0;
	private static final int[] TALON_IDS = {7, 8};
	
	public enum ClimberState{
		INIT, PULSING, READY_TO_CLIMB, CLIMBING, STALLED, BUMPING;
	}
	
	private ClimberState currentState;
	
	private CANTalon masterTalon;
	private double currentPower;
	
	private static final int MAX_STALL_TIME = 200;
	private static final int MAX_BUMP_TIME = 100;
	private static final int MAX_PULSE_TIME = 200;
	
	private boolean broken;
	
	private int stallTime;
	private int pulseTime;
	private int bumpTime;
	
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
	}
	
	public void run(){
		switch(currentState){
		case INIT:
			currentState = ClimberState.PULSING;
			break;
		case READY_TO_CLIMB:
			currentState = ClimberState.CLIMBING;
			break;
		case STALLED:
			currentState = ClimberState.BUMPING;
			break;
		default:
			break;
		}
	}
	
	public void update() {
		
		switch(currentState){
		case INIT:
		case READY_TO_CLIMB:
		case STALLED:
			currentPower = 0;
		case PULSING:
			currentPower = CLIMBER_POWER;
			pulseTime += Robot.UPDATE_PERIOD;
			if(pulseTime >= MAX_PULSE_TIME){
				currentState = ClimberState.READY_TO_CLIMB;
			}
		case CLIMBING:
			currentPower = CLIMBER_POWER;
			double current = masterTalon.getOutputCurrent();
			double voltage = masterTalon.getOutputVoltage();
			double ratio = current/voltage;
			if(ratio > MAX_CURRENT_V_RATIO){
				stallTime += Robot.UPDATE_PERIOD;
				if(stallTime >= MAX_STALL_TIME){
					currentState = ClimberState.STALLED;
				}
			} else {
				stallTime = 0;
			}
		case BUMPING:
			currentPower = CLIMBER_POWER;
			bumpTime += Robot.UPDATE_PERIOD;
			if(bumpTime >= MAX_BUMP_TIME){
				currentState = ClimberState.STALLED;
				bumpTime = 0;
			}			
		}
		masterTalon.set(currentPower);
	}
	
	public void setClimberPower(double power){
		currentPower = power;
	}
	
	public ClimberState getClimberState(){
		return currentState;
	}

}

package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.Robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class Climber implements Module{

	private static final double MAX_CURRENT_V_RATIO = 2;
	private static final double CLIMBER_POWER = 0.8;
	private static final int MAX_STALL_TIME = 200;
	private static final int BUMP_TIME = 100;
	private static final int[] TALON_IDS = {7, 8};
	
	private CANTalon masterTalon;
	
	private boolean isRunning;
	private boolean hasStalled;
	private boolean isBumping;
	
	private int stallTime;
	private int bumpTime;
	
	public Climber(){		
	}
	
	public void initialize() {
		masterTalon = new CANTalon(TALON_IDS[0]);
		for(int i = 1; i < TALON_IDS.length; i++){
			CANTalon talon = new CANTalon(TALON_IDS[i]);
			talon.setControlMode(TalonControlMode.Follower.value);
			talon.set(TALON_IDS[0]);
		}
		masterTalon.setControlMode(TalonControlMode.PercentVbus.value);
	}
	
	public void run(){
		if(!hasStalled){
			isRunning = true;
		}else{
			bump();
		}
	}
	
	public void bump(){
		if(isRunning) return;
		isBumping = true;
		bumpTime = 0;
	}

	public void update() {
		
		if(isRunning){
			
			double current = masterTalon.getOutputCurrent();
			double voltage = masterTalon.getBusVoltage();
		
			if(current / voltage >= MAX_CURRENT_V_RATIO){
				stallTime += Robot.UPDATE_PERIOD;
				if(stallTime >= MAX_STALL_TIME){
					isRunning = false;
					hasStalled = true;
				}
			}else{
				stallTime = 0;
			}
		
		} else if(isBumping) {
			bumpTime += Robot.UPDATE_PERIOD;
			if(bumpTime >= BUMP_TIME){
				isBumping = false;
			}
		}
		
		if(isRunning || isBumping){
			masterTalon.set(CLIMBER_POWER);
		}
		else{
			masterTalon.set(0);
		}
	}

}

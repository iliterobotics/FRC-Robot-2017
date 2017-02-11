package org.usfirst.frc.team1885.robot.modules;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1885.robot.common.impl.ETalonControlMode;
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalon;
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalonFactory;

import edu.wpi.first.wpilibj.DriverStation;

public class Climber implements Module {
	
	private static final int[] MOTOR_IDS = {7, 8};
		
	private static final int id = 5;
	private static final double MAX_CURRENT_V_RATIO = 2;
	private static final int MAX_BURN_TIME = 250;
	
	private final List<ICanTalon> talons;
	private final ICanTalonFactory talonFactory;
	
	private boolean isRunning; 
	private int timeBurning;
	
	public Climber(ICanTalonFactory factory){
		this.talons = new ArrayList<>();
		this.talonFactory = factory;
	}
	
	public void setRunning(boolean running){
		isRunning = running;
	}
	
	public void update() {
		
		double current = getAverageOutputCurrent();
		double voltage = getAverageBusVoltage();
		
		if (current/voltage > MAX_CURRENT_V_RATIO) {
			timeBurning += 5;
			if(timeBurning >= MAX_BURN_TIME){
				isRunning = false;
			}
		}
		else{
			timeBurning = 0;
		}
		
		if(isRunning){
			setOutputPower(0.6);
		} else {
			setOutputPower(0.0);
		}

		DriverStation.reportError(String.format("I:%f, V:%f, I/V:%f", current, voltage, current/voltage), false);
	}
	
	private void setOutputPower(double power){
		for(ICanTalon talon : talons){
			talon.set(power);
		}
	}
	
	private double getAverageOutputCurrent(){
		double sum = 0;
		double count = 0;
		for(ICanTalon talon : talons){
			sum += talon.getOutputCurrent();
			count++;
		}
		return count == 0?0:sum/count;
	}
	
	private double getAverageBusVoltage(){
		double sum = 0;
		double count = 0;
		for(ICanTalon talon : talons){
			sum += talon.getBusVoltage();
			count++;
		}
		return count == 0?0:sum/count;
	}

	@Override
	public void initialize() {
		for(int id : MOTOR_IDS){
			ICanTalon talon = talonFactory.getCanTalon(id);
			talon.setControlMode(ETalonControlMode.PercentVbus);
			talons.add(talon);
		}
	}

}

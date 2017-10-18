package org.usfist.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.modules.Module;

import com.ctre.MotorControl.CANTalon;
import com.ctre.MotorControl.SmartMotorController.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;

public class TestClamp implements Module {

	private CANTalon motor;
	
	private static final int id = 5;
	private static final double MAX_CURRENT_V_RATIO = 2;
	private static final int MAX_BURN_TIME = 250;
	
	private boolean isRunning; 
	private int timeBurning;
	

	public void update() {
		motor.setControlMode(TalonControlMode.PercentVbus.value);
		
		double current = motor.getOutputCurrent();
		double voltage = motor.getBusVoltage();
		
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
			motor.set(0.6);
		} else {
			motor.set(0);
		}

		DriverStation.reportError(String.format("I:%f, V:%f, I/V:%f", current, voltage, current/voltage), false);
	}

	@Override
	public void initialize() {
		motor = new CANTalon(id);
		isRunning = true;
	}

}

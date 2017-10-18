package org.usfirst.frc.team1885.robot.modules.telemetry;

import org.usfirst.frc.team1885.robot.modules.Module;

import com.ctre.MotorControl.CANTalon;

import edu.wpi.first.wpilibj.DriverStation;

public class TalonData implements Module{
	
	private CANTalon motor;
	
	private final int id;
	private double voltage;
	private double position;
	private double current; 
	private double outputVoltage;
	private double timeStamp;
	
	
	public TalonData( int id ){
		this.id = id;
	}
	


	public void initialize() {
		motor = new CANTalon(id);
	}

	public void update() {
		voltage = motor.getBusVoltage();
		position = motor.getPosition();
		current = motor.getOutputCurrent();
		outputVoltage = motor.getOutputVoltage();
		timeStamp = DriverStation.getInstance().getMatchTime();
	}


	public double getTimeStamp() {
		return timeStamp;
	}
	public double getVoltage() {
		return voltage;
	}
	
	public double getPosition()	{
		return position;
	}



	public double getCurrent() {
		return current;
	}


	public double outputVoltage() {
		return outputVoltage;
	}



}

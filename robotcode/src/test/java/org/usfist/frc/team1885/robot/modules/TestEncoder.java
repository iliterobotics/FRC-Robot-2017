package org.usfist.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.modules.Module;

import com.ctre.MotorControl.CANTalon;
import com.ctre.MotorControl.SmartMotorController.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;

public class TestEncoder implements Module {
	private CANTalon motor;
	
	private final int id;
	
	public TestEncoder( int id )
	{
		this.id = id;
	}
	
	public void initialize() {
		motor = new CANTalon(id);
	}

	public void update() {
		motor.setControlMode(TalonControlMode.PercentVbus.value);
		DriverStation.reportError(String.format("Position:%f, Velocity:%f", motor.getPosition(), motor.getBusVoltage()), false);
	}

}
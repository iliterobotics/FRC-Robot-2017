package org.usfist.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.modules.Module;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;



public class VoltageValueTester implements Module{

	private static final int TALON_ID_TO_CHECK = 3;
	private CANTalon talon;
	
	@Override
	public void initialize() {
		talon = new CANTalon(TALON_ID_TO_CHECK);
	}

	@Override
	public void update() {
		talon.changeControlMode(TalonControlMode.PercentVbus);
		
	}

}

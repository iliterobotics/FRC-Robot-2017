package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.coms.ConstantGetter;
import org.usfirst.frc.team1885.robot.Robot;
import org.usfirst.frc.team1885.robot.modules.Module;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class VoltageValueTester implements Module{

	private static final int TALON_ID_TO_CHECK = 12;
	private static final double ANGLE_STEP = Math.PI / (1000/Robot.UPDATE_PERIOD);
	
	private CANTalon talon;
	private double currentAngle = 0.0;
	
	@Override
	public void initialize() {
		talon = new CANTalon(TALON_ID_TO_CHECK);
		talon.changeControlMode(TalonControlMode.PercentVbus);
	}

	@Override
	public void update() {
		currentAngle += ANGLE_STEP;
		talon.set(0);
		//System.out.printf("Voltage Bus:%f VoltageOutput:%f CurrentOuput:%f Tempurature:%f", talon.getBusVoltage(), talon.getOutputVoltage(), talon.getOutputCurrent(), talon.getTemperature());
		System.out.printf("Speed:%s", ConstantGetter.getConstant("speed"));
	}

}

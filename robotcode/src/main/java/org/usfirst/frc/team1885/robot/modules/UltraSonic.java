package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltraSonic implements Module{
	private Ultrasonic ultra;
	private DigitalOutput channel1; 
	private DigitalInput channel2;
	private double distanceInch;
	private double distanceMM;
	
	public double getInches()
	{
		return distanceInch;
	}
	
	public double getMM()
	{
		return distanceMM;
	}

	@Override
	public void initialize() {
		channel1 = new DigitalOutput(0);
		channel2 = new DigitalInput(1);
		ultra = new Ultrasonic(channel1, channel2);
	}

	@Override
	public void update() {
		distanceMM = ultra.getRangeMM();
		distanceInch = ultra.getRangeInches();
	}

}
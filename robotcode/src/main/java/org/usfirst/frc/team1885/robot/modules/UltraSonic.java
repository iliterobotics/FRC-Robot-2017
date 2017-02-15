package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltraSonic implements Module{
	
	private static final int CHANNEL = 0;
	private static final double MIN_VOLTAGE = 0.293;
	private static final double MIN_DIST_MM = 300;

	private static final double MAX_VOLTAGE = 4.885;
	private static final double MAX_DIST_MM = 5000;
	
	private AnalogInput ultra;
	private double distanceInch;
	private double distanceMM;
	
	private double sum;
	private double count;
	
	public double getMM()
	{
		return distanceMM;
	}

	@Override
	public void initialize() {
		ultra = new AnalogInput(CHANNEL);
	}

	@Override
	public void update() {
		if(count < 1000){
			sum += ultra.getAverageVoltage() / 0.000977;
			count++;
		}else{
			distanceMM = sum/count;
			sum = count = 0;
		}
	}

}
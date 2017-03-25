package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.DigitalInput;

public class BeamSensor implements Module {

	DigitalInput beamInput;
	
	private static final int INPUT_CHANNEL = 0;
	
	public BeamSensor() {
		beamInput = new DigitalInput(INPUT_CHANNEL);
	}

	@Override
	public void initialize() {

		
		
	}

	@Override
	public void update() {


		
	}
	
	public boolean isBroken() {
		return beamInput.get();
	}
	
}

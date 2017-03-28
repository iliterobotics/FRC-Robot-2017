package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.DigitalInput;

public class BeamSensor implements Module {

	DigitalInput beamInput;
	
	private static final int INPUT_CHANNEL = 1;
	
	public BeamSensor() {
		beamInput = new DigitalInput(INPUT_CHANNEL);
	}

	@Override
	public void initialize() {

		
		
	}

	@Override
	public void update() {

		System.out.println("BEAM IS " + isBroken());
		
	}
	
	public boolean isBroken() {
		return beamInput.get();
	}
	
}

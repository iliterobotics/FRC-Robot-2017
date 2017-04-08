package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.coms.ConstantUpdater;

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
		ConstantUpdater.putBoolean("beam-broken", isBroken());
	}
	
	public boolean isBroken() {
		return beamInput.get();
	}
	
}

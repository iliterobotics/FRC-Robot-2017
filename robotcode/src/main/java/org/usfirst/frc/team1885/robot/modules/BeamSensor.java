package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.DigitalInput;

public class BeamSensor implements Module {

	DigitalInput beamInput;
	
	private static final int INPUT_CHANNEL = 1;
	private long timeActivated;
	private long firstActivated; 
	private boolean currentState;
	private boolean lastState;
			
	public BeamSensor() {
		beamInput = new DigitalInput(INPUT_CHANNEL);
		timeActivated = 0;
		firstActivated = 0;
		currentState = false;
		lastState = false;
	}

	@Override
	public void initialize() {

		
		
	}

	@Override
	public void update() {

		currentState = isBroken();
		
		if(currentState == true && lastState == false) firstActivated = System.currentTimeMillis();
		
		if(lastState == true && currentState == true) {
			timeActivated = System.currentTimeMillis() - firstActivated;
		} else if(lastState == true && currentState==false) {
			firstActivated = 0;
			timeActivated = 0;
		}
		
		System.out.println("BEAM IS " + currentState);
		lastState = currentState;
		
	}
	
	public boolean isBroken() {
		return beamInput.get();
	}
	
	public long getTimeActivated() {
		return timeActivated;
	}
	
}

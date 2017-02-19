package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

public class PressureSensor implements Module{
    
    public static final int CHANNEL = 0;
    public static final int RELAY_PORT = 0;
    
    private Relay relay;
	private Compressor compressor;
	private DigitalInput dio;

    public PressureSensor() {
        dio = new DigitalInput(CHANNEL);
		relay = new Relay(RELAY_PORT);
    }
	
	public void initialize() {
		relay.set(Relay.Value.kForward);
	}
	
	public void update() {
		if ( !dio.get() )
		{
			relay.set(Relay.Value.kForward);
		}
		else
		{
			relay.set(Relay.Value.kOff);
		}
	}

}

package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Relay;

public class PressureSensor extends AnalogInput implements Module{
    
    public static final int NORMALIZED_PRESSURE = 120;
    public static final double NORMALIZED_VOLTAGE = 2.905273199081421;
    public static final double NORMALIZED_BATTERY_VOLTAGE = NORMALIZED_VOLTAGE / (.004 * NORMALIZED_PRESSURE + .1 );
    public static final double BATTERY_VOLTAGE = 5.0;
    private Relay relay;
	private Compressor compressor;

    public PressureSensor(int channel) {
        super(channel);
    }
    public double getPressure() {
        return (250.0 * (getVoltage()/NORMALIZED_BATTERY_VOLTAGE)) - 25;
    }
	
	public void initialize() {
		compressor = new Compressor(0);
		compressor.start();
		relay = new Relay(1);
		
	}
	
	public void update() {
		if ( getPressure() >= NORMALIZED_PRESSURE )
		{
			relay.set(Relay.Value.kOff);
		}
		else
		{
			relay.set(Relay.Value.kOn);
		}
	}

}

 package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

public class PressureSensor implements Module{
    
	public static final double PSI_PER_VOLTAGE = 1;

	public static final double LOW_VOLTAGE = 2.25;
	
    public static final int CHANNEL = 0;
    public static final int RELAY_PORT = 0;
    public static final int AIO_PORT = 0;
    
    private Relay relay;
	private Compressor compressor;
	private DigitalInput dio;
	private AnalogInput aio;
	private boolean isCompressorOn;
    private double voltageReadout;
	
	public PressureSensor() {
        dio = new DigitalInput(CHANNEL);
        aio = new AnalogInput(AIO_PORT);
		relay = new Relay(RELAY_PORT);
    }
	
	public void initialize() {
		relay.set(Relay.Value.kForward);
	}
	
	public void update() {
		if ( !dio.get() )
		{
			relay.set(Relay.Value.kForward);
			isCompressorOn = true;
		}
		else
		{
			relay.set(Relay.Value.kOff);
			isCompressorOn = false;
		}
		voltageReadout = aio.getVoltage();
		//System.out.println("Voltage: " + aio.getVoltage() + "v");
		//System.out.println("Pressure: " + ((250 * ( voltageReadout/5 )) - 25));
	}
	
	public boolean isCompressorLow(){
		return voltageReadout <= LOW_VOLTAGE;
	}
	
	public boolean isCompressorOn() {
		return isCompressorOn;
	}

}

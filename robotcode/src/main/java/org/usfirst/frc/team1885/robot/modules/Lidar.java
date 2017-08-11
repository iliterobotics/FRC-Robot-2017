package org.usfirst.frc.team1885.robot.modules;

import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class Lidar {

	private static final Port PORT_TYPE = Port.kMXP;
	private static final int ARDUINO_ADDRESS = 1;
	private ByteBuffer distanceBuffer;
	private int currentDistanceCentimeters;
	
	private I2C wire;
	
	public Lidar() {
		wire = new I2C(PORT_TYPE, ARDUINO_ADDRESS);
	}

	public int read() {
		wire.readOnly(distanceBuffer, 2);
		return distanceBuffer.getInt();
	}
	
	public int getCentimeters() {
		return read();
	}

}

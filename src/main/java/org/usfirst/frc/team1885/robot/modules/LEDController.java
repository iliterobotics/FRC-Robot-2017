package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class LEDController implements Module{
	
	private static final Port PORT_TYPE = Port.kOnboard;
	private static final int TARGET_ADDRESS = 1;
	
	private static final String TERM = ";";
	private String currentMessage;
	private String lastMessage;
	
	private I2C wire;

	@Override
	public void initialize() {
		wire = new I2C(PORT_TYPE, TARGET_ADDRESS);
	}
	
	public void sendMessage(String message){
		currentMessage = message + TERM;
	}
	
	@Override
	public void update() {
		if(!currentMessage.equals(lastMessage)){
			byte[] bytes = currentMessage.getBytes();
			wire.transaction(bytes, bytes.length, null, 0);
			lastMessage = currentMessage;
		}
	}
	
}

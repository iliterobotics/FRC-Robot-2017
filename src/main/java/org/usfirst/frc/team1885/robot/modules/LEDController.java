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
	
	public enum LEDColor {
		PURPLE(1, 0, 1), RED(1, 0, 0), BLUE(0, 0, 1);
		
		final int r, g, b;
		LEDColor(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}
	
	public enum LEDPattern {
		BLINK("blink"), PULSE("pulse"), SOLID("solid");
		
		final String command;
		LEDPattern( String command ) {
			this.command = command;
		}
	}
	
	public enum LEDMode {
		GEAR_NEEDED( LEDPattern.SOLID, LEDColor.PURPLE), LIFT_DETECTED(LEDPattern.PULSE,  LEDColor.RED), GEAR_PLACED(LEDPattern.BLINK, LEDColor.BLUE );

		final LEDPattern pattern;
		final LEDColor color;
		LEDMode( LEDPattern pattern, LEDColor color) {
			this.pattern = pattern;
			this.color = color;
		}
	}
	
	@Override
	public void initialize() {
		wire = new I2C(PORT_TYPE, TARGET_ADDRESS);
	}
	
	public void sendMessage(String message){
		currentMessage = message + TERM;
	}
	
	public void setMode(LEDMode mode)
	{
		sendMessage(mode.pattern.command + " " + mode.color.r + " " + mode.color.g + " " + mode.color.b);
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

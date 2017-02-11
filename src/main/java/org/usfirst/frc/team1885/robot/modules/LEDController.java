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
		PURPLE(255, 0, 255), RED(255, 0, 0), WHITE(255, 255, 255), GREEN(0, 255, 0), DEFAULT_COLOR(0, 0, 0);
		
		final int r, g, b;
		LEDColor(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}
	
	public enum LEDPattern {
		BLINK("blink"), RUN("run"), SOLID("solid"), CLEAR("clear");
		
		final String command;
		LEDPattern( String command ) {
			this.command = command;
		}
	}
	
	public enum FeederMessage {
		WAIT(LEDPattern.SOLID, LEDColor.WHITE), DROP_TO_GROUND(LEDPattern.SOLID, LEDColor.GREEN), DROP_TO_INTAKE(LEDPattern.SOLID, LEDColor.PURPLE);
		
		final LEDColor color;
		final LEDPattern pattern;
		private FeederMessage(LEDPattern pattern, LEDColor color) {
			this.pattern = pattern;
			this.color = color;
		}
	}
	
	public enum PilotMessage {
		WAIT(LEDPattern.SOLID, LEDColor.WHITE), GEAR_PLACED(LEDPattern.BLINK, LEDColor.PURPLE)
		final LEDColor color;
		final LEDPattern pattern;
		private PilotMessage(LEDPattern pattern, LEDColor color) {
			this.pattern = pattern;
			this.color = color;
		}
	}
	
	public enum DriverMessage {
		
		final LEDColor color;
		final LEDPattern pattern;
		private DriverMessage(LEDPattern pattern, LEDColor color) {
			this.pattern = pattern;
			this.color = color;
		}
	}
	
	public enum AutoMessage {
		
		final LEDColor color;
		final LEDPattern pattern;
		private AutoMessage(LEDPattern pattern, LEDColor color) {
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
	
	public void send(LEDMode mode)
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

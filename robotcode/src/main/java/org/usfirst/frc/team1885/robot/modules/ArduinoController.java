package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class ArduinoController implements Module{
	

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
		BLINK("blink", 500), RUN("run", 0), PULSE("pulse", 0), SOLID("solid", 0), CRAZY("crazy", 0), CLEAR("clear", 0);
		
		final String command;
		final int delay;
		LEDPattern( String command, int delay ) {
			this.command = command;
			this.delay = delay;
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
		WAIT(LEDPattern.SOLID, LEDColor.WHITE), GEAR_PLACED(LEDPattern.BLINK, LEDColor.PURPLE), LOOK_FOR_SIGNAL(LEDPattern.CRAZY, LEDColor.DEFAULT_COLOR);
		
		final LEDColor color;
		final LEDPattern pattern;
		private PilotMessage(LEDPattern pattern, LEDColor color) {
			this.pattern = pattern;
			this.color = color;
		}
	}
	
	public enum DriverMessage {
		CURRENT_LIMIT(LEDPattern.RUN, LEDColor.RED), HIGH_GEAR(LEDPattern.RUN, LEDColor.GREEN), LOW_AIR(LEDPattern.PULSE, LEDColor.RED), FLAP_OUT(LEDPattern.SOLID, LEDColor.PURPLE), READY_TO_PLACE(LEDPattern.PULSE, LEDColor.PURPLE), INTAKE_DOWN(LEDPattern.PULSE, LEDColor.GREEN); 
		
		final LEDColor color;
		final LEDPattern pattern;
		private DriverMessage(LEDPattern pattern, LEDColor color) {
			this.pattern = pattern;
			this.color = color;
		}
	}
	
//	public enum AutoMessage {
//		
//		final LEDColor color;
//		final LEDPattern pattern;
//		private AutoMessage(LEDPattern pattern, LEDColor color) {
//			this.pattern = pattern;
//			this.color = color;
//		}
//	}
	
	@Override
	public void initialize() {
		wire = new I2C(PORT_TYPE, TARGET_ADDRESS);
	}
	
	private void sendMessage(String message){
		currentMessage = message + TERM;
	}
	
	public void send(FeederMessage message)
	{
		sendMessage(message.pattern.command + TERM + message.color.r + TERM + message.color.g + TERM + message.color.b + TERM + message.pattern.delay + TERM);
	}
	
	public void send(DriverMessage message)
	{
		sendMessage(message.pattern.command + TERM + message.color.r + TERM + message.color.g + TERM + message.color.b + TERM + message.pattern.delay + TERM);	
	}
	
	public void send(PilotMessage message)
	{
		sendMessage(message.pattern.command + TERM + message.color.r + TERM + message.color.g + TERM + message.color.b + TERM + message.pattern.delay + TERM);
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

package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class ArduinoController implements Module{
	

	private static final Port PORT_TYPE = Port.kOnboard;
	private static final int TARGET_ADDRESS = 1;
	
	private static final String TERM = ";";
	private String currentMessage = "";
	private String lastMessage = " ";
	
	private I2C wire;
	
	public enum LEDColor {
		PURPLE(255, 0, 255), 
		RED(255, 0, 0), 
		LIGHT_BLUE(0, 100, 255),
		WHITE(255, 255, 255), 
		GREEN(0, 255, 0),
		YELLOW(255, 255, 0),
		DEFAULT_COLOR(0, 0, 0), 
		GREEN_HSV(84, 255, 255), 
		RED_HSV(0, 255, 255),
		YELLOW_HSV(20, 255, 255),
		PURPLE_HSV(212, 255, 255);
		
		final int r, g, b;
		LEDColor(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}
	
	public enum LEDPattern {
		BLINK("blink"), 
		RUN("run"), 
		PULSE("pulse"), 
		SOLID("solid"), 
		CRAZY("crazy"), 
		CLEAR("clear");
		
		final String command;
		LEDPattern( String command) {
			this.command = command;
		}
	}
	
	public enum FeederMessage {
		WAIT(LEDPattern.SOLID, LEDColor.WHITE, 0), 
		DROP_TO_GROUND(LEDPattern.SOLID, LEDColor.GREEN, 0), 
		DROP_TO_INTAKE(LEDPattern.SOLID, LEDColor.PURPLE, 0);
		
		final LEDColor color;
		final LEDPattern pattern;
		final int delay;
		private FeederMessage(LEDPattern pattern, LEDColor color, int delay) {
			this.pattern = pattern;
			this.color = color;
			this.delay = delay;
		}
	}
	
	public enum PilotMessage {
		WAIT(LEDPattern.SOLID, LEDColor.WHITE, 0), 
		GEAR_PLACED(LEDPattern.BLINK, LEDColor.PURPLE, 0), 
		LOOK_FOR_SIGNAL(LEDPattern.CRAZY, LEDColor.DEFAULT_COLOR, 200);
		
		final LEDColor color;
		final LEDPattern pattern;
		int delay;
		private PilotMessage(LEDPattern pattern, LEDColor color, int delay) {
			this.pattern = pattern;
			this.color = color;
			this.delay = delay;
		}
	}
	
	public enum DriverMessage {
		CURRENT_LIMIT(LEDPattern.BLINK, LEDColor.RED, 100), 
		HIGH_GEAR(LEDPattern.RUN, LEDColor.GREEN_HSV, 0), 
		LOW_AIR(LEDPattern.PULSE, LEDColor.LIGHT_BLUE, 0), 
		FLAP_OUT(LEDPattern.SOLID, LEDColor.PURPLE, 0),
		READY_TO_LIFT(LEDPattern.BLINK, LEDColor.PURPLE, 0), 
		INTAKE_DOWN(LEDPattern.PULSE, LEDColor.GREEN, 0),
		IDLE(LEDPattern.RUN, LEDColor.YELLOW_HSV, 0);
		
		final LEDColor color;
		final LEDPattern pattern;
		final int delay;
		private DriverMessage(LEDPattern pattern, LEDColor color, int delay) {
			this.pattern = pattern;
			this.color = color;
			this.delay = delay;
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
		currentMessage = message;
		DriverStation.reportError(currentMessage, false);
	}
	
	public void send(FeederMessage message)
	{
		sendMessage(message.pattern.command + TERM + message.color.r + TERM + message.color.g + TERM + message.color.b + TERM + message.delay + TERM);
	}
	
	public void send(DriverMessage message)
	{
		sendMessage(message.pattern.command + TERM + message.color.r + TERM + message.color.g + TERM + message.color.b + TERM + message.delay + TERM);	
	}
	
	public void send(PilotMessage message)
	{
		sendMessage(message.pattern.command + TERM + message.color.r + TERM + message.color.g + TERM + message.color.b + TERM + message.delay + TERM);
	}
	
	@Override
	public void update() {
		if(!currentMessage.equals(lastMessage)){
			byte[] bytes = currentMessage.getBytes();
			boolean result = wire.transaction(bytes, bytes.length, null, 0);
			lastMessage = currentMessage;
		}
	}
	
}
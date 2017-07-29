package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.modules.ArduinoController.DriverMessage;
import org.usfirst.frc.team1885.robot.modules.ArduinoController.FeederMessage;
import org.usfirst.frc.team1885.robot.modules.ArduinoController.PilotMessage;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;

public class LEDController implements Module{

	private ArduinoController arduinoController;
	private DriverControl driverControl;
	private PressureSensor pressureSensor;
	
	public LEDController(ArduinoController arduinoController, DriverControl driverControl, PressureSensor pressureSensor)
	{
		this.arduinoController = arduinoController;
		this.driverControl = driverControl;
		this.pressureSensor = pressureSensor;
	}
	
	@Override
	public void initialize() {
		
	}

	@Override
	public void update() {
		if(driverControl.isWarping()) arduinoController.send(DriverMessage.HIGH_GEAR);
		else if(pressureSensor.isCompressorLow()) arduinoController.send(DriverMessage.LOW_AIR);
		else arduinoController.send(DriverMessage.IDLE);
	}
	
	
	
	
	
}

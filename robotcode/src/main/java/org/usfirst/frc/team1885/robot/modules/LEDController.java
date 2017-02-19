package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.modules.ArduinoController.DriverMessage;
import org.usfirst.frc.team1885.robot.modules.ArduinoController.FeederMessage;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;

public class LEDController implements Module{

	private ArduinoController arduinoController;
	private DriveTrain driveTrain;
	private DriverControl driverControl;
	private PressureSensor pressureSensor;
	private Climber climber;
	private GearManipulator gearManipulator;
	
	public LEDController(ArduinoController arduinoController, DriveTrain driveTrain, DriverControl driverControl, PressureSensor pressureSensor, Climber climber, GearManipulator gearManipulator)
	{
		this.arduinoController = arduinoController;
		this.driveTrain = driveTrain;
		this.driverControl = driverControl;
		this.pressureSensor = pressureSensor;
		this.climber = climber;
		this.gearManipulator = gearManipulator;
	}
	
	@Override
	public void initialize() {
		
	}

	@Override
	public void update() {
		updateDriver();
	}
	
	private void updateDriver() {
		if(driverControl.isWarping()) arduinoController.send(DriverMessage.HIGH_GEAR);
		if(climber.getClimberState() == Climber.ClimberState.STALLED) arduinoController.send(DriverMessage.CURRENT_LIMIT);
		if(gearManipulator.isDown()) arduinoController.send(DriverMessage.INTAKE_DOWN);
		if(pressureSensor.isCompressorOn()) arduinoController.send(DriverMessage.LOW_AIR);
	}
	
	
	
}

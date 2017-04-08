package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.modules.ArduinoController.DriverMessage;
import org.usfirst.frc.team1885.robot.modules.ArduinoController.FeederMessage;
import org.usfirst.frc.team1885.robot.modules.ArduinoController.PilotMessage;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;

public class LEDController implements Module{

	private ArduinoController arduinoController;
	private DriveTrain driveTrain;
	private DriverControl driverControl;
	private PressureSensor pressureSensor;
	private BeamSensor beamSensor;
	private Climber climber;
	private GearManipulator gearManipulator;
	
	private boolean kicked;
	
	public LEDController(ArduinoController arduinoController, DriveTrain driveTrain, DriverControl driverControl, PressureSensor pressureSensor, BeamSensor beamSensor, Climber climber, GearManipulator gearManipulator)
	{
		this.arduinoController = arduinoController;
		this.driveTrain = driveTrain;
		this.driverControl = driverControl;
		this.pressureSensor = pressureSensor;
		this.beamSensor = beamSensor;
		this.climber = climber;
		this.gearManipulator = gearManipulator;
	}
	
	@Override
	public void initialize() {
		
	}

	@Override
	public void update() {
		
		if(gearManipulator.isKicked()) kicked = true;
		else if(!(gearManipulator.isLong() && gearManipulator.isShort())) kicked = false;
		
		if(driverControl.isWarping()) arduinoController.send(DriverMessage.HIGH_GEAR);
		else if(climber.getClimberState() == Climber.ClimberState.STALLED) arduinoController.send(DriverMessage.CURRENT_LIMIT);
		else if(beamSensor.isBroken()) arduinoController.send(DriverMessage.BEAM_BROKEN);
		else if(gearManipulator.isStalled()) arduinoController.send(DriverMessage.CURRENT_LIMIT);
		else if(kicked) arduinoController.send(DriverMessage.READY_TO_LIFT);
		else if(gearManipulator.isLong() && !gearManipulator.isShort()) arduinoController.send(DriverMessage.FLAP_OUT);
		else if(driverControl.isWait()) arduinoController.send(FeederMessage.WAIT);
		else if(gearManipulator.isDown()) arduinoController.send(DriverMessage.INTAKE_DOWN);
		else if(driverControl.isLook()) arduinoController.send(PilotMessage.LOOK_FOR_SIGNAL);		
		//else if(pressureSensor.isCompressorLow()) arduinoController.send(DriverMessage.LOW_AIR);
		else arduinoController.send(DriverMessage.IDLE);
	}
	
	
	
	
	
}

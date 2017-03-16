package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.Robot;
import org.usfirst.frc.team1885.robot.modules.ArduinoController.DriverMessage;
import org.usfirst.frc.team1885.robot.modules.ArduinoController.FeederMessage;
import org.usfirst.frc.team1885.robot.modules.ArduinoController.PilotMessage;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;

import edu.wpi.first.wpilibj.Timer;

public class LEDUpdater implements Module{

	private Robot robot;
	private ArduinoController arduinoController;
	private DriveTrain driveTrain;
	private DriverControl driverControl;
	private PressureSensor pressureSensor;
	private Climber climber;
	private GearManipulator gearManipulator;
	
	private boolean kicked;
	private boolean runAutoSignal;
	private double timeSinceCommandFinished;
	
	private static final int AUTO_FINISHED_SIGNAL_TIME = 2000;
	
	public LEDUpdater(Robot robot, ArduinoController arduinoController, DriveTrain driveTrain, DriverControl driverControl, PressureSensor pressureSensor, Climber climber, GearManipulator gearManipulator)
	{
		this.robot = robot;
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
	
		if(robot.isAutonomous() && robot.isCurrentAutoFinished()) {
			runAutoSignal = true;
			timeSinceCommandFinished = Timer.getFPGATimestamp();
		}
		
		if((Timer.getFPGATimestamp() <= timeSinceCommandFinished + AUTO_FINISHED_SIGNAL_TIME) && runAutoSignal) {
			arduinoController.send(DriverMessage.AUTO_COMMAND_FINISHED);
		}
		
		if(gearManipulator.isKicked()) kicked = true;
		else if(!(gearManipulator.isLong() && gearManipulator.isShort())) kicked = false;
		
		if(driverControl.isWarping()) arduinoController.send(DriverMessage.HIGH_GEAR);
		else if(climber.getClimberState() == Climber.ClimberState.STALLED) arduinoController.send(DriverMessage.CURRENT_LIMIT);
		else if(gearManipulator.isStalled()) arduinoController.send(DriverMessage.CURRENT_LIMIT);
		else if(kicked) arduinoController.send(DriverMessage.READY_TO_LIFT);
		else if(gearManipulator.isLong() && !gearManipulator.isShort()) arduinoController.send(DriverMessage.FLAP_OUT);
		else if(gearManipulator.isDown()) arduinoController.send(DriverMessage.INTAKE_DOWN);
		else if(driverControl.isLook()) arduinoController.send(PilotMessage.LOOK_FOR_SIGNAL);
		else if(driverControl.isWait()) arduinoController.send(FeederMessage.WAIT);
		else if(pressureSensor.isCompressorOn()) arduinoController.send(DriverMessage.LOW_AIR);
		else arduinoController.send(DriverMessage.IDLE);
	}
	
	
	
	
	
}

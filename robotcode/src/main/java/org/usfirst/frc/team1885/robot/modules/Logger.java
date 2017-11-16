package org.usfirst.frc.team1885.robot.modules;
import com.flybotix.hfr.codex.Codex;
import com.flybotix.hfr.codex.CodexOf;
import com.flybotix.hfr.codex.CodexSender;

public class Logger implements Module {
	
	private Codex<Double, RobotData> codex;
	private CodexSender<Double, RobotData> sender;
	
	private NavX navX;
	private ArduinoController arduinoController;
	private BeamSensor beamSensor;
	private DriveTrain driveTrain;
	private GearManipulator gearManipulator;
	private PressureSensor pressureSensor;

	

	public Logger(NavX navX, ArduinoController arduinoController, BeamSensor beamSensor, DriveTrain driveTrain,
			GearManipulator gearManipulator, PressureSensor pressurenSensor,  Codex<Double, RobotData> codex, CodexSender<Double, RobotData> sender) {
		this.navX = navX;
		this.arduinoController = arduinoController;
		this.beamSensor = beamSensor;
		this.driveTrain = driveTrain;
		this.gearManipulator = gearManipulator;
		this.pressureSensor = pressurenSensor;
		this.codex = codex;
		this.sender = sender;
	}

	public enum RobotData implements CodexOf<Double>{
	  gyroAngle,
	  //voltageLeftDriveTrain,
	  //voltageRightDriveTrain,
	  leftEncoderPos,
	  rightEncoderPos,
	  leftVelocity,
	  rightVelocity
	}
	
	@Override
	public void initialize() {
		codex.reset();
		
	}

	@Override
	public void update() {
		updateCodex();
		sender.send(codex);
		
	}
	
	public void updateCodex()
	{
		codex.reset();
		codex.set(RobotData.gyroAngle, navX.getAngle());
		codex.set(RobotData.leftEncoderPos, driveTrain.getLeftPosition());
		codex.set(RobotData.rightEncoderPos, driveTrain.getRightPosition());
		codex.set(RobotData.leftVelocity, driveTrain.getLeftEncoderVelocity());
		codex.set(RobotData.rightVelocity, driveTrain.getRightEncoderVelocity());
	}
	

}

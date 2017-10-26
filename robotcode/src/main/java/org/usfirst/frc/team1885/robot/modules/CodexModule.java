package org.usfirst.frc.team1885.robot.modules;
import org.usfirst.frc.team1885.robot.Codex;
import org.usfirst.frc.team1885.robot.CodexSender;
import org.usfirst.frc.team1885.robot.RobotData;

import com.flybotix.hfr.codex.CodexOf;

public class CodexModule implements Module {
	private Codex codex;
	private CodexSender sender;
	private NavX navX;
	private ArduinoController arduinoController;
	private BeamSensor beamSensor;
	private DriveTrain driveTrain;
	private GearManipulator gearManipulator;
	private PressureSensor pressureSensor;

	

	public CodexModule(NavX navX, ArduinoController arduinoController, BeamSensor beamSensor, DriveTrain driveTrain,
			GearManipulator gearManipulator, PressureSensor pressurenSensor,  Codex codex, CodexSender sender) {
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
		data.reset();
		
	}

	@Override
	public void update() {
		updateCodex();
		sender.send(data);
		
	}
	
	public void updateCodex()
	{
		data.reset();
		data.put(RobotData.gyroAngle, navX.getAngle());
		data.put(RobotData.leftEncoderPos, driveTrain.getLeftPosition());
		data.put(RobotData.rightEncoderPos, driveTrain.getRightPosition());
		data.put(RobotData.leftVelocity, driveTrain.getLeftEncoderVelocity());
		data.put(RobotData.rightVelocity, driveTrain.getRightEncoderVelocity());
	}
	

}

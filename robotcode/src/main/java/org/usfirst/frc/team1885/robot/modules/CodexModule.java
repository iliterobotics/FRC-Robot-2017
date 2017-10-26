package org.usfirst.frc.team1885.robot.modules;
import org.usfirst.frc.team1885.robot.Codex;
import org.usfirst.frc.team1885.robot.CodexSender;
import org.usfirst.frc.team1885.robot.RobotData;

import com.flybotix.hfr.codex.CodexOf;

public class CodexModule implements Module {
	private Codex codex;
	private NavX navX;
	private ArduinoController arduinoController;
	private BeamSensor beamSensor;
	private DriveTrain driveTrain;
	private GearManipulator gearManipulator;
	private PressureSensor pressurenSensor;
	private UltraSonic ultraSonic;
	

	public CodexModule(NavX navX, ArduinoController arduinoController, BeamSensor beamSensor, DriveTrain driveTrain,
			GearManipulator gearManipulator, PressureSensor pressurenSensor, UltraSonic ultraSonic, Codex codex) {
		this.navX = navX;
		this.arduinoController = arduinoController;
		this.beamSensor = beamSensor;
		this.driveTrain = driveTrain;
		this.gearManipulator = gearManipulator;
		this.pressurenSensor = pressurenSensor;
		this.ultraSonic = ultraSonic;
		this.codex = codex;
	}

	public enum RobotData implements CodexOf<Double>{
	  gyroAngle,
	  voltageLeftDriveTrain,
	  voltageRightDriveTrain,
	  leftEncoderPos,
	  rightEncoderPos,
	  leftVelocity,
	  rightVelocity
	}
	
	@Override
	public void initialize() {
		CodexSender<Double, RobotData> sender = new CodexSender<>(RobotData.class, true);
		sender.initConnection(EProtocol.UDP, 8500, 8501, "localhost");
		
	}

	@Override
	public void update() {
		Codex<Double, RobotData> data = Codex.of.thisEnum(RobotData.class);

		// Reset the codex at the beginning of each cycle.  This effectively sets each value to 'null'.  Fill out data throughout each cycle.
		data.reset(); // beginning of the cycle
		
		data.put(RobotData., -23.3d);
		data.put(RobotData.gyro, mxp.getGyroRelative());

		// Use the data throughout the robot cycle after it's gathered
		double degrees = data.get(RobotData.gyro);

		// Send the data back to the laptop at the end of each cycle
		sender.send(data);
		
	}
	
	public void updateCodex()
	{
		
	}
	

}

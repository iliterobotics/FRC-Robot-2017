package org.usfirst.frc.team1885.robot.modules;
import com.flybotix.hfr.codex.CodexOf;

public class Codex implements Module {
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
	}
	
	

}

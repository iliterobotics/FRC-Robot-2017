package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;

import edu.wpi.first.wpilibj.Joystick.AxisType;

public class DriverControlArcadeOneStick extends DriverControl {
	
	public DriverControlArcadeOneStick(DriveTrain driveTrain) {
		super(driveTrain);
	}

	public void update() {
		double throttle = getController(ControllerType.LEFT_STICK).getAxis(AxisType.kY);
		double turn = getController(ControllerType.LEFT_STICK).getAxis(AxisType.kX);
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		setSpeeds(leftInput, rightInput);
	}

}

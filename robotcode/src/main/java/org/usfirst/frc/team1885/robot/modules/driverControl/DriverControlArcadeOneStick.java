package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.EJoystickAxis;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class DriverControlArcadeOneStick extends DriverControl {
	
	public DriverControlArcadeOneStick(DriveTrain driveTrain) {
		super(driveTrain, new DefaultJoystickFactory());		
	}
	
	public DriverControlArcadeOneStick(DriveTrain driveTrain, IJoystickFactory joystickFact) {
		super(driveTrain, joystickFact);
	}

	public void update() {
		double throttle = getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kY);
		double turn = getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kX);
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		setSpeeds(leftInput, rightInput);
	}

}

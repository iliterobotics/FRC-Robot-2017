package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.interfaces.controller.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.interfaces.controller.EJoystickAxis;
import org.usfirst.frc.team1885.robot.interfaces.controller.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;

public class DriverControlTank extends DriverControl{
	
	private static final double SCALING_EXP = 2;
	
	public DriverControlTank(DriveTrain driveTrain) {
		this(driveTrain, new DefaultJoystickFactory());
	}

	public DriverControlTank(DriveTrain driveTrain, IJoystickFactory factory) {
		super(driveTrain, factory);
	}

	public void update() {
		double leftInput = getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kY);
		double rightInput = getController(ControllerType.RIGHT_STICK).getAxis(EJoystickAxis.kY);

		DriverStation.reportError(String.format("oL:%f oR:%f", leftInput, rightInput), false);
		
		//See if sticks are close enough together to be considered the same
		if(Math.abs(leftInput - rightInput) < DEADZONE ){
			leftInput = rightInput = (leftInput + rightInput) / 2;
		}
		
		//Exponential ramping
		int leftScaler = leftInput > 0?1:-1;
		leftInput = Math.pow(leftInput, SCALING_EXP) * leftScaler;
		int rightScaler = leftInput > 0?1:-1;
		leftInput = Math.pow(leftInput, SCALING_EXP) * rightScaler;
		
		DriverStation.reportError(String.format("fL:%f fR:%f", leftInput, rightInput), false);

		setSpeeds(leftInput, rightInput);
	}

}

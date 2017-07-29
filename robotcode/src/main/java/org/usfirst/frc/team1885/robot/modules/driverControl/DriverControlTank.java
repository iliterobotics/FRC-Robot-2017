package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class DriverControlTank extends DriverControl{
	
	private static final double SCALING_EXP = 2;
	private double reducer;
	private DriverStation driveStation;
	
	public DriverControlTank(DriveTrain driveTrain, NavX navx) {
		super(driveTrain, navx);
		this.driveStation = driveStation;
	}
	
	public double getReducer(double value) {
		return (value + 1.0) / 2.0;
	}

	@Override
	public void updateDriveTrain() {
		reducer = getReducer(getController(ControllerType.LEFT_STICK).getAxis(Joystick.AxisType.kZ));
		double leftInput = getController(ControllerType.LEFT_STICK).getAxis(Joystick.AxisType.kY);
		double rightInput = getController(ControllerType.RIGHT_STICK).getAxis(Joystick.AxisType.kY);

		driveStation.reportError(String.format("oL:%f oR:%f", leftInput, rightInput), false);
		
		//See if sticks are close enough together to be considered the same
		if(Math.abs(leftInput - rightInput) < JOYSTICK_DEADZONE ){
			leftInput = rightInput = (leftInput + rightInput) / 2;
		}
		
		//Exponential ramping
		int leftScaler = leftInput > 0?1:-1;
		leftInput = Math.pow(leftInput, SCALING_EXP) * leftScaler * reducer;
		int rightScaler = rightInput > 0?1:-1;
		rightInput = Math.pow(rightInput, SCALING_EXP) * rightScaler * reducer;
		
		driveStation.reportError(String.format("fL:%f fR:%f", leftInput, rightInput), false);

		setSpeeds(leftInput, rightInput);
	}

}

package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultDriverStation;
import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.EJoystickAxis;
import org.usfirst.frc.team1885.robot.common.interfaces.IDriverStation;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class DriverControlTank extends DriverControl{
	
	private static final double SCALING_EXP = 2;
	private double reducer;
	private IDriverStation driveStation;
	
	public DriverControlTank(DriveTrain driveTrain, GearManipulator gearManipulator, Climber climber, NavX navx) {
		this(driveTrain, gearManipulator, climber, navx, new DefaultJoystickFactory(), new DefaultDriverStation());
	}

	public DriverControlTank(DriveTrain driveTrain, GearManipulator gearManipulator, Climber climber, NavX navx, IJoystickFactory factory, IDriverStation driveStation) {
		super(driveTrain, gearManipulator, climber, navx, factory);
		this.driveStation = driveStation;
	}
	
	public double getReducer(double value) {
		return (value + 1.0) / 2.0;
	}

	@Override
	public void updateDriveTrain() {
		reducer = getReducer(getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kZ));
		double leftInput = getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kY);
		double rightInput = getController(ControllerType.RIGHT_STICK).getAxis(EJoystickAxis.kY);

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

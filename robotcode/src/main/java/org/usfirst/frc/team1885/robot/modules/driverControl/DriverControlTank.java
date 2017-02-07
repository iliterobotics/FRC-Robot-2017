package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultDriverStation;
import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.EJoystickAxis;
import org.usfirst.frc.team1885.robot.common.interfaces.IDriverStation;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;

public class DriverControlTank extends DriverControl{
	
	private static final double SCALING_EXP = 2;
	private double reducer;
	private IDriverStation driveStation;
	
	public DriverControlTank(DriveTrain driveTrain) {
		this(driveTrain, new DefaultJoystickFactory(), new DefaultDriverStation());
	}

	public DriverControlTank(DriveTrain driveTrain, IJoystickFactory factory, IDriverStation driveStation) {
		super(driveTrain, factory);
		this.driveStation = driveStation;
	}

	public void update() {
		reducer = getReducer(getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kZ));
		double leftInput = getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kY);
		double rightInput = getController(ControllerType.RIGHT_STICK).getAxis(EJoystickAxis.kY);

		driveStation.reportError(String.format("oL:%f oR:%f", leftInput, rightInput), false);
		
		//See if sticks are close enough together to be considered the same
		if(Math.abs(leftInput - rightInput) < DEADZONE ){
			leftInput = rightInput = (leftInput + rightInput) / 2;
		}
		
		//Exponential ramping
		int leftScaler = leftInput > 0?1:-1;
		leftInput = Math.pow(leftInput, SCALING_EXP) * leftScaler * reducer;
		int rightScaler = leftInput > 0?1:-1;
		leftInput = Math.pow(leftInput, SCALING_EXP) * rightScaler * reducer;
		
		driveStation.reportError(String.format("fL:%f fR:%f", leftInput, rightInput), false);

		setSpeeds(leftInput, rightInput);
	}
	
	public double getReducer(double value) {
		return (value + 1.0) / 2.0;
	}

}

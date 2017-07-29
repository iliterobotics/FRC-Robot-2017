package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class DriverControlTankController extends DriverControl{

	private static final double SCALING_EXP = 2;

	public DriverControlTankController(DriveTrain driveTrain, NavX navx) {
		super(driveTrain, navx);
	}


	@Override
	public void updateDriveTrain() {
		double leftInput = getController(ControllerType.CONTROLLER).getRawAxis(1);
		double rightInput = getController(ControllerType.CONTROLLER).getRawAxis(5);
		
		int leftScaler = leftInput > 0?1:-1;
		leftInput = Math.pow(leftInput, SCALING_EXP) * leftScaler;
		int rightScaler = leftInput > 0?1:-1;
		leftInput = Math.pow(leftInput, SCALING_EXP) * rightScaler;

		setSpeeds(leftInput, rightInput);		
	}

}


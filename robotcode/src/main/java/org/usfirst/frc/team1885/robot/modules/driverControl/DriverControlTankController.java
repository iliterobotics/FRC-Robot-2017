package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.Joystick;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class DriverControlTankController extends DriverControl{

	private static final double SCALING_EXP = 2;

	public DriverControlTankController(DriveTrain driveTrain, Climber climber, NavX navx, GearManipulator gearManipulator) {
		this(driveTrain, gearManipulator, climber, navx, new Joystick());
	}
	
	public DriverControlTankController(DriveTrain driveTrain, GearManipulator gearManipulator, Climber climber, NavX navx, IJoystickFactory joystickFact) {
		super(driveTrain, gearManipulator, climber, navx, joystickFact);
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


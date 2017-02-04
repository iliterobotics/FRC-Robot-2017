package org.usfirst.frc.team1885.robot.modules.driverControl;

import org.usfirst.frc.team1885.robot.common.impl.DefaultJoystickFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.EJoystickAxis;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;

public class DriverControlArcadeOneStick extends DriverControl {
	
	public DriverControlArcadeOneStick(DriveTrain driveTrain, GearManipulator gearManipulator) {
		super(driveTrain, gearManipulator, new DefaultJoystickFactory());		
	}
	
	public DriverControlArcadeOneStick(DriveTrain driveTrain, GearManipulator gearManipulator, IJoystickFactory joystickFact) {
		super(driveTrain, gearManipulator, joystickFact);
	}

	public void update() {
		updateDriveTrain();
	}

	@Override
	public void updateDriveTrain() {
		double throttle = getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kY);
		double turn = getController(ControllerType.LEFT_STICK).getAxis(EJoystickAxis.kX);
		
		double leftInput, rightInput;
		
		leftInput =  throttle - turn;
		rightInput = throttle + turn;

		setSpeeds(leftInput, rightInput);
	}

}

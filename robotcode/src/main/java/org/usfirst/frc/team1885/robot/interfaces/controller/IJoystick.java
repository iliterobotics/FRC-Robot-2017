package org.usfirst.frc.team1885.robot.interfaces.controller;

import edu.wpi.first.wpilibj.Joystick.AxisType;

public interface IJoystick {

	double getRawAxis(int gamepadLeftY);

	boolean getRawButton(int i);

	double getAxis(AxisType ky);

}

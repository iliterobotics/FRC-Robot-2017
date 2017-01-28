package org.usfirst.frc.team1885.robot.common.interfaces;

public interface IJoystick {

	double getRawAxis(int gamepadLeftY);

	boolean getRawButton(int i);

	double getAxis(EJoystickAxis ky);

}

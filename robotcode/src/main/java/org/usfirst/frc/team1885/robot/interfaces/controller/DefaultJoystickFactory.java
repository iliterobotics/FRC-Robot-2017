package org.usfirst.frc.team1885.robot.interfaces.controller;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;

public class DefaultJoystickFactory implements IJoystickFactory {

	@Override
	public IJoystick createJoystick(int id) {
		return new DefaultJoystick(new Joystick(id));
	}
	
	
	private class DefaultJoystick implements IJoystick { 
		
		private Joystick realJoystick;

		public DefaultJoystick(Joystick realJoystick) {
			this.realJoystick = realJoystick;
		}

		@Override
		public double getRawAxis(int gamepadLeftY) {
			return this.realJoystick.getRawAxis(gamepadLeftY);
		}

		@Override
		public boolean getRawButton(int i) {
			return this.realJoystick.getRawButton(i);
		}

		@Override
		public double getAxis(AxisType ky) {
			return this.realJoystick.getAxis(ky);
		}
		
	}

}

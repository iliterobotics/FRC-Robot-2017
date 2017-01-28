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
		public double getAxis(EJoystickAxis ky) {
			AxisType axisType = null;
			double returnVal = Double.NaN;
			
			switch(ky) {
			case kX:
				axisType = AxisType.kX;
				break;
			case kY:
				axisType = AxisType.kY;
				break;
			case kZ:
				axisType = AxisType.kZ;
				break;
			case kTwist:
				axisType = AxisType.kTwist;
				break;
			case kThrottle:
				axisType = AxisType.kThrottle;
				break;
			case kNumAxis:
				axisType = AxisType.kNumAxis;
				break;
			}
			
			if(axisType != null) { 
				returnVal = this.realJoystick.getAxis(axisType);
			}
			
			return returnVal;
		}
		
	}

}

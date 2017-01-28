package org.usfirst.frc.team1885.robot.common.impl.converters;

import org.usfirst.frc.team1885.robot.common.impl.ETalonControlMode;

import com.ctre.CANTalon.TalonControlMode;
import com.google.common.base.Converter;

public class TalonControlModeConverter extends Converter<ETalonControlMode, TalonControlMode>{

	@Override
	protected TalonControlMode doForward(ETalonControlMode a) {
		switch(a) {
		case Current:
			return TalonControlMode.Current;
		case Disabled:
			return TalonControlMode.Disabled;
		case Follower:
			return TalonControlMode.Follower;
		case MotionMagic:
			return TalonControlMode.MotionMagic;
		case MotionProfile:
			return TalonControlMode.MotionProfile;
		case PercentVbus:
			return TalonControlMode.PercentVbus;
		case Position:
			return TalonControlMode.Position;
		case Speed:
			return TalonControlMode.Speed;
		case Voltage:
			return TalonControlMode.Voltage;
		}
		
		return null;
	}

	@Override
	protected ETalonControlMode doBackward(TalonControlMode b) {
		switch(b) {
		case Current:
			return ETalonControlMode.Current;
		case Disabled:
			return ETalonControlMode.Disabled;
		case Follower:
			return ETalonControlMode.Follower;
		case MotionMagic:
			return ETalonControlMode.MotionMagic;
		case MotionProfile:
			return ETalonControlMode.MotionProfile;
		case PercentVbus:
			return ETalonControlMode.PercentVbus;
		case Position:
			return ETalonControlMode.Position;
		case Speed:
			return ETalonControlMode.Speed;
		case Voltage:
			return ETalonControlMode.Voltage;
		}
		return null;
	}
	
	public static TalonControlModeConverter getConverter() { 
		return INSTANCE_HOLDER.converter;
	}
	private static class INSTANCE_HOLDER { 
		public static TalonControlModeConverter converter = new TalonControlModeConverter();
	}
	
	private TalonControlModeConverter() { 
		
	}

}

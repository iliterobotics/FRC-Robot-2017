package org.usfirst.frc.team1885.robot.common.impl.converters;

import org.usfirst.frc.team1885.robot.common.impl.EFeedbackDevice;

import com.ctre.CANTalon.FeedbackDevice;
import com.google.common.base.Converter;

public class FeedbackConverter extends Converter<FeedbackDevice, EFeedbackDevice>{

	@Override
	protected EFeedbackDevice doForward(FeedbackDevice a) {
		
		switch(a) {
		case AnalogEncoder:
			return EFeedbackDevice.AnalogEncoder;
		case CtreMagEncoder_Absolute:
			return EFeedbackDevice.CtreMagEncoder_Absolute;
		case EncFalling:
			return EFeedbackDevice.EncFalling;
		case PulseWidth:
			return EFeedbackDevice.PulseWidth;
		case AnalogPot:
			return EFeedbackDevice.AnalogPot;
		case EncRising:
			return EFeedbackDevice.EncRising;
		case QuadEncoder:
			return EFeedbackDevice.QuadEncoder;
			
		}
		return null;
	}

	@Override
	protected FeedbackDevice doBackward(EFeedbackDevice b) {

		switch(b) {
		case AnalogEncoder:
			return FeedbackDevice.AnalogEncoder;
		case CtreMagEncoder_Absolute:
			return FeedbackDevice.CtreMagEncoder_Absolute;
		case EncFalling:
			return FeedbackDevice.EncFalling;
		case PulseWidth:
			return FeedbackDevice.PulseWidth;
		case AnalogPot:
			return FeedbackDevice.AnalogPot;
		case EncRising:
			return FeedbackDevice.EncRising;
		case QuadEncoder:
			return FeedbackDevice.QuadEncoder;
			
		}
		return null;
	}
	
	public static FeedbackConverter getConverter() { 
		return INSTANCE_HOLDER.instance;
	}

	private static class INSTANCE_HOLDER { 
		public static FeedbackConverter instance = new FeedbackConverter();
	}
	
	private FeedbackConverter() { 
		
	}
}

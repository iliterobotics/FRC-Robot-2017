package org.usfirst.frc.team1885.robot.common.impl.converters;

import org.usfirst.frc.team1885.robot.common.interfaces.ESerialPort;

import com.google.common.base.Converter;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class SerialPortConverter extends Converter<ESerialPort, SerialPort.Port>{

	@Override
	protected Port doForward(ESerialPort a) {
		switch(a){
		case kMXP:
			return SerialPort.Port.kMXP;
		case kOnboard:
			return SerialPort.Port.kOnboard;
		case kUSB:
			return SerialPort.Port.kUSB;
		case kUSB1:
			return SerialPort.Port.kUSB1;
		case kUSB2:
			return SerialPort.Port.kUSB2;
		}
		return null;
	}

	@Override
	protected ESerialPort doBackward(Port b) {
		switch(b){
		case kMXP:
			return ESerialPort.kMXP;
		case kOnboard:
			return ESerialPort.kOnboard;
		case kUSB:
			return ESerialPort.kUSB;
		case kUSB1:
			return ESerialPort.kUSB1;
		case kUSB2:
			return ESerialPort.kUSB2;
		}
		return null;
	}
	
	public static SerialPortConverter getConverter() { 
		return INSTANCE_HOLDER.converter;
	}
	private static class INSTANCE_HOLDER { 
		public static SerialPortConverter converter = new SerialPortConverter();
	}
	
	private SerialPortConverter() {
		
	}

}

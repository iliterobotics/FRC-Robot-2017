package org.usfirst.frc.team1885.robot.common.impl;

import org.usfirst.frc.team1885.robot.common.impl.converters.SerialPortConverter;
import org.usfirst.frc.team1885.robot.common.interfaces.ESerialPort;
import org.usfirst.frc.team1885.robot.common.interfaces.IAHRS;
import org.usfirst.frc.team1885.robot.common.interfaces.IAHRSFactory;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDSourceType;

public class DefaultAHRSFactory implements IAHRSFactory{

	public IAHRS getAHRS(ESerialPort port) {
		return new DefaultAHRS(new AHRS(SerialPortConverter.getConverter().convert(port)));
	}
	
	private class DefaultAHRS implements IAHRS{
		
		private final AHRS ahrs;
		
		public DefaultAHRS(AHRS ahrs){
			this.ahrs = ahrs;
		}

		@Override
		public double getYaw() {
			return ahrs.getYaw();
		}

		@Override
		public double getDisplacementX() {
			return ahrs.getDisplacementX();
		}

		@Override
		public double getDisplacementY() {
			return ahrs.getDisplacementY();
		}

		@Override
		public double getDisplacementZ() {
			return ahrs.getDisplacementZ();
		}

		@Override
		public void zeroYaw() {
			ahrs.zeroYaw();
		}

		@Override
		public void resetDisplacement() {
			ahrs.resetDisplacement();
		}

		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
			ahrs.setPIDSourceType(pidSource);
		}

		@Override
		public PIDSourceType getPIDSourceType() {
			return getPIDSourceType();
		}

		@Override
		public double pidGet() {
			return ahrs.pidGet();
		}
		
	}
	
	
	
}

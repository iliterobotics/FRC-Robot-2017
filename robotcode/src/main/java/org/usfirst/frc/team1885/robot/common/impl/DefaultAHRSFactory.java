package org.usfirst.frc.team1885.robot.common.impl;

import org.usfirst.frc.team1885.robot.common.interfaces.ESerialPort;
import org.usfirst.frc.team1885.robot.common.interfaces.IAHRS;
import org.usfirst.frc.team1885.robot.common.interfaces.IAHRSFactory;

import com.kauailabs.navx.frc.AHRS;

public class DefaultAHRSFactory implements IAHRSFactory{

	public IAHRS getAHRS(ESerialPort port) {
		return new DefaultAHRS(new AHRS(port.value));
	}
	
	private class DefaultAHRS implements IAHRS{
		
		private final AHRS ahrs;
		
		public DefaultAHRS(AHRS ahrs){
			this.ahrs = ahrs;
		}
		
	}
	
	
	
}

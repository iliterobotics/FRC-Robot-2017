package org.usfirst.frc.team1885.robot.common.interfaces;

public enum ESerialPort {
    kOnboard(0), kMXP(1), kUSB(2), kUSB1(2), kUSB2(3);
	
	public final int value;
	
	ESerialPort(int value){
		this.value = value;
	}
}

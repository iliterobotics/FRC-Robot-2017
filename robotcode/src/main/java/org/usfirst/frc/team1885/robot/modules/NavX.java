package org.usfirst.frc.team1885.robot.modules;

import org.usfirst.frc.team1885.robot.common.impl.DefaultAHRSFactory;
import org.usfirst.frc.team1885.robot.common.interfaces.ESerialPort;
import org.usfirst.frc.team1885.robot.common.interfaces.IAHRS;
import org.usfirst.frc.team1885.robot.common.interfaces.IAHRSFactory;

public class NavX{
	
	private static final ESerialPort DEFAULT_PORT = ESerialPort.kMXP;
	private final double initialYaw;
	private final IAHRS iahrs;
	
	public NavX(){
		this(new DefaultAHRSFactory());
	}
	
	public NavX(IAHRSFactory factory) {
		this.iahrs = factory.getAHRS(DEFAULT_PORT);
		initialYaw = iahrs.getYaw();
	}

	public double getInitialYaw() {
		return initialYaw;
	}

	public double getYaw() {
		return iahrs.getYaw();
	}

	public double getDisplacementX() {
		return iahrs.getDisplacementX();
	}
	
	public double getDisplacementY() {
		return iahrs.getDisplacementY();
	}
	
	public double getDisplacementZ() {
		return iahrs.getDisplacementZ();
	}

	public void zeroYaw() {
		iahrs.zeroYaw();
	}

	public void resetDisplacement() {
		iahrs.resetDisplacement();
	}

}

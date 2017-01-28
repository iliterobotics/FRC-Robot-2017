package org.usfirst.frc.team1885.robot.modules;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort;

public class NavX extends AHRS{
	private final double initialYaw;
	
	public NavX(SerialPort.Port kmxp) {
		super(kmxp);
		initialYaw = getYaw();
	}

	public double getInitialYaw() {
		return initialYaw;
	}
}

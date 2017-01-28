package org.usfirst.frc.team1885.robot.common.impl;

import org.usfirst.frc.team1885.robot.common.interfaces.IDriverStation;

import edu.wpi.first.wpilibj.DriverStation;

public class DefaultDriverStation implements IDriverStation {

	@Override
	public void reportError(String format, boolean b) {
		DriverStation.reportError(format, b);
	}

}

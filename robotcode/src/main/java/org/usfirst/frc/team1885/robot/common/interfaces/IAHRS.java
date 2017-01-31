package org.usfirst.frc.team1885.robot.common.interfaces;

public interface IAHRS{

	double getYaw();

	double getDisplacementX();

	double getDisplacementY();

	double getDisplacementZ();

	void zeroYaw();

	void resetDisplacement();

}

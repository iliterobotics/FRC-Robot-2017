package org.usfirst.frc.team1885.robot.common.interfaces;

import edu.wpi.first.wpilibj.PIDSource;

public interface IAHRS extends PIDSource{

	double getYaw();

	double getDisplacementX();

	double getDisplacementY();

	double getDisplacementZ();

	void zeroYaw();

	void resetDisplacement();

	boolean isCalibrating();

}

package org.usfirst.frc.team1885.robot.common.interfaces;

public interface IAHRS{

	double getYaw();

	double getDisplacementX();

	double getDisplacementY();

	double getDisplacementZ();
	
	float getWorldLinearAccelZ();

	void zeroYaw();

	void resetDisplacement();
	
	boolean isCalibrating();
	
	double getAngle();

}

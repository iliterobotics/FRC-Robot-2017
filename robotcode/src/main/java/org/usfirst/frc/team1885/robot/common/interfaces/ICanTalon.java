package org.usfirst.frc.team1885.robot.common.interfaces;

import org.usfirst.frc.team1885.robot.common.impl.EFeedbackDevice;
import org.usfirst.frc.team1885.robot.common.impl.ETalonControlMode;

import com.ctre.CANTalon.FeedbackDevice;

public interface ICanTalon {

	void setEncPosition(int i);

	void setP(double d);

	double getD();

	double getI();

	double getP();

	void setControlMode(ETalonControlMode follower);

	void set(int talonId);

	int getEncVelocity();

	void set(double d);

	ETalonControlMode getControlMode();

	void setFeedbackDevice(EFeedbackDevice analogencoder);

	void setVoltageRampRate(double rate);

	int getEncPosition();

}

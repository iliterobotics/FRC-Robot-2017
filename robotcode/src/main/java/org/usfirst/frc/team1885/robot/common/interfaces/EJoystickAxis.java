package org.usfirst.frc.team1885.robot.common.interfaces;

public enum EJoystickAxis {
	kX(0), kY(1), kZ(2), kTwist(3), kThrottle(4), kNumAxis(5);

	public final int value;

	private EJoystickAxis(int value) {
		this.value = value;
	}
}